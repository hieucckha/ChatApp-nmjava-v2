package org.nmjava.chatapp.server.app;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.nmjava.chatapp.commons.enums.RequestType;
import org.nmjava.chatapp.commons.requests.Request;
import org.nmjava.chatapp.commons.responses.Response;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@Setter
public class ClientHandler implements Runnable {
    private static Map<RequestType, Consumer<Request>> handlers = registerHandler();

    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String uid;


    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.inputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        this.outputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.uid = UUID.randomUUID().toString();

        System.out.println("UID: " + getUid() + " is created!!");
        Thread.currentThread().setName(getUid());
    }

    private void response(Response response) throws IOException {
        this.outputStream.writeObject(response);
        this.outputStream.flush();
    }

    public static HashMap<RequestType, Consumer<Request>> registerHandler() {
        HashMap<RequestType, Consumer<Request>> commands = new HashMap<>();

        commands.put(RequestType.AUTHENTICATION, RequestHandler::AUTHENTICATION_);

        return commands;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object input = this.inputStream.readObject();
                if (ObjectUtils.isNotEmpty(input)) {
                    Request request = (Request) input;

                    handlers.get(request.getType()).accept(request);
                }
            }
        } catch (EOFException e) {
            // When client socket close()
            // this exception will throw because the input stream is close with socket
            System.out.println("Client disconnect");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("UID: " + getUid() + " is delete!!");

            try {
                if (this.inputStream != null) {
                    this.inputStream.close();
                }

                if (this.outputStream != null) {
                    this.outputStream.close();
                }

                if (this.clientSocket != null) {
                    this.clientSocket.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}