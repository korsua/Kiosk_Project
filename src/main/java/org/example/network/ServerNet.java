package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNet {
    ServerSocket serverSocket = null;
    Socket soc = null;
    PrintWriter writer = null;
    BufferedReader reader = null;
    String lineStr;

    public ServerNet() {
        try {
            serverSocket = new ServerSocket(5505);

            while(true){
                soc = serverSocket.accept();
                System.out.println(soc.getInetAddress() + "님이 입장 하였습니다.");

                writer = new PrintWriter(soc.getOutputStream(),true);
                reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));

                while((lineStr = reader.readLine()) != null){
                    writer.write(lineStr);
                    System.out.println("입력 값 "+ lineStr);
                }

                writer.close();;
                reader.close();
                soc.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
