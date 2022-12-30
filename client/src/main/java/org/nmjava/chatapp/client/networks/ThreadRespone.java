package org.nmjava.chatapp.client.networks;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.components.*;
import org.nmjava.chatapp.client.controllers.ListReqAddFriendController;
import org.nmjava.chatapp.client.controllers.LoginController;
import org.nmjava.chatapp.client.controllers.UserHomeController;
import org.nmjava.chatapp.client.utils.SceneController;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Conservation;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.requests.GetListConservationRequest;
import org.nmjava.chatapp.commons.requests.GetListFriendRequest;
import org.nmjava.chatapp.commons.requests.GetListMessageConservationRequest;
import org.nmjava.chatapp.commons.requests.GetListRequestFriendRequest;
import org.nmjava.chatapp.commons.responses.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ThreadRespone implements Runnable {
    private Thread thrd;
    public String currentName;

    public  static ScrollPane reqlistContainer;


    public static ScrollPane spContainer;


    private Alert a;
    public ThreadRespone(String name) {
        thrd = new Thread(this, name);
        currentName = name;
        System.out.println("Thread " + currentName + " khoi tao");
        thrd.start();
    }
    public  VBox createContactMessageList() {
        VBox contactMessageList = new ContactMessageList();
        if(Objects.isNull( UserHomeController.listReqAddFriend)) return contactMessageList;
        for(Friend friend :  UserHomeController.listReqAddFriend){
            System.out.println("In component: " + friend.getUsername());
            contactMessageList.getChildren().add(new ReqAddFriendCard(friend.getUsername()));
        }
        UserHomeController.listReqAddFriend = new ArrayList<>();
        return contactMessageList;
    }
    @Override
    public void run() {
        Platform.runLater(new Runnable(){
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
                if(!Main.stage.getTitle().equals(currentName)){
                    break;
                }
//                System.out.println(currentName);
                response = Main.socketClient.getResponseFromQueue();
                if (response == null) {
                    continue;
                }
                System.out.println("Type: " + response.getType());

                switch (response.getType())
                {
                    case AUTHENTICATION -> {
                        AuthenticationResponse res = (AuthenticationResponse) response;
                        if(res.getStatusCode()== StatusCode.OK){
                            Platform.runLater(new Runnable(){
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
                        else if(res.getStatusCode()== StatusCode.NOT_FOUND) {
                            Platform.runLater(new Runnable(){
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

                        if(response.getStatusCode()== StatusCode.OK){
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    a.setAlertType(Alert.AlertType.CONFIRMATION);
                                    a.setContentText("Dang ki thanh cong");
                                    // show the dialog
                                    a.show();
                                    Main.stage.setScene(SceneController.staticGetScene("Login"));
                                    Main.stage.show();
                                }
                            });
                        }
                        else {
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    a.setAlertType(Alert.AlertType.WARNING);
                                    a.setContentText("Tai Khoan da ton tai");
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

                        if(!friends.isEmpty()){
                            UserHomeController.listReqAddFriend = new ArrayList<>();
                            for(Friend friend:friends){
                                System.out.println(friend.getUsername());
                                UserHomeController.listReqAddFriend.add(friend);
                            }
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    reqlistContainer.setContent(createContactMessageList());
                                }
                            });

                        }
                        else{
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    reqlistContainer.setContent(createContactMessageList());
                                }
                            });
                            System.out.println("Not found Request Add Friend");
                        }
                        break;
                    }
                    case ADD_FRIEND -> {
                        Main.socketClient.addRequestToQueue(GetListRequestFriendRequest.builder().username(Main.UserName).build());
                        break;
                    }
                    case GET_LIST_FRIEND -> {
                        GetListFriendResponse res = (GetListFriendResponse) response;
                        Collection<Friend> friends = res.getFriends();
                        if(!friends.isEmpty()){
                            VBox friendOnlineList = new FriendOnlineList();
                            for (Friend friend:friends) {
                                friendOnlineList.getChildren().add(new FriendCard(friend.getUsername()));
                            }
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    spContainer.setContent(friendOnlineList);
                                }
                            });

                        }
                        else{
                            Platform.runLater(new Runnable(){
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
                        if(!conservations.isEmpty()){
                            System.out.println(conservations.size());
                            VBox friendOnlineList = new FriendOnlineList();
                            for (Conservation conservation:conservations) {
                                System.out.println(conservation.getName());
                                if(!conservation.getIsGroup())
                                {
                                    String [] listName = conservation.getName().split(" ");
                                    if(listName[1].equals(Main.UserName))
                                    {
                                        FriendOnlineCard newContact = new FriendOnlineCard(listName[0]);
                                        newContact.setOnMouseClicked(e -> {
                                            Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservation.getConservationID()).build());
                                        });
                                        friendOnlineList.getChildren().add(newContact);
                                    }
                                    else {
                                        FriendOnlineCard newContact = new FriendOnlineCard(listName[1]);
                                        newContact.setOnMouseClicked(e -> {
                                            Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservation.getConservationID()).build());
                                        });
                                        friendOnlineList.getChildren().add(newContact);
                                    }
                                }
                                else {
                                    FriendOnlineCard newContact = new FriendOnlineCard(conservation.getName());
                                    newContact.setOnMouseClicked(e -> {
                                        Main.socketClient.addRequestToQueue(GetListMessageConservationRequest.builder().username(Main.UserName).conservationID(conservation.getConservationID()).build());
                                    });
                                    friendOnlineList.getChildren().add(newContact);
                                }
                            }
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    spContainer.setContent(friendOnlineList);
                                }
                            });

                        }
                        else{
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    spContainer.setContent(new VBox());
                                }
                            });
                        }
                        break;
                    }case GET_LIST_FRIEND_ONLINE -> {
                    GetListFriendOnlineResponse res = (GetListFriendOnlineResponse) response;
                    Collection<Friend> friends = res.getFriends();
                    if(!friends.isEmpty()){
                        VBox friendOnlineList = new FriendOnlineList();
                        for (Friend friend:friends) {
                            friendOnlineList.getChildren().add(new FriendOnlineCard(friend.getUsername()));
                        }
                        Platform.runLater(new Runnable(){
                            public void run() {
                                spContainer.setContent(friendOnlineList);
                            }
                        });

                    }
                    else{
                        Platform.runLater(new Runnable(){
                            public void run() {
                                spContainer.setContent(new VBox());
                            }
                        });
                    }
                    break;
                }
                }

            } while (response == null);
        }
    }
}
