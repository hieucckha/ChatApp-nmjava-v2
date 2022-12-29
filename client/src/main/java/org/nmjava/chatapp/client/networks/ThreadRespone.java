package org.nmjava.chatapp.client.networks;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.controllers.ListReqAddFriendController;
import org.nmjava.chatapp.client.controllers.LoginController;
import org.nmjava.chatapp.client.utils.SceneController;
import org.nmjava.chatapp.commons.enums.ResponseType;
import org.nmjava.chatapp.commons.enums.StatusCode;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.responses.AddFriendResponse;
import org.nmjava.chatapp.commons.responses.AuthenticationResponse;
import org.nmjava.chatapp.commons.responses.GetListRequestFriendResponse;
import org.nmjava.chatapp.commons.responses.Response;

import java.util.Collection;
import java.util.Objects;

public class ThreadRespone implements Runnable {
    private Thread thrd;
    private String currentName;

    private Alert a;
    public ThreadRespone(String name) {
        thrd = new Thread(this, name);
        currentName = name;
        System.out.println("Thread " + currentName + " khoi tao");
        thrd.start();
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
                            for(Friend friend:friends){
                                System.out.println(friend.getUsername());
                                ListReqAddFriendController.listReqAddFriend.add(friend);
                            }
//                            ListReqAddFriendController.reqlistContainer.setContent(ListReqAddFriendController.createContactMessageList());
//                            ListReqAddFriendController.reqlistContainer.setContent(ListReqAddFriendController.createContactMessageList());
                        }
                        else{
                            System.out.println("Not found Request Add Friend");
                        }
                        break;
                    }
                    case ADD_FRIEND -> {
                        AddFriendResponse res = (AddFriendResponse) response;

                        ListReqAddFriendController.listReqAddFriend.add(new Friend(res.getFriend(),false));
//                        ListReqAddFriendController.reqlistContainer.setContent(ListReqAddFriendController.createContactMessageList());
                        break;
                    }
                }

            } while (response == null);
        }
    }
}
