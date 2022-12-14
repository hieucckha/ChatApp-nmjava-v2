package org.nmjava.chatapp.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConnectSimple {
    String mesg = "Hello cross the net";

    public static void main(String[] argv) {
        new ConnectSimple().converse("localhost");
    }

    protected void converse(String hostname) {
        try (Socket sock = new Socket(hostname, 9999);) {
            BufferedReader is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter os = new PrintWriter(sock.getOutputStream(), true);

            os.println(mesg + "\r\n");
            os.flush();
            String reply = is.readLine();
            System.out.println("Sent \"" + mesg + "\"");
            System.out.println("Got \"" + reply + "\"");
        } catch (IOException ioEx) {
            System.out.println(ioEx);
        }
    }
}
