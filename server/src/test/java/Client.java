package org.nmjava.chatapp.server;

import org.apache.commons.lang3.ObjectUtils;
import org.nmjava.chatapp.commons.enums.Action;
import org.nmjava.chatapp.commons.requests.InformationRequest;
import org.nmjava.chatapp.commons.requests.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private void sendRequest(Request req) throws IOException {
        this.out.writeObject(req);
        this.out.flush();
    }

    private void close() throws IOException {
        if (this.in != null) {
            this.in.close();
        }
        if (this.out != null) {
            this.out.close();
        }
        if (this.clientSocket != null) {
            this.clientSocket.close();
        }
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());

            sendRequest(InformationRequest.builder().action(Action.DISCONNECT).build());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }

                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class ResponseProcess extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Object object = in.readObject();
                    if (ObjectUtils.isEmpty(object)) {
                        continue;
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
