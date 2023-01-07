package org.nmjava.chatapp.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.components.*;
import org.nmjava.chatapp.client.networks.ThreadRespone;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.requests.AddFriendRequest;
import org.nmjava.chatapp.commons.requests.GetListRequestFriendRequest;
import org.nmjava.chatapp.commons.requests.RejectRequestFriendRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;

public class ListReqAddFriendController implements Initializable {
    @FXML
    private TextField labelName;
    @FXML
    public ScrollPane reqlistContainer;
    @FXML
    private Button sendRequestAddFriendBtn;

    public ListReqAddFriendController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ThreadRespone.reqlistContainer = reqlistContainer;

        sendRequestAddFriendBtn.setOnMouseClicked(e->{
            if(!Main.UserName.equals(labelName.getText()))
                Main.socketClient.addRequestToQueue(AddFriendRequest.builder().user(Main.UserName).friend(labelName.getText()).build());
        });
    }



}
