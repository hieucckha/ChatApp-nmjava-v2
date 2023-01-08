package org.nmjava.chatapp.server;

import org.nmjava.chatapp.server.app.SocketServer;

public class ServerMain {
    public static void main(String[] args) {
        new SocketServer().start(9999);
    }
}
