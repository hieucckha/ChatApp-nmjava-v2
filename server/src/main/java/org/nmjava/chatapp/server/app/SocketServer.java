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
import org.nmjava.chatapp.server.services.SendMailService;

import javax.mail.MessagingException;
import java.io.*;
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
                clientHandlerHashMap.remove(this.getUid());
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
            commands.put(RequestType.CHECK_USER_EXIST, requestHandler::CHECK_USER_EXIST_);

            commands.put(RequestType.GET_LIST_FRIEND, requestHandler::GET_LIST_FRIEND_);
            commands.put(RequestType.GET_LIST_FRIEND_ONLINE, requestHandler::GET_LIST_FRIEND_ONLINE_);
            commands.put(RequestType.GET_LIST_REQUEST_FRIEND, requestHandler::GET_LIST_REQUEST_FRIEND_);
            commands.put(RequestType.UNFRIEND, requestHandler::UNFRIEND_);
            commands.put(RequestType.ADD_FRIEND, requestHandler::ADD_FRIEND_);
            commands.put(RequestType.ACCEPT_REQUEST_FRIEND, requestHandler::ACCEPT_REQUEST_FRIEND_);
            commands.put(RequestType.REJECT_REQUEST_FRIEND, requestHandler::REJECT_REQUEST_FRIEND_);

            commands.put(RequestType.GET_LIST_CONSERVATION, requestHandler::GET_LIST_CONSERVATION_);
            commands.put(RequestType.GET_LIST_MESSAGE_CONSERVATION, requestHandler::GET_LIST_MESSAGE_CONSERVATION_);
            commands.put(RequestType.SEND_MESSAGE, requestHandler::SEND_MESSAGE_);
            commands.put(RequestType.DELETE_MESSAGE, requestHandler::DELETE_MESSAGE_);
            commands.put(RequestType.SEARCH_MESSAGE_CONSERVATION, requestHandler::SEARCH_MESSAGE_CONSERVATION_);
            commands.put(RequestType.SEARCH_MESSAGE_ALL, requestHandler::SEARCH_MESSAGE_ALL_);

            commands.put(RequestType.CREATE_GROUP_CHAT, requestHandler::CREATE_GROUP_CHAT_);
            commands.put(RequestType.GET_LIST_MEMBER_CONSERVATION, requestHandler::GET_LIST_MEMBER_CONSERVATION_);
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

                    if (isSuccess.isEmpty()) {
                        clientHandler.response(AuthenticationResponse.builder().statusCode(StatusCode.NOT_FOUND).build());
                        return;
                    }

                    isSuccess = userDao.updateUserOnline(username, true);

                    if (isSuccess.isEmpty()) {
                        clientHandler.response(AuthenticationResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    Optional<Boolean> isAdmin = userDao.isAdmin(username);
                    if (isAdmin.isPresent()) {
                        clientHandler.response(AuthenticationResponse.builder().role(0).statusCode(StatusCode.OK).build());
                        return;
                    }

                    clientHandler.response(AuthenticationResponse.builder().role(1).statusCode(StatusCode.OK).build());
                    addClientHandlerMap(username, clientHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void CREATE_ACCOUNT_(ClientHandler clientHandler, Request request) {
                CreateAccountRequest req = (CreateAccountRequest) request;

                String username = req.getUsername();
                String password = req.getPassword();
                System.out.println("Password owr creqteAccount server### " + password);
                String fullName = req.getFullName();
                String address = req.getAddress();
                LocalDate dateOfBirth = req.getDateOfBirth();
                String gender = req.getGender();
                String email = req.getEmail();

                UserDao userDao = new UserDao();
                Optional<String> userID = userDao.save(new User(username, password, fullName, address, dateOfBirth, gender, email, false, true, LocalDateTime.now()));

                try {
                    if (userID.isEmpty()) {
                        clientHandler.response(CreateAccountResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(CreateAccountResponse.builder().statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void FORGOT_PASSWORD_(ClientHandler clientHandler, Request request) {
                ForgotPasswordRequest req = (ForgotPasswordRequest) request;

                String email = req.getEmail();

                UserDao userDao = new UserDao();
                Optional<String> newPassword = userDao.resetPassword(email);

                try {
                    if (newPassword.isEmpty()) {
                        clientHandler.response(ForgotPasswordResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    Optional<String> username = userDao.getUsernameByEmail(email);

                    Boolean isSuccess = SendMailService.sendMail(email, "Reset pasword", "Your username is: <b>%s</b><br>Your new password: <b>%s</b>".formatted(username.get(), newPassword.get()));

                    if (!isSuccess) {
                        clientHandler.response(ForgotPasswordResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(ForgotPasswordResponse.builder().statusCode(StatusCode.OK).build());
                } catch (MessagingException | IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void CHECK_USER_EXIST_(ClientHandler clientHandler, Request request) {
                CheckUserExistRequest req = (CheckUserExistRequest) request;

                String username = req.getUsername();

                UserDao userDao = new UserDao();

                Optional<Boolean> isExists = userDao.isUserExists(username);

                try {
                    if (isExists.isEmpty()) {
                        clientHandler.response(CheckUserExistResponse.builder().isExist(false).statusCode(StatusCode.OK).username(username).build());
                    } else {
                        clientHandler.response(CheckUserExistResponse.builder().isExist(true).statusCode(StatusCode.OK).username(username).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
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
                for (Friend fr : friends) {
                    System.out.println(fr.getUsername());
                }

                try {
                    clientHandler.response(GetListRequestFriendResponse.builder().friends(friends).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void UNFRIEND_(ClientHandler clientHandler, Request request) {
                UnfriendRequest req = (UnfriendRequest) request;

                String user = req.getUser();
                String friend = req.getFriend();

                FriendDao friendDao = new FriendDao();

                Optional<Boolean> isSuccess = friendDao.unfriend(user, friend);

                try {
                    if (isSuccess.isEmpty()) {
                        clientHandler.response(UnfriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(UnfriendResponse.builder().user(user).friend(friend).statusCode(StatusCode.OK).build());

                    ClientHandler other = getClientHandlerMap(friend);
                    if (other != null) {
                        other.response(UnfriendResponse.builder().user(user).friend(friend).statusCode(StatusCode.OK).build());
                    }
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
                    if (isSuccess.isEmpty()) {
                        clientHandler.response(AddFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(AddFriendResponse.builder().statusCode(StatusCode.OK).build());

                    ClientHandler other = getClientHandlerMap(friend);
                    if (other != null) {
                        other.response(AddFriendResponse.builder().user(user).friend(friend).statusCode(StatusCode.OK).build());
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
                    if (isSuccess.isEmpty()) {
                        clientHandler.response(AcceptRequestFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    Optional<Boolean> isExists = conservationDao.isConservationBefore(user, friend);
                    if (isExists.isEmpty()) {
                        isSuccess = conservationDao.createConservation(user, new ArrayList<String>() {{
                            add(friend);
                        }}, false);

                        if (isSuccess.isEmpty()) {
                            clientHandler.response(AcceptRequestFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                            return;
                        }
                    }

                    clientHandler.response(AcceptRequestFriendResponse.builder().statusCode(StatusCode.OK).build());

                    ClientHandler other = getClientHandlerMap(user);
                    if (other != null) {
                        other.response(AddFriendResponse.builder().user(user).friend(friend).statusCode(StatusCode.OK).build());
                    }
                    other = getClientHandlerMap(friend);
                    if (other != null) {
                        other.response(AddFriendResponse.builder().user(user).friend(friend).statusCode(StatusCode.OK).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void REJECT_REQUEST_FRIEND_(ClientHandler clientHandler, Request request) {
                RejectRequestFriendRequest req = (RejectRequestFriendRequest) request;

                String user = req.getUser();
                String friend = req.getFriend();

                FriendDao friendDao = new FriendDao();

                Optional<Boolean> isSuccess = friendDao.rejectFriend(user, friend);
                try {
                    if (isSuccess.isEmpty()) {
                        clientHandler.response(RejectRequestFriendResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(RejectRequestFriendResponse.builder().statusCode(StatusCode.OK).build());
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
                    clientHandler.response(GetListMessageConservationResponse.builder().messages(messages).conservationID(conservationID).statusCode(StatusCode.OK).build());
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
                            other.response(SentMessageResponse.builder().conservationID(conservationID).sender(user).message(message).statusCode(StatusCode.OK).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void DELETE_MESSAGE_(ClientHandler clientHandler, Request request) {
                DeleteMessageRequest req = (DeleteMessageRequest) request;

                String conservationID = req.getConservationID();
                String username = req.getUsername();
                String messageID = req.getMessageID();

                ConservationDao conservationDao = new ConservationDao();
                Optional<Boolean> isSuccess = conservationDao.deleteMessage(conservationID, username, messageID);

                try {
                    if (isSuccess.isEmpty()) {
                        clientHandler.response(DeleteMessageResponse.builder().statusCode(StatusCode.BAD_REQUEST).conservationID(conservationID).build());
                        return;
                    }

                    clientHandler.response(DeleteMessageResponse.builder().statusCode(StatusCode.OK).conservationID(conservationID).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void SEARCH_MESSAGE_CONSERVATION_(ClientHandler clientHandler, Request request) {
                SearchMessageConservationRequest req = (SearchMessageConservationRequest) request;

                String conservationID = req.getConservationID();
                String text = req.getText();

                ConservationDao conservationDao = new ConservationDao();
                Collection<Message> messages = conservationDao.searchMessageInConservation(conservationID, text);

                try {
                    clientHandler.response(SearchMessageConservationResponse.builder().messages(messages).statusCode(StatusCode.OK).conservationID(conservationID).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void SEARCH_MESSAGE_ALL_(ClientHandler clientHandler, Request request) {
                SearchMessageAllRequest req = (SearchMessageAllRequest) request;

                String text = req.getText();

                ConservationDao conservationDao = new ConservationDao();
                Collection<Message> messages = conservationDao.searchAllMessage(text);

                try {
                    clientHandler.response(SearchMessageAllResponse.builder().messages(messages).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void CREATE_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                CreateGroupChatRequest req = (CreateGroupChatRequest) request;

                String creator = req.getCreator();
                List<String> members = req.getMembers();

                ConservationDao conservationDao = new ConservationDao();
                Optional<Boolean> isSuccess = conservationDao.createConservation(creator, members, true);

                try {
                    if (isSuccess.isEmpty()) {
                        clientHandler.response(CreateGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(CreateGroupChatResponse.builder().statusCode(StatusCode.OK).build());

                    for (String member : members) {
                        ClientHandler other = getClientHandlerMap(member);

                        if (other != null)
                            other.response(CreateGroupChatResponse.builder().statusCode(StatusCode.OK).build());
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void GET_LIST_MEMBER_CONSERVATION_(ClientHandler clientHandler, Request request) {
                GetListMemberConservationRequest req = (GetListMemberConservationRequest) request;

                String conservationID = req.getConservationID();

                ConservationDao conservationDao = new ConservationDao();
                Collection<String> users = conservationDao.getMember(conservationID);

                try {
                    clientHandler.response(GetListMemberConservationResponse.builder().users(users).statusCode(StatusCode.OK).build());
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void RENAME_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                RenameGroupChatRequest req = (RenameGroupChatRequest) request;

                String conservationID = req.getConservationID();
                String username = req.getUsername();
                String newName = req.getNewName();

                ConservationDao conservationDao = new ConservationDao();

                Optional<Boolean> isSuccess = conservationDao.renameConservation(conservationID, newName);
                try {
                    if (isSuccess.isEmpty()) {
                        clientHandler.response(RenameGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(RenameGroupChatResponse.builder().conservationID(conservationID).username(username).newName(newName).statusCode(StatusCode.OK).build());

                    ArrayList<String> memberInGroup = (ArrayList<String>) conservationDao.sentMessage(conservationID, "system", String.format("Group chat was renamed to %s by %s", newName, username));
                    for (String user : memberInGroup) {
                        ClientHandler other = getClientHandlerMap(user);

                        if (other != null) {
                            System.out.println(user);
                            other.response(RenameGroupChatResponse.builder().conservationID(conservationID).username(username).newName(newName).statusCode(StatusCode.OK).build());
                            System.out.println("Gui " + user);
                        }
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

                    // Check role of adder
                    if (role.get() > 1)
                        clientHandler.response(AddMemberToGroupResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    Optional<Boolean> isSuccess = conservationDao.addMemberGroupChat(conservationID, member);

                    if (isSuccess.isEmpty()) {
                        clientHandler.response(AddMemberToGroupResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(AddMemberToGroupResponse.builder().conservationID(conservationID).adder(adder).member(member).statusCode(StatusCode.OK).build());
                    conservationDao.sentMessage(conservationID, "system", String.format("%s was added to group by %s", member, adder));
                    ArrayList<String> memberInGroup = (ArrayList<String>) conservationDao.getMember(conservationID);

                    for (String user : memberInGroup) {
                        ClientHandler other = getClientHandlerMap(user);
                        if (other != null) {
                            other.response(AddMemberToGroupResponse.builder().conservationID(conservationID).adder(adder).member(member).statusCode(StatusCode.OK).build());

                        }
                    }
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

                Optional<Integer> role = conservationDao.getRoleUserConservation(conservationID, admin);
                try {
                    if (role.isEmpty())
                        clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    if (role.get() > 1)
                        clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    Optional<Boolean> isSuccess = conservationDao.giveAdminUserGroupChat(conservationID, member);

                    if (isSuccess.isEmpty()) {
                        clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.OK).build());

                    ArrayList<String> memberInGroup = (ArrayList<String>) conservationDao.sentMessage(conservationID, "system", String.format("%s was promoted to admin by %s", member, admin));
                    for (String user : memberInGroup) {
                        ClientHandler other = getClientHandlerMap(user);

                        if (other != null) {
                            other.response(GiveAdminUserGroupChatResponse.builder().statusCode(StatusCode.OK).build());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            public void REMOVE_USER_GROUP_CHAT_(ClientHandler clientHandler, Request request) {
                RemoveUserGroupChatRequest req = (RemoveUserGroupChatRequest) request;

                String conservationID = req.getConservationID();
                String admin = req.getAdmin();
                String member = req.getMember();

                ConservationDao conservationDao = new ConservationDao();

                Optional<Integer> role = conservationDao.getRoleUserConservation(conservationID, admin);
                try {
                    if (role.isEmpty())
                        clientHandler.response(RemoveUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());

                    if (role.get() > 1)
                        clientHandler.response(RemoveUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                    ArrayList<String> memberInGroup = (ArrayList<String>) conservationDao.getMember(conservationID);
                    Optional<Boolean> isSuccess = conservationDao.removeUserGroupChat(conservationID, member);

                    if (isSuccess.isEmpty()) {
                        clientHandler.response(RemoveUserGroupChatResponse.builder().statusCode(StatusCode.BAD_REQUEST).build());
                        return;
                    }

                    clientHandler.response(RemoveUserGroupChatResponse.builder().conservationId(conservationID).member(member).statusCode(StatusCode.OK).build());
                    conservationDao.sentMessage(conservationID, "system", String.format("%s was removed from group chat by %s", member, admin));

                    for (String user : memberInGroup) {
                        ClientHandler other = getClientHandlerMap(user);

                        if (other != null) {
                            other.response(RemoveUserGroupChatResponse.builder().conservationId(conservationID).member(member).statusCode(StatusCode.OK).build());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}