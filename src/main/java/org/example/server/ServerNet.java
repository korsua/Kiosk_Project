package org.example.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNet {
    public static ExecutorService threadPool;
    public static Vector<ClientNet> clients = new Vector<>();
    ServerSocket serverSocket = null;

    //    Socket soc = null;
//    PrintWriter writer = null;
//    BufferedReader reader = null;
//    String lineStr;
    public ServerNet() {
    }

    public void startServer() {
        try {
            System.out.println("접속대기");
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost",5505));
        } catch (IOException e) {
            System.out.println("fail");
            e.printStackTrace();
            if (!serverSocket.isClosed()) {
                stopServer();
            }
            return;
        }
        Runnable thread = () -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept(); // 이게 문제야
                    clients.add(new ClientNet(socket));
                    System.out.println("[클라이언트 접속]"
                            + socket.getRemoteSocketAddress());
                } catch (Exception e) {
                    System.out.println("실패");
                    if (!serverSocket.isClosed()) {
                        stopServer();
                    }
                    break;
                }
            }
        };
        System.out.println("222");
        threadPool = Executors.newCachedThreadPool();
        threadPool.submit(thread);

    }

    public void stopServer() {
        try {
            Iterator<ClientNet> iterator = clients.iterator();
            while (iterator.hasNext()) {
                ClientNet clientNet = iterator.next();
                clientNet.socket.close();
                iterator.remove();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//        try {
//            serverSocket = new ServerSocket(5505);
//
//            while(true){
//                soc = serverSocket.accept();
//                System.out.println(soc.getInetAddress() + "님이 입장 하였습니다.");
//
//                writer = new PrintWriter(soc.getOutputStream(),true);
//                reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
//
//                while((lineStr = reader.readLine()) != null){
//                    writer.write(lineStr);
//                    writer.println(lineStr);
//                    System.out.println("입력 값 "+ lineStr);
//                }
//
//                writer.close();;
//                reader.close();
//                soc.close();
//            }
//
//    } catch(
//    IOException e)
//
//    {
//        throw new RuntimeException(e);
//    }
public static void main(String[] args) {
        new ServerNet().startServer();
}
}
