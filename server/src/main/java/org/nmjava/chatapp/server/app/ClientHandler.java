package org.nmjava.chatapp.server.app;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.nmjava.chatapp.commons.daos.ConservationDao;
import org.nmjava.chatapp.commons.daos.FriendDao;
import org.nmjava.chatapp.commons.daos.UserDao;
import org.nmjava.chatapp.commons.enums.RequestType;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Conservation;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.models.User;
import org.nmjava.chatapp.commons.requests.*;
import org.nmjava.chatapp.commons.responses.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

@Getter
@Setter
public class ClientHandler implements Runnable {
    private Map<RequestType, BiConsumer<ClientHandler, Request>> handlers = registerHandler();

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

    @Override
    public void run() {
        try {
            while (true) {
                Object input = this.inputStream.readObject();
                if (ObjectUtils.isNotEmpty(input)) {
                    Request request = (Request) input;

                    handlers.get(request.getType()).accept(this, request);
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

    private void response(Response response) throws IOException {
        this.outputStream.writeObject(response);
        this.outputStream.flush();
    }

    public HashMap<RequestType, BiConsumer<ClientHandler, Request>> registerHandler() {
        HashMap<RequestType, BiConsumer<ClientHandler, Request>> commands = new HashMap<>();

        RequestHandler requestHandler = new RequestHandler();

        commands.put(RequestType.AUTHENTICATION, requestHandler::AUTHENTICATION_);
        commands.put(RequestType.CREATE_ACCOUNT, requestHandler::CREATE_ACCOUNT_);
        commands.put(RequestType.FORGOT_PASSWORD, requestHandler::FORGOT_PASSWORD_);
        commands.put(RequestType.GET_LIST_CONSERVATION, requestHandler::GET_LIST_CONSERVATION_);
        commands.put(RequestType.GET_LIST_MESSAGE_CONSERVATION, requestHandler::GET_LIST_MESSAGE_CONSERVATION_);
        commands.put(RequestType.GET_LIST_FRIEND, requestHandler::GET_LIST_FRIEND_);
        commands.put(RequestType.GET_LIST_FRIEND_ONLINE, requestHandler::GET_LIST_FRIEND_ONLINE_);
        commands.put(RequestType.GET_LIST_REQUEST_FRIEND, requestHandler::GET_LIST_REQUEST_FRIEND_);
        commands.put(RequestType.ACCEPT_REQUEST_FRIEND, requestHandler::ACCEPT_REQUEST_FRIEND_);
        commands.put(RequestType.SEND_MESSAGE, requestHandler::SEND_MESSAGE_);
        commands.put(RequestType.DELETE_MESSAGE, requestHandler::DELETE_MESSAGE_);
        commands.put(RequestType.SEARCH_MESSAGE_USER, requestHandler::SEARCH_MESSAGE_USER_);
        commands.put(RequestType.SEARCH_MESSAGE_ALL, requestHandler::SEARCH_MESSAGE_ALL_);
        commands.put(RequestType.CREATE_GROUP_CHAT, requestHandler::CREATE_GROUP_CHAT_);
        commands.put(RequestType.RENAME_GROUP_CHAT, requestHandler::RENAME_GROUP_CHAT_);
        commands.put(RequestType.ADD_MEMBER_GROUP_CHAT, requestHandler::ADD_MEMBER_GROUP_CHAT_);
        commands.put(RequestType.GIVE_ADMIN_USER_GROUP_CHAT, requestHandler::GIVE_ADMIN_USER_GROUP_CHAT_);
        commands.put(RequestType.REMOVE_USER_GROUP_CHAT, requestHandler::REMOVE_USER_GROUP_CHAT_);

        return commands;
    }


    private class RequestHandler {
        public void AUTHENTICATION_(ClientHandler clientHandler, Request request) {
            AuthenticationRequest req = (AuthenticationRequest) request;

            String username = req.getUsername();
            String password = req.getPassword();

            UserDao userDao = new UserDao();

            try {
                Optional<String> userID = userDao.isAuthUser(username, password);

                if (userID.isPresent()) {
                    clientHandler.response(AuthenticationResponse.builder().userID(userID.get()).statusCode(StatusCode.OK).build());
                } else {
                    clientHandler.response((AuthenticationResponse.builder().statusCode(StatusCode.NOT_FOUND).build()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void CREATE_ACCOUNT_(ClientHandler clientHandler, Request request) {
            CreateAccountRequest req = (CreateAccountRequest) request;

            String username = req.getUsername();
            String password = req.getPassword();
            String fullName = req.getFullName();
            String address = req.getAddress();
            LocalDate dateOfBirth = req.getDateOfBirth();
            String gender = req.getGender();
            String email = req.getEmail();

            UserDao userDao = new UserDao();
            Optional<String> userID = userDao.save(new User(username, password, fullName, address, dateOfBirth, gender, email, false, true, LocalDateTime.now()));

            try {
                if (userID.isPresent()) {
                    clientHandler.response(CreateAccountResponse.builder().statusCode(StatusCode.OK).build());
                } else {
                    clientHandler.response(CreateAccountResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                }
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        public void FORGOT_PASSWORD_(ClientHandler clientHandler, Request request) {

        }

        public void GET_LIST_CONSERVATION_(ClientHandler clientHandler, Request request) {
            GetListConservationRequest req = (GetListConservationRequest) request;

            String userID = req.getUserID();

            ConservationDao conservationDao = new ConservationDao();

            Collection<Conservation> conservations = conservationDao.getListConservation(userID);
            try {
                clientHandler.response(GetListConservationResponse.builder().conservations(conservations).statusCode(StatusCode.OK).build());
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        public void GET_LIST_MESSAGE_CONSERVATION_(ClientHandler clientHandler, Request request) {

        }

        public void GET_LIST_FRIEND_(ClientHandler clientHandler, Request request) {
            GetListFriendRequest req = (GetListFriendRequest) request;

            String userID = req.getUserID();

            FriendDao friendDao = new FriendDao();

            Collection<Friend> friends = friendDao.getListFriend(userID);
            try {
                clientHandler.response(GetListFriendResponse.builder().friends(friends).statusCode(StatusCode.OK).build());
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        public void GET_LIST_FRIEND_ONLINE_(ClientHandler clientHandler, Request request) {
            GetListFriendRequest req = (GetListFriendRequest) request;

            String userID = req.getUserID();

            FriendDao friendDao = new FriendDao();

            Collection<Friend> friends = friendDao.getListFriend(userID);
            try {
                clientHandler.response(GetListFriendOnlineResponse.builder().friends(friends).statusCode(StatusCode.OK).build());
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        public void GET_LIST_REQUEST_FRIEND_(ClientHandler clientHandler, Request request) {
            GetListRequestFriendRequest req = (GetListRequestFriendRequest) request;

            String userID = req.getUserID();

            FriendDao friendDao = new FriendDao();

            Collection<Friend> friends = friendDao.getListRequestFriend(userID);
            try {
                clientHandler.response(GetListRequestFriendResponse.builder().friends(friends).statusCode(StatusCode.OK).build());
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        public void ACCEPT_REQUEST_FRIEND_(ClientHandler clientHandler, Request request) {
            AcceptRequestFriendRequest req = (AcceptRequestFriendRequest) request;

            String userID = req.getUserID();
            String friendID = req.getFriendID();

            FriendDao friendDao = new FriendDao();

            Optional<Boolean> isSuccess = friendDao.acceptFriend(userID, friendID);
            try {
                if (isSuccess.isPresent())
                    clientHandler.response(AcceptRequestFriendResponse.builder().statusCode(StatusCode.OK).build());
                else
                    clientHandler.response(AcceptRequestFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        public void SEND_MESSAGE_(ClientHandler clientHandler, Request request) {

        }

        public void DELETE_MESSAGE_(ClientHandler clientHandler, Request request) {

        }

        public void SEARCH_MESSAGE_USER_(ClientHandler clientHandler, Request request) {

        }

        public void SEARCH_MESSAGE_ALL_(ClientHandler clientHandler, Request request) {

        }

        public void CREATE_GROUP_CHAT_(ClientHandler clientHandler, Request request) {

        }

        public void RENAME_GROUP_CHAT_(ClientHandler clientHandler, Request request) {

        }

        public void ADD_MEMBER_GROUP_CHAT_(ClientHandler clientHandler, Request request) {

        }

        public void GIVE_ADMIN_USER_GROUP_CHAT_(ClientHandler clientHandler, Request request) {

        }

        public void REMOVE_USER_GROUP_CHAT_(ClientHandler clientHandler, Request request) {

        }
    }
}