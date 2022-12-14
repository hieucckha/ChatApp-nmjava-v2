package org.nmjava.chatapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Handler extends Thread {
    ServerSocket servSock;
    int threadNumber;

    Handler(ServerSocket s, int i) {
        servSock = s;
        threadNumber = i;
        setName("Thread " + threadNumber);
    }

    public void run() {
        while (true) {
            try {
                System.out.println(getName() + " waiting");

                Socket clientSock;

                synchronized (servSock) {
                    clientSock = servSock.accept();
                }
                System.out.println(getName() + " starting, IP=" + clientSock.getInetAddress());
                try (BufferedReader is = new BufferedReader(new InputStreamReader(clientSock.getInputStream())); PrintStream os = new PrintStream(clientSock.getOutputStream(), true);) {
                    String line;
                    while ((line = is.readLine()) != null) {
                        os.print(line + "\r\n");
                        os.flush();
                    }

                    System.out.println(getName() + " ENDED");
                    clientSock.close();
                }
            } catch (IOException ioEx) {
                System.out.println(getName() + ": IO Error on socket " + ioEx);
                return;
            }
        }
    }
}
