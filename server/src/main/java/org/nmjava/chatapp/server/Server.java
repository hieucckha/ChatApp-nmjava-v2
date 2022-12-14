package org.nmjava.chatapp.server;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.nmjava.chatapp.commons.enums.Action;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.requests.Request;
import org.nmjava.chatapp.commons.responses.Response;
import org.nmjava.chatapp.commons.responses.UserOnlineResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

    private ServerSocket serverSocket;
    private Map<String, ClientHandler> clientHandlers;

    public Server() {
        this.clientHandlers = new HashMap<>();
    }

    public void start(int port) {
        System.out.println("Server starting!!!");
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(serverSocket.getInetAddress().getHostName());
            System.out.println(serverSocket.getLocalPort());
            while (true) {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                clientHandler.start();
                this.clientHandlers.put(clientHandler.getUid(), clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getUserIdOnline() {
        return this.clientHandlers.values().stream()
                .map(ClientHandler::getUid)
                .collect(Collectors.toList());
    }


    @Getter
    @Setter
    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private String uid;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            this.uid = UUID.randomUUID().toString();
        }

        private void response(Response response) throws IOException {
            this.out.writeObject(response);
            this.out.flush();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object input = in.readObject();
                    if (ObjectUtils.isNotEmpty(input)) {
                        Request request = (Request) input;
                        switch (request.getAction()) {
                            case GET_USERS_ONLINE: {
                                this.response(UserOnlineResponse.builder()
                                        .userIds(getUserIdOnline())
                                        .statusCode(StatusCode.OK)
                                        .build());
                                break;
                            }
                            case DISCONNECT: {
                                clientHandlers.remove(this.getUid());
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}