package org.nmjava.chatapp.client.networks;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.components.*;
import org.nmjava.chatapp.client.controllers.CreateGroupChatController;
import org.nmjava.chatapp.client.controllers.UserHomeController;
import org.nmjava.chatapp.client.utils.SceneController;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Conservation;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.models.Message;
import org.nmjava.chatapp.commons.requests.*;
import org.nmjava.chatapp.commons.responses.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadRespone implements Runnable {


    private Thread thrd;
    public String currentName;

    public static ScrollPane reqlistContainer;
    public static ScrollPane spContainer;

    public static ScrollPane conservationContainer;

    public static ScrollPane listMemberContainer;
    public static ScrollPane listMemberGroupContainer;


    private Alert a;

    public ThreadRespone(String name) {
        thrd = new Thread(this, name);
        currentName = name;
        System.out.println("Thread " + currentName + " khoi tao");
        thrd.start();
    }

    public VBox createContactMessageList() {
        VBox contactMessageList = new ContactMessageList();
        if (Objects.isNull(UserHomeController.listReqAddFriend)) return contactMessageList;
        for (Friend friend : UserHomeController.listReqAddFriend) {
            System.out.println("In component: " + friend.getUsername());
            contactMessageList.getChildren().add(new ReqAddFriendCard(friend.getUsername()));
        }
        UserHomeController.listReqAddFriend = new ArrayList<>();
        return contactMessageList;
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            public void run() {
                a = new Alert(Alert.AlertType.NONE);
            }
        });

        Response response = null;
        while (true) {

            response = null;
            do {
                try {
                    thrd.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println(currentName+": " + !Main.stage.getTitle().equals(currentName));
//                System.out.println(currentName);
                if (!Main.stage.getTitle().equals(currentName)) {
                    break;
                }
//                System.out.println(currentName);
                response = Main.socketClient.getResponseFromQueue();
                if (response == null) {
                    continue;
                }
                System.out.println("Type: " + response.getType());

                switch (response.getType()) {
//                    case Get_Info_conservation ->{
//                        AuthenticationResponse res = (AuthenticationResponse) response;
//                        if (res.getStatusCode() == StatusCode.OK) {
//                            if(res.conID.equals(UserHomeController.conservationID))
//                            {
//                                UserHomeController.utc.setUserName(res.getNameConserVation());
//                            }
//                        }
//                    }
                    case AUTHENTICATION -> {
                        AuthenticationResponse res = (AuthenticationResponse) response;
                        if (res.getStatusCode() == StatusCode.OK) {
                            System.out.println(res.getRole());
                            if(res.getRole()==1){
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        a.setAlertType(Alert.AlertType.CONFIRMATION);
                                        a.setContentText("Dang nhap thanh cong");
                                        // show the dialog
                                        a.show();

                                        Main.stage.setScene(SceneController.staticGetScene("UserHome"));
                                        Main.stage.setTitle("UserHome");

                                        Main.stage.show();
                                    }
                                });
                            }
                            else {
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        a.setAlertType(Alert.AlertType.CONFIRMATION);
                                        a.setContentText("Dang nhap thanh cong");
                                        // show the dialog
                                        a.show();

                                        Main.stage.setScene(SceneController.staticGetScene("AdminHome"));
                                        Main.stage.setTitle("AdminHome");

                                        Main.stage.show();
                                    }
                                });
                            }

                        } else if (res.getStatusCode() == StatusCode.NOT_FOUND) {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    a.setAlertType(Alert.AlertType.WARNING);
                                    a.setContentText("Vui long nhap lai tai khoan mat khau");
                                    // show the dialog
                                    a.show();
                                }
                            });

                        }
                        break;
                    }
                    case CREATE_ACCOUNT -> {

                        if (response.getStatusCode() == StatusCode.OK) {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    a.setAlertType(Alert.AlertType.CONFIRMATION);
                                    a.setContentText("Dang ki thanh cong");
                                    // show the dialog
                                    a.show();
                                    Main.stage.setScene(SceneController.staticGetScene("Login"));
                                    Main.stage.show();
                                }
                            });
                        } else {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    a.setAlertType(Alert.AlertType.WARNING);
                                    a.setContentText("Tai Khoan da ton tai");
                                    // show the dialog
                                    a.show();
                                    Main.stage.setScene(SceneController.staticGetScene("Login"));
                                    Main.stage.show();
                                }
                            });

                        }
                        break;
                    }
                    case FORGOT_PASSWORD -> {
                        if (response.getStatusCode() == StatusCode.OK) {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    a.setAlertType(Alert.AlertType.CONFIRMATION);
                                    a.setContentText("Reset Thanh Cong");
                                    // show the dialog
                                    a.show();
                                }
                            });
                        }
                        else{
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    a.setAlertType(Alert.AlertType.WARNING);
                                    a.setContentText("Mail ko ton tai hoac da co loi xay ra luc khoi tao lai pw");
                                    // show the dialog
                                    a.show();
                                }
                            });
                        }

                        break;
                    }
                    case GET_LIST_REQUEST_FRIEND -> {
                        GetListRequestFriendResponse res = (GetListRequestFriendResponse) response;
                        Collection<Friend> friends = res.getFriends();

                        if (!friends.isEmpty()) {
                            UserHomeController.listReqAddFriend = new ArrayList<>();
                            for (Friend friend : friends) {
                                System.out.println(friend.getUsername());
                                UserHomeController.listReqAddFriend.add(friend);
                            }
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    reqlistContainer.setContent(createContactMessageList());
                                }
                            });

                        } else {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    reqlistContainer.setContent(createContactMessageList());
                                }
                            });
                            System.out.println("Not found Request Add Friend");
                        }
                        break;
                    }
                    case ADD_FRIEND,ACCEPT_REQUEST_FRIEND ,REJECT_REQUEST_FRIEND -> {
                        Main.socketClient.addRequestToQueue(GetListRequestFriendRequest.builder().username(Main.UserName).build());
                        Main.socketClient.addRequestToQueue(GetListConservationRequest.builder().username(Main.UserName).build());

                        break;
                    }
                    case GET_LIST_FRIEND -> {
                        GetListFriendResponse res = (GetListFriendResponse) response;
                        Collection<Friend> friends = res.getFriends();
                        if (!friends.isEmpty()) {
                            VBox friendOnlineList = new FriendOnlineList();
                            for (Friend friend : friends) {
                                friendOnlineList.getChildren().add(new FriendCard(friend.getUsername()));
                            }
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    spContainer.setContent(friendOnlineList);
                                }
                            });

                        } else {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    spContainer.setContent(new VBox());
                                }
                            });
                        }
                        break;
                    }
                    case GET_LIST_CONSERVATION -> {
                        GetListConservationResponse res = (GetListConservationResponse) response;
                        Collection<Conservation> conservations = res.getConservations();
                        if (!conservations.isEmpty()) {
                            System.out.println(conservations.size());
                            VBox friendOnlineList = new FriendOnlineList();
                            for (Conservation conservation : conservations) {
                                System.out.println(conservation.getName());
                                if (!conservation.getIsGroup()) {
                                    String[] listName = conservation.getName().split(" ");
                                    if (listName[1].equals(Main.UserName)) {
                                        FriendOnlineCard newContact = new FriendOnlineCard(listName[0]);
                                        newContact.setOnMouseClicked(e -> {
                                            Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservation.getConservationID()).build());
                                            UserHomeController.utc.setUserName(listName[0]);
                                            UserHomeController.conservationID = conservation.getConservationID();
                                        });
                                        friendOnlineList.getChildren().add(newContact);
                                    } else {

                                        FriendOnlineCard newContact = new FriendOnlineCard(listName[1]);
                                        newContact.setOnMouseClicked(e -> {
                                            Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservation.getConservationID()).build());
                                            UserHomeController.utc.setUserName(listName[1]);
                                            UserHomeController.conservationID = conservation.getConservationID();
                                        });
                                        friendOnlineList.getChildren().add(newContact);
                                    }
                                } else {
                                    FriendOnlineCard newContact = new FriendOnlineCard(conservation.getName());
                                    newContact.setOnMouseClicked(e -> {
                                        Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservation.getConservationID()).build());
                                        UserHomeController.utc.setUserName(conservation.getName());
                                        UserHomeController.conservationID = conservation.getConservationID();

                                    });
                                    friendOnlineList.getChildren().add(newContact);
                                }

                            }
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    spContainer.setContent(friendOnlineList);
                                }
                            });

                        } else {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    spContainer.setContent(new VBox());
                                }
                            });
                        }
                        break;
                    }
                    case GET_LIST_FRIEND_ONLINE -> {
                        GetListFriendOnlineResponse res = (GetListFriendOnlineResponse) response;
                        Collection<Friend> friends = res.getFriends();
                        if (!friends.isEmpty()) {
                            VBox friendOnlineList = new FriendOnlineList();
                            for (Friend friend : friends) {
                                friendOnlineList.getChildren().add(new FriendOnlineCard(friend.getUsername()));
                            }
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    spContainer.setContent(friendOnlineList);
                                }
                            });

                        } else {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    spContainer.setContent(new VBox());
                                }
                            });
                        }
                        break;
                    }
                    case SEARCH_MESSAGE_ALL -> {
                        SearchMessageAllResponse res = (SearchMessageAllResponse) response;
                        Collection<Message> messages = res.getMessages();
                        if (!messages.isEmpty()) {
                            VBox ContactMessageList = new ContactMessageList();
                            for (Message msg : messages) {
                                HBox timeLabel = new HBox();
                                timeLabel.setAlignment(Pos.CENTER);
                                timeLabel.getChildren().add(new Label(msg.getCreateAt().toString()));
                                timeLabel.setFillHeight(true);
                                ContactMessageList.getChildren().add(0,timeLabel);
                                if (msg.getSender().equals(Main.UserName)) {
                                    ContactMessageList.getChildren().add(0,new messageFind(msg, true));
                                } else {
                                    if (msg.getSender().equals("system")) {
                                        HBox newLine = new HBox();
                                        newLine.setAlignment(Pos.CENTER);
                                        newLine.getChildren().add(new Label(msg.getMessage()));
                                        newLine.setFillHeight(true);
                                        ContactMessageList.getChildren().add(0,newLine);
                                    }
                                    else{
                                        ContactMessageList.getChildren().add(0,new messageFind(msg, false));
                                    }

                                }
                            }
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    spContainer.setContent(ContactMessageList);
                                }
                            });

                        }
                    }
                    case SEARCH_MESSAGE_CONSERVATION -> {
                        SearchMessageConservationResponse res = (SearchMessageConservationResponse) response;
                        Collection<Message> messages = res.getMessages();
                        String conservationID = res.getConservationID();
                        System.out.println(conservationID);
                        if (!conservationID.equals(UserHomeController.conservationID)) break;
                        System.out.println("hi");
                        if (!messages.isEmpty()) {
                            System.out.println("hi");
                            VBox ContactMessageList = new ContactMessageList();
                            for (Message msg : messages) {
                                HBox timeLabel = new HBox();
                                timeLabel.setAlignment(Pos.CENTER);
                                timeLabel.getChildren().add(new Label(msg.getCreateAt().toString()));
                                timeLabel.setFillHeight(true);
                                ContactMessageList.getChildren().add(0,timeLabel);
                                if (msg.getSender().equals(Main.UserName)) {
                                    ContactMessageList.getChildren().add(0,new conservationLine(msg, true));
                                } else {
                                    if (msg.getSender().equals("system")) {
                                        HBox newLine = new HBox();
                                        newLine.setAlignment(Pos.CENTER);
                                        newLine.getChildren().add(new Label(msg.getMessage()));
                                        newLine.setFillHeight(true);
                                        ContactMessageList.getChildren().add(0,newLine);
                                    }
                                    else{
                                        ContactMessageList.getChildren().add(0,new conservationLine(msg, false));
                                    }

                                }
                            }
                            Platform.runLater(new Runnable() {
                                public void run() {

                                    conservationContainer.setContent(ContactMessageList);
                                }
                            });

                        }
                    }
                    case GET_LIST_MESSAGE_CONSERVATION -> {
                        GetListMessageConservationResponse res = (GetListMessageConservationResponse) response;
                        Collection<Message> messages = res.getMessages();
                        String conservationID = res.getConservationID();
//                        System.out.println(messages.(messages.size()-1).getSender());
                        if (!conservationID.equals(UserHomeController.conservationID)) break;
                        if (!messages.isEmpty()) {
                            VBox ContactMessageList = new ContactMessageList();
                            for (Message msg : messages) {
                                int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
                                if (randomNum < 50) {
                                    HBox newLine = new HBox();
                                    newLine.setAlignment(Pos.CENTER);
                                    newLine.getChildren().add(new Label(msg.getCreateAt().toString()));
                                    newLine.setFillHeight(true);
                                    ContactMessageList.getChildren().add(0,newLine);
                                }
                                if (msg.getSender().equals(Main.UserName)) {
                                    ContactMessageList.getChildren().add(0,new conservationLine(msg, true));
                                } else {
                                    if (msg.getSender().equals("system")) {
                                        HBox newLine = new HBox();
                                        newLine.setAlignment(Pos.CENTER);
                                        newLine.getChildren().add(new Label(msg.getMessage()));
                                        newLine.setFillHeight(true);
                                        ContactMessageList.getChildren().add(0,newLine);
                                    }
                                    else{
                                        ContactMessageList.getChildren().add(0,new conservationLine(msg, false));
                                    }

                                }
                            }
                            Platform.runLater(new Runnable() {
                                public void run() {

                                    conservationContainer.setContent(ContactMessageList);
                                }
                            });

                        } else {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    conservationContainer.setContent(new VBox());

                                }
                            });
                        }
                        break;
                    }
                    case SEND_MESSAGE -> {
                        SentMessageResponse res = (SentMessageResponse) response;
                        String conservationID = res.getConservationID();
                        Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservationID).build());
                        break;
                    }
                    case DELETE_MESSAGE -> {
                        DeleteMessageResponse res = (DeleteMessageResponse) response;
                        String conservationID = res.getConservationID();
                        Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservationID).build());
                        break;
                    }
                    case CREATE_GROUP_CHAT,GIVE_ADMIN_USER_GROUP_CHAT-> {
                        Main.socketClient.addRequestToQueue(GetListConservationRequest.builder().username(Main.UserName).build());
                        break;
                    }
                    case   ADD_MEMBER_GROUP_CHAT  -> {
                        AddMemberToGroupResponse res = (AddMemberToGroupResponse) response;
                        Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().conservationID(res.getConservationID()).username(Main.UserName).build());
                        Main.socketClient.addRequestToQueue(GetListConservationRequest.builder().username(Main.UserName).build());
                        if( !Objects.isNull(UserHomeController.conservationID))
                        {
                            Main.socketClient.addRequestToQueue(GetListMemberConservationRequest.builder().conservationID(UserHomeController.conservationID).build());
                        }
                        break;
                    }
                    case RENAME_GROUP_CHAT ->{
                        RenameGroupChatResponse res = (RenameGroupChatResponse) response;
                        if(res.getConservationID().equals(UserHomeController.conservationID))
                        {
                            UserHomeController.conservationID = res.getConservationID();
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    UserHomeController.utc.setUserName(res.getNewName());
                                }
                            });
                            Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().conservationID(res.getConservationID()).username(Main.UserName).build());
                        }
                        Main.socketClient.addRequestToQueue(GetListConservationRequest.builder().username(Main.UserName).build());
                        break;
                    }
                    case REMOVE_USER_GROUP_CHAT->{
                        RemoveUserGroupChatResponse res = (RemoveUserGroupChatResponse) response;
                        if( !Objects.isNull(UserHomeController.conservationID))
                        {
                            if(res.getConservationId().equals(UserHomeController.conservationID) && res.getMember().equals(Main.UserName))
                            {
                                UserHomeController.conservationID = new String();
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        UserHomeController.utc.setUserName("");
                                        ThreadRespone.conservationContainer.setContent(new VBox());
                                    }
                                });


                            }
                            else{
                                Main.socketClient.addRequestToQueue(GetListMemberConservationRequest.builder().conservationID(UserHomeController.conservationID).build());
                            }
                        }

                        Main.socketClient.addRequestToQueue(GetListConservationRequest.builder().username(Main.UserName).build());
                        break;
                    }
                    case CHECK_USER_EXIST -> {
                        CheckUserExistResponse res = (CheckUserExistResponse) response;
                        Boolean isExist = res.getIsExist();
                        System.out.println(isExist);
                        if(isExist){
                            boolean yourselt = res.getUsername().equals(Main.UserName);
                            if(!CreateGroupChatController.listMember.contains(res.getUsername()) && !yourselt) {
                                CreateGroupChatController.listMember.add(res.getUsername());
                                VBox MemberList = new FriendOnlineList();
                                MemberList.getChildren().add(new Label("Member Just add to List"));
                                MemberList.getChildren().add(new FriendCard(res.getUsername()));
                                Platform.runLater(new Runnable() {
                                public void run() {
                                    listMemberContainer.setContent(MemberList);
                                }
                              });
                            }
                            else {
                                VBox MemberList = new FriendOnlineList();
                                MemberList.getChildren().add(new Label("Member was in List"));
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        listMemberContainer.setContent(MemberList);
                                    }
                                });
                            }
                        }
                        else {
                            VBox MemberList = new FriendOnlineList();
                            MemberList.getChildren().add(new Label("Not Found"));
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    listMemberContainer.setContent(MemberList);
                                }
                            });
                        }

                        break;
                    }
                    case GET_LIST_MEMBER_CONSERVATION -> {
                        GetListMemberConservationResponse res = (GetListMemberConservationResponse) response;
                        Collection<String> listMembers =   res.getUsers();
                        VBox MemberList = new FriendOnlineList();
                        MemberList.getChildren().add(new Label("List Member"));
                        for (String member : listMembers) {
                            System.out.println(member);
                            MemberList.getChildren().add(new FriendCard(member));
                        }
                        Platform.runLater(new Runnable() {
                            public void run() {
                                listMemberGroupContainer.setContent(MemberList);
                            }
                        });
                        break;
                    }
                    case UNFRIEND -> {
                        Main.socketClient.addRequestToQueue(GetListFriendRequest.builder().username(Main.UserName).build());
                    }

                }

            } while (response == null);
        }
    }
}
