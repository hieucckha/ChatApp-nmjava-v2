package org.nmjava.chatapp.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.components.FriendCard;
import org.nmjava.chatapp.client.components.FriendOnlineList;
import org.nmjava.chatapp.client.networks.ThreadRespone;
import org.nmjava.chatapp.commons.requests.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class SettingConservationController implements Initializable {
    @FXML
    private Button AddMemBtn;

    @FXML
    private Button AssignAdminBtn;

    @FXML
    private Button DeleteUserBtn;

    @FXML
    private TextField TextInput;

    @FXML
    private Button changeNameBtn;

    @FXML
    private ScrollPane listMemberGroupContainer;

    public static Collection<String> listMember;

    public SettingConservationController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listMember = new ArrayList<>();
        ThreadRespone.listMemberGroupContainer = listMemberGroupContainer;

        AddMemBtn.setOnMouseClicked(e -> {
            if(!TextInput.getText().isEmpty() && !UserHomeController.utc.getUserName().isEmpty()&& !UserHomeController.utc.getUserName().equals("")&& !UserHomeController.conservationID.isEmpty())
                Main.socketClient.addRequestToQueue(AddMemberGroupChatRequest.builder().adder(Main.UserName).conservationID(UserHomeController.conservationID).member(TextInput.getText()).build());
        });
        DeleteUserBtn.setOnMouseClicked(e -> {
            if(!TextInput.getText().isEmpty() && !UserHomeController.utc.getUserName().isEmpty()&& !UserHomeController.utc.getUserName().equals("")&& !UserHomeController.conservationID.isEmpty())
                Main.socketClient.addRequestToQueue(RemoveUserGroupChatRequest.builder().admin(Main.UserName).conservationID(UserHomeController.conservationID).member(TextInput.getText()).build());
        });
        AssignAdminBtn.setOnMouseClicked(e -> {
            if(!TextInput.getText().isEmpty() && !UserHomeController.utc.getUserName().isEmpty()&& !UserHomeController.utc.getUserName().equals("")&& !UserHomeController.conservationID.isEmpty())
                Main.socketClient.addRequestToQueue(GiveAdminUserGroupChat.builder().admin(Main.UserName).conservationID(UserHomeController.conservationID).member(TextInput.getText()).build());
        });
        changeNameBtn.setOnMouseClicked(e -> {
            if(!TextInput.getText().isEmpty() && !UserHomeController.utc.getUserName().isEmpty()&& !UserHomeController.utc.getUserName().equals("")&& !UserHomeController.conservationID.isEmpty())
                Main.socketClient.addRequestToQueue(RenameGroupChatRequest.builder().username(Main.UserName).newName(TextInput.getText()).conservationID(UserHomeController.conservationID).build());
        });

    }


}
