package org.nmjava.chatapp.client.networks;

import org.apache.commons.lang3.ObjectUtils;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.requests.Request;
import org.nmjava.chatapp.commons.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class ClientSocket {
    private static ClientSocket instance;

    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private final LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();
    private final Thread requestProcess = new Thread(new RequestProcess(), "Request Process");

    private final LinkedBlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
    private final Thread responseReceive = new Thread(new ResponseReceive(), "Response Receive");
    private final Thread responseProcess = new Thread(new ResponseProcess(), "Response Process");

    private ClientSocket() {
    }

    public static ClientSocket getInstance() {
        if (instance == null) {
            synchronized (ClientSocket.class) {
                instance = new ClientSocket();
            }
        }

        return instance;
    }

    public void sendRequest(Request request) throws IOException {
        this.outputStream.writeObject(request);
        this.outputStream.flush();
    }

    public boolean startConnection(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        requestProcess.start();
        responseReceive.start();
        responseProcess.start();

        return true;
    }

    public void close() throws IOException {
        this.requestProcess.interrupt();
        this.responseReceive.interrupt();
        this.responseProcess.interrupt();

        if (this.inputStream != null)
            this.inputStream.close();
        if (this.outputStream != null)
            this.outputStream.close();
        if (this.clientSocket != null)
            this.clientSocket.close();
    }


    public void addRequestToQueue(Request request) {
        this.requestQueue.add(request);
    }

    private class RequestProcess implements Runnable {
        @Override
        public void run() {
            do {
                Request request = requestQueue.poll();
                if (request == null)
                    continue;

                try {
                    sendRequest(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!Thread.currentThread().isInterrupted());
        }
    }

    private class ResponseReceive implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    Object objet = inputStream.readObject();
                    if (ObjectUtils.isEmpty(objet))
                        responseQueue.add((Response) objet);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } while (!Thread.currentThread().isInterrupted());
        }
    }

    private class ResponseProcess implements Runnable {
        private final Map<ResponseType, Consumer<Response>> handlers = new HashMap<>();

        private void registerResponseHandler() {
            handlers.put(ResponseType.MESSAGE, ResponseHandler::MESSAGE_);
        }

        @Override
        public void run() {
            registerResponseHandler();

            do {
                Response response = responseQueue.poll();
                if (ObjectUtils.isEmpty(response))
                    continue;

                handlers.get(response.getType()).accept(response);
            } while (!Thread.currentThread().isInterrupted());
        }
    }

}
