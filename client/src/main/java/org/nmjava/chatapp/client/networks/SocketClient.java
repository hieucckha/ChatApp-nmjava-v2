package org.nmjava.chatapp.client.networks;

import org.apache.commons.lang3.ObjectUtils;
import org.nmjava.chatapp.commons.requests.Request;
import org.nmjava.chatapp.commons.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();
    private final Thread requestProcess = new Thread(new RequestProcess(), "Request Process");

    private final LinkedBlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
    private final Thread responseReceive = new Thread(new ResponseReceive(), "Response Receive");

    private void sendRequest(Request request) throws IOException {
        this.out.writeObject(request);
        this.out.flush();
    }

    public boolean startConnection(String ip, int port) {
        try {
            System.out.println("Start Connect");
            clientSocket = new Socket(ip, port);
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println("Finish connect");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        requestProcess.start();
        responseReceive.start();

        return true;
    }

    public void close() throws IOException {
        this.requestProcess.interrupt();
        this.responseReceive.interrupt();

        if (this.in != null)
            this.in.close();
        if (this.out != null)
            this.out.close();
        if (this.clientSocket != null)
            this.clientSocket.close();
    }


    public void addRequestToQueue(Request request) {
        this.requestQueue.add(request);
    }

    public Response getResponseFromQueue() {
        return this.responseQueue.poll();
    }

    // Class for send request to server
    // request is read from requestQueue
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

    // Class for receive response from server
    // Read object from stream and push to responseQueue
    private class ResponseReceive implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    Object objet = in.readObject();
                    if (ObjectUtils.isNotEmpty(objet))
                        responseQueue.add((Response) objet);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } while (!Thread.currentThread().isInterrupted());
        }
    }
}
