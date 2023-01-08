package org.nmjava.chatapp.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.components.Friend;
import org.nmjava.chatapp.client.components.FriendCard;
import org.nmjava.chatapp.client.components.FriendOnlineList;
import org.nmjava.chatapp.client.components.ReqAddFriendCard;
import org.nmjava.chatapp.client.networks.ThreadRespone;
import org.nmjava.chatapp.commons.requests.AddFriendRequest;
import org.nmjava.chatapp.commons.requests.CheckUserExistRequest;
import org.nmjava.chatapp.commons.requests.CreateGroupChatRequest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class CreateGroupChatController implements Initializable {
    @FXML
    private Button checkExisUser;

    @FXML
    private Button getcurrentpersoncreategroup;
    @FXML
    private TextField labelName;

    @FXML
    private ScrollPane listMemberContainer;
    @FXML
    private Button sendCreateGroup;

    public static Collection<String> listMember;

    public CreateGroupChatController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listMember = new ArrayList<>();
        ThreadRespone.listMemberContainer = listMemberContainer;

        checkExisUser.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(CheckUserExistRequest.builder().username(labelName.getText()).build());
        });
        getcurrentpersoncreategroup.setOnMouseClicked(e -> {
            VBox MemberList = new FriendOnlineList();
            MemberList.getChildren().add(new Label("List Member"));
            for (String member : listMember) {
                MemberList.getChildren().add(new FriendCard(member));
            }
            listMemberContainer.setContent(MemberList);
        });
        sendCreateGroup.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(CreateGroupChatRequest.builder().creator(Main.UserName).members(listMember.stream().toList()).build());
        });
    }


}
