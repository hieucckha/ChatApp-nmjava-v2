package org.nmjava.chatapp.server;

import org.nmjava.chatapp.server.app.Server;

public class Main {
    public static void main(String[] args) {
        new Server().start(9999);
    }
}
