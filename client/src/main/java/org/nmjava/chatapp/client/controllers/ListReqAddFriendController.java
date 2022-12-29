package org.nmjava.chatapp.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.components.ContactMessageCard;
import org.nmjava.chatapp.client.components.ContactMessageList;
import org.nmjava.chatapp.client.components.ReqAddFriendCard;
import org.nmjava.chatapp.client.networks.ThreadRespone;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.requests.AddFriendRequest;
import org.nmjava.chatapp.commons.requests.GetListRequestFriendRequest;
import org.nmjava.chatapp.commons.requests.RejectRequestFriendRequest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;

public class ListReqAddFriendController implements Initializable {
    public static Collection<Friend> listReqAddFriend;
    @FXML
    private TextField labelName;
    @FXML
    public static ScrollPane reqlistContainer;
    @FXML
    private Button sendRequestAddFriendBtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listReqAddFriend = new ArrayList<>();
//        this.reqlistContainer.setContent(createContactMessageList());
        sendRequestAddFriendBtn.setOnMouseClicked(e->{
            Main.socketClient.addRequestToQueue(AddFriendRequest.builder().user(Main.UserName).friend(labelName.getText()).build());
        });
    }
    public static VBox createContactMessageList() {
        VBox contactMessageList = new ContactMessageList();
        if(Objects.isNull(listReqAddFriend)) return contactMessageList;
        for(Friend friend : listReqAddFriend){
            contactMessageList.getChildren().add(new ReqAddFriendCard(friend.getUsername()));
        }
        return contactMessageList;
    }
}
