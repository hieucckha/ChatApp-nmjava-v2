package org.nmjava.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final short PORT = 9999;

    public static final int NUM_THREADS = 50;

    public Server(int port, int numThreads) {
        ServerSocket servSock;
        try {
            servSock = new ServerSocket(port);
        } catch (IOException ioEx) {
            throw new RuntimeException("Could not create ServerSocket ", ioEx);
        }

        for (int i = 0; i < numThreads; ++i) {
            new org.nmjava.chatapp.server.Handler(servSock, i).start();
        }
    }

    public static void main(String[] argv) {
        new Server(PORT, NUM_THREADS);
    }
}
