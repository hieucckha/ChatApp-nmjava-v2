package org.nmjava.chatapp.client.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.components.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.nmjava.chatapp.client.networks.ThreadRespone;
import org.nmjava.chatapp.client.utils.SceneController;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.requests.GetListRequestFriendRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserHomeController implements Initializable {
    @FXML
    public GridPane utilsContainer;

    @FXML
    public HBox titleChatContainer;

    @FXML
    public GridPane listInfoContainer;
    @FXML
    private Button contactBtn;
    @FXML
    private Button friendOnlineBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button friendBtn;
    @FXML
    private ScrollPane spContainer;
    @FXML
    public GridPane chatContainer;
    @FXML
    private Button addFrdBtn;

    private UserTitleChat utc;
    public static Collection<Friend> listReqAddFriend;

    public UserHomeController() {
        this.utc = new UserTitleChat("", 0);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle agr1) {
        listReqAddFriend = new ArrayList<>();
        ThreadRespone UserHomeThrd = new ThreadRespone("UserHome");
        this.titleChatContainer.getChildren().add(this.utc);

        spContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.spContainer.setContent(createContactMessageList());

        contactBtn.setOnMouseClicked(e -> {
            System.out.println("contactBtn is clicks");
            this.spContainer.setContent(createContactMessageList());
        });
        friendOnlineBtn.setOnMouseClicked(e -> {
            System.out.println("friendOnlineBtn is clicks");
            this.spContainer.setContent(createFriendOnlineList());
        });
        logoutBtn.setOnMouseClicked(e -> {
            Main.stage.setTitle("Login");
            Main.stage.setScene(SceneController.staticGetScene("Login"));
            Main.stage.show();
        });
        addFrdBtn.setOnMouseClicked(e -> {
            System.out.println(Main.UserName);
            Main.socketClient.addRequestToQueue(GetListRequestFriendRequest.builder().username(Main.UserName).build());
            Stage newStage = new Stage();
            newStage.setScene(SceneController.staticGetScene("ListAddFriendReq"));
            newStage.show();
        });
    }

    private VBox createContactMessageList() {
        VBox contactMessageList = new ContactMessageList();

        for (int i = 0; i < 20; ++i) {
            contactMessageList.getChildren().add(new ContactMessageCard("Nguyen Hieu", 10, "Day la tin nhan cuoi cung"));
        }

        return contactMessageList;
    }

    private VBox createFriendOnlineList() {
        VBox friendOnlineList = new FriendOnlineList();

        for (int i = 0; i < 20; ++i) {
            friendOnlineList.getChildren().add(new FriendOnlineCard("Nguyen Hieu"));
        }

        return friendOnlineList;
    }
}
