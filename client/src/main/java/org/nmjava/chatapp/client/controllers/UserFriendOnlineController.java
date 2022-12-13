package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.client.components.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class UserFriendOnlineController implements Initializable {
    @FXML
    public GridPane utilsContainer;
    @FXML
    public HBox titleChatContainer;

    @FXML
    public GridPane listInfoContainer;
    @FXML
    public GridPane chatContainer;
    @FXML
    private ScrollPane chatListSP;
    @FXML
    private VBox chatList;
    @FXML
    private Label nameContacting;
    @FXML
    private Label statusContacting;

    public UserFriendOnlineController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserTitleChat utc = new UserTitleChat("Something is the long long long long long long long long long long text", 10);
        this.titleChatContainer.getChildren().add(utc);

        chatListSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatListSP.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        for (int i = 0; i < 20; ++i) {
            chatList.getChildren().add(new FriendOnlineCard("Nguyen Hieu"));
        }
    }
}
