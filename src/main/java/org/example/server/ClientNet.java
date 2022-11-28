package org.example.server;

import org.example.model.Order;

import java.io.*;
import java.net.Socket;

public class ClientNet {
    Order order;
    public Socket socket = null;
    PrintWriter writer = null;
    BufferedReader reader = null;

    public void send(String message) {
        Runnable thread = () -> {
            try {
                OutputStream out = socket.getOutputStream();
                byte[] buffer = message.getBytes("UTF-8");
                out.write(buffer);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    System.out.println("[메시지 송신 오류]"
                            + socket.getRemoteSocketAddress()
                            + " : "
                            + Thread.currentThread().getName());
                    ServerNet.clients.remove(ClientNet.this);
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        ServerNet.threadPool.submit(thread);
    }

    public void receive() {
        Runnable thread = () -> {
            try {
                while (true) {
                    InputStream in = socket.getInputStream();
                    byte[] buffer = new byte[512];
                    int length = in.read(buffer);
                    while (length == -1) throw new IOException();
                    System.out.println("[메시지 수신 성공]"
                            + socket.getRemoteSocketAddress()
                            + " : " + Thread.currentThread().getName()
                    );
                    String message = new String(buffer, 0, length, "UTF-8");
                    for (ClientNet client : ServerNet.clients) {
                        client.send(message);
                    }
                }
            } catch (Exception e) {
                try {
                    System.out.println("[메시지 수신 오류]"
                            + socket.getRemoteSocketAddress());
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
        };
        ServerNet.threadPool.submit(thread);

    }

    public ClientNet(Socket socket) {
        this.socket = socket;
        receive();
    }
//        try {
//        socket = new Socket("localhost", 5505);
//        writer = new PrintWriter(socket.getOutputStream(), true);
//        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//        String str = null;
//        BufferedReader cReader = new BufferedReader(new InputStreamReader(System.in));
//
//        while ((str = cReader.readLine()) != null) {
//            writer.println(str);
//            System.out.println("출력된 값 " + str);
//            System.out.println(reader.readLine());
//        }
//
//
//        writer.close();
//        reader.close();
//        cReader.close();
//        socket.close();
//    } catch (UnknownHostException e) {
//        throw new RuntimeException(e);
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }

}
