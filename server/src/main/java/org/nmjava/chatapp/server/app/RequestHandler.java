package org.nmjava.chatapp.server.app;

import org.nmjava.chatapp.commons.requests.Request;

public class RequestHandler {
    public static void DISCONNECT_(Request request) {
        System.out.println("Client disconnect");
    }
}
