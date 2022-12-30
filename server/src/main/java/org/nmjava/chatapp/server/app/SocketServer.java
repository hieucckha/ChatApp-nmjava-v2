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
import org.nmjava.chatapp.commons.models.Message;
import org.nmjava.chatapp.commons.models.User;
import org.nmjava.chatapp.commons.requests.*;
import org.nmjava.chatapp.commons.responses.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

public class SocketServer {

    private ServerSocket serverSocket;
    private static Map<String, ClientHandler> clientHandlerHashMap = new HashMap<>(); // Username - ClientHandler

    public void start(int port) {
        System.out.println("Server starting!!!");
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(serverSocket.getInetAddress().getHostName());
            System.out.println(serverSocket.getLocalPort());
            while (true) {
                new Thread(new ClientHandler(serverSocket.accept())).start();
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

    public synchronized void addClientHandlerMap(String username, ClientHandler clientHandler) {
        clientHandlerHashMap.put(username, clientHandler);
    }

    public synchronized ClientHandler getClientHandlerMap(String username) {
        return clientHandlerHashMap.get(username);
    }

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

            commands.put(RequestType.GET_LIST_FRIEND, requestHandler::GET_LIST_FRIEND_);
            commands.put(RequestType.GET_LIST_FRIEND_ONLINE, requestHandler::GET_LIST_FRIEND_ONLINE_);
            commands.put(RequestType.GET_LIST_REQUEST_FRIEND, requestHandler::GET_LIST_REQUEST_FRIEND_);
            commands.put(RequestType.ADD_FRIEND, requestHandler::ADD_FRIEND_);
            commands.put(RequestType.ACCEPT_REQUEST_FRIEND, requestHandler::ACCEPT_REQUEST_FRIEND_);
            commands.put(RequestType.REJECT_REQUEST_FRIEND, requestHandler::REJECT_REQUEST_FRIEND_);

            commands.put(RequestType.GET_LIST_CONSERVATION, requestHandler::GET_LIST_CONSERVATION_);
            commands.put(RequestType.GET_LIST_MESSAGE_CONSERVATION, requestHandler::GET_LIST_MESSAGE_CONSERVATION_);
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
                    Optional<Boolean> isSuccess = userDao.isAuthUser(username, password);

                    if (isSuccess.isPresent()) {
                        clientHandler.response((AuthenticationResponse.builder().statusCode(StatusCode.OK).build()));
                        addClientHandlerMap(username, clientHandler);
                    } else
                        clientHandler.response((AuthenticationResponse.builder().statusCode(StatusCode.NOT_FOUND).build()));
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

            public void GET_LIST_FRIEND_(ClientHandler clientHandler, Request request) {
                GetListFriendRequest req = (GetListFriendRequest) request;

                String username = req.getUsername();

                FriendDao friendDao = new FriendDao();

                Collection<Friend> friends = friendDao.getListFriend(username);
                try {
                    clientHandler.response(GetListFriendResponse.builder().friends(friends).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void GET_LIST_FRIEND_ONLINE_(ClientHandler clientHandler, Request request) {
                GetListFriendOnlineRequest req = (GetListFriendOnlineRequest) request;

                String username = req.getUsername();

                FriendDao friendDao = new FriendDao();

                Collection<Friend> friends = friendDao.getListFriendOnline(username);
                try {
                    clientHandler.response(GetListFriendOnlineResponse.builder().friends(friends).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void GET_LIST_REQUEST_FRIEND_(ClientHandler clientHandler, Request request) {
                GetListRequestFriendRequest req = (GetListRequestFriendRequest) request;

                String username = req.getUsername();
                System.out.println(username);
                FriendDao friendDao = new FriendDao();

                Collection<Friend> friends = friendDao.getListRequestFriend(username);
                for(Friend fr : friends){
                    System.out.println(fr.getUsername());
                }
                try {
                    clientHandler.response(GetListRequestFriendResponse.builder().friends(friends).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void ADD_FRIEND_(ClientHandler clientHandler, Request request) {
                AddFriendRequest req = (AddFriendRequest) request;

                String user = req.getUser();
                String friend = req.getFriend();

                FriendDao friendDao = new FriendDao();

                Optional<Boolean> isSuccess = friendDao.addFriend(user, friend);
                try {
                    if (isSuccess.isPresent()) {
                        clientHandler.response(AddFriendResponse.builder().statusCode(StatusCode.OK).build());

                        ClientHandler other = getClientHandlerMap(friend);
                        if (other != null) {
                            other.response(AddFriendResponse.builder().user(user).friend(friend).statusCode(StatusCode.OK).build());
                        }
                    } else {
                        clientHandler.response(AddFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void ACCEPT_REQUEST_FRIEND_(ClientHandler clientHandler, Request request) {
                AcceptRequestFriendRequest req = (AcceptRequestFriendRequest) request;

                String user = req.getUser();
                String friend = req.getFriend();

                FriendDao friendDao = new FriendDao();
                ConservationDao conservationDao = new ConservationDao();

                Optional<Boolean> isSuccess = friendDao.acceptFriend(user, friend);
                try {
                    if (isSuccess.isPresent()) {
                        clientHandler.response(AcceptRequestFriendResponse.builder().statusCode(StatusCode.OK).build());

                        ClientHandler other = getClientHandlerMap(user);
                        if (other != null) {
                            other.response(AddFriendResponse.builder().user(user).friend(friend).statusCode(StatusCode.OK).build());
                        }

                        conservationDao.createConservation(user, new ArrayList<String>() {{
                            add(friend);
                        }}, false);


                    } else
                        clientHandler.response(AcceptRequestFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void REJECT_REQUEST_FRIEND_(ClientHandler clientHandler, Request request) {
                RejectRequestFriendRequest req = (RejectRequestFriendRequest) request;

                String user = req.getUser();
                String friend = req.getFriend();

                FriendDao friendDao = new FriendDao();

                Optional<Boolean> isSuccess = friendDao.acceptFriend(user, friend);
                try {
                    if (isSuccess.isPresent()) {
                        clientHandler.response(RejectRequestFriendResponse.builder().statusCode(StatusCode.OK).build());
                    } else
                        clientHandler.response(RejectRequestFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void GET_LIST_CONSERVATION_(ClientHandler clientHandler, Request request) {
                GetListConservationRequest req = (GetListConservationRequest) request;

                String username = req.getUsername();

                ConservationDao conservationDao = new ConservationDao();

                Collection<Conservation> conservations = conservationDao.getListConservation(username);
                System.out.println(conservations.size());
                try {
                    clientHandler.response(GetListConservationResponse.builder().conservations(conservations).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void GET_LIST_MESSAGE_CONSERVATION_(ClientHandler clientHandler, Request request) {
                GetListMessageConservationRequest req = (GetListMessageConservationRequest) request;

                String conservationID = req.getConservationID();
                String username = req.getUsername();

                ConservationDao conservationDao = new ConservationDao();
                Collection<Message> messages = conservationDao.getListMessageConservation(conservationID, username);
                try {
                    clientHandler.response(GetListMessageConservationResponse.builder().messages(messages).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void SEND_MESSAGE_(ClientHandler clientHandler, Request request) {
                SendMessageRequest req = (SendMessageRequest) request;

                String conservationID = req.getConservationID();
                String username = req.getUsername();
                String message = req.getMessage();

                ConservationDao conservationDao = new ConservationDao();
                ArrayList<String> users = (ArrayList<String>) conservationDao.sentMessage(conservationID, username, message);
                try {
                    clientHandler.response(SentMessageResponse.builder().conservationID(conservationID).sender(username).message(message).statusCode(StatusCode.OK).build());
                    for (String user : users) {
                        ClientHandler other = getClientHandlerMap(user);
                        if (other != null)
                            other.response(SentMessageResponse.builder().conservationID(conservationID).sender(username).message(message).statusCode(StatusCode.OK).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void DELETE_MESSAGE_(ClientHandler clientHandler, Request request) {
                DeleteMessageRequest req = (DeleteMessageRequest) request;

                String conservatioID = req.getConservationID();
                String username = req.getUsername();
                String messgeID = req.getMessageID();

                ConservationDao conservationDao = new ConservationDao();
                Optional<Boolean> isSuccess = conservationDao.deleteMessage(conservatioID, username, messgeID);

                try {
                    if (isSuccess.isPresent())
                        clientHandler.response(DeleteMessageResponse.builder().statusCode(StatusCode.OK).build());
                    else
                        clientHandler.response(DeleteMessageResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void SEARCH_MESSAGE_USER_(ClientHandler clientHandler, Request request) {

            }

            public void SEARCH_MESSAGE_ALL_(ClientHandler clientHandler, Request request) {

            }

            public void CREATE_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                CreateGroupChatRequest req = (CreateGroupChatRequest) request;

                String creator = req.getCreator();
                List<String> members = req.getMembers();

                ConservationDao conservationDao = new ConservationDao();
                Optional<Boolean> isSuccess = conservationDao.createConservation(creator, members, true);

                try {
                    if (isSuccess.isPresent()) {
                        clientHandler.response(CreateGroupChatResponse.builder().statusCode(StatusCode.OK).build());
                    } else {
                        clientHandler.response(CreateGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void RENAME_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                RenameGroupChatRequest req = (RenameGroupChatRequest) request;

                String conservationID = req.getConservationID();
                String newName = req.getNewName();

                ConservationDao conservationDao = new ConservationDao();

                Optional<Boolean> isSuccess = conservationDao.renameConservation(conservationID, newName);
                try {
                    if (isSuccess.isPresent()) {
                        clientHandler.response(RenameGroupChatResponse.builder().statusCode(StatusCode.OK).build());
                    } else {
                        clientHandler.response(RenameGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void ADD_MEMBER_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                AddMemberGroupChatRequest req = (AddMemberGroupChatRequest) request;

                String conservationID = req.getConservationID();
                String adder = req.getAdder();
                String member = req.getMember();

                ConservationDao conservationDao = new ConservationDao();

                Optional<Integer> role = conservationDao.getRoleUserConservation(conservationID, adder);
                try {
                    if (role.isEmpty())
                        clientHandler.response(AddMemberToGroupResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    if (role.get() > 1)
                        clientHandler.response(AddMemberToGroupResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    Optional<Boolean> isSuccess = conservationDao.addMemberGroupChat(conservationID, member);

                    if (isSuccess.isEmpty())
                        clientHandler.response(AddMemberToGroupResponse.builder().conservationID(conservationID).adder(adder).member(member).statusCode(StatusCode.OK).build());
                    else
                        clientHandler.response(AddMemberToGroupResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void GIVE_ADMIN_USER_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                GiveAdminUserGroupChat req = (GiveAdminUserGroupChat) request;

                String conservationID = req.getConservationID();
                String admin = req.getAdmin();
                String member = req.getMember();

                ConservationDao conservationDao = new ConservationDao();

                Optional<Integer> role = conservationDao.getRoleUserConservation(conservationID, member);
                try {
                    if (role.isEmpty())
                        clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    if (role.get() > 1)
                        clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    Optional<Boolean> isSuccess = conservationDao.giveAdminUserGroupChat(conservationID, member);

                    if (isSuccess.isEmpty())
                        clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.OK).build());
                    else
                        clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void REMOVE_USER_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                RemoveUserGroupChatRequest req = (RemoveUserGroupChatRequest) request;

                String conservationID = req.getConservationID();
                String admin = req.getAdmin();
                String memeber = req.getMember();

                ConservationDao conservationDao = new ConservationDao();

                Optional<Integer> role = conservationDao.getRoleUserConservation(conservationID, admin);
                try {
                    if (role.isEmpty())
                        clientHandler.response(RemoveUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    if (role.get() > 1)
                        clientHandler.response(RemoveUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    Optional<Boolean> isSuccess = conservationDao.removeUserGroupChat(conservationID, memeber);

                    if (isSuccess.isEmpty())
                        clientHandler.response(RemoveUserGroupChatResponse.builder().statusCode(StatusCode.OK).build());
                    else
                        clientHandler.response(RemoveUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}