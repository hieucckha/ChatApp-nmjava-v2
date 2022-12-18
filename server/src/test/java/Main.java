package org.nmjava.chatapp.server;

public class clientmain {
    public static void main(String[] args) {
        new Client().startConnection("localhost", 9999);
    }
}
