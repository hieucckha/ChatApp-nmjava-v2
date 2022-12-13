package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.client.components.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
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
    private Button friendBtn;
    @FXML
    private ScrollPane spContainer;
    @FXML
    public GridPane chatContainer;

    private UserTitleChat utc;

    public UserHomeController() {
        this.utc = new UserTitleChat("", 0);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle agr1) {
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
