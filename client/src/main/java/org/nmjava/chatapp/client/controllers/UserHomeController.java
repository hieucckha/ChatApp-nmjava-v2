package org.nmjava.chatapp.client.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import org.nmjava.chatapp.commons.requests.*;

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
    private ScrollPane conservationContainer;
    @FXML
    private Button addFrdBtn;

    @FXML
    private Button sendMsgBtn;
    @FXML
    private TextField jtfmsg;

    @FXML
    private Button createGroupbtn;
    @FXML
    private TextField searchAll;
    @FXML
    private HBox settingConservation;
    @FXML
    private Button searchAllBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private TextField search;
    public static UserTitleChat utc;
    public static String conservationID;

    public static Collection<Friend> listReqAddFriend;

    public UserHomeController() {
        this.utc = new UserTitleChat("", 0);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle agr1) {
        jtfmsg.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    System.out.println(conservationID);
                    System.out.println(jtfmsg.getText());
                    if(!jtfmsg.getText().isEmpty() && !utc.getUserName().isEmpty()&& !utc.getUserName().equals("") )
                        Main.socketClient.addRequestToQueue(SendMessageRequest.builder().username(Main.UserName).conservationID(UserHomeController.conservationID).message(jtfmsg.getText()).build());
                }
            }
        });
        listReqAddFriend = new ArrayList<>();
        ThreadRespone UserHomeThrd = new ThreadRespone("UserHome");
        this.titleChatContainer.getChildren().add(0,this.utc);

        Button setting = new Button("Setting Conservation");
        setting.setOnMouseClicked(e->{
            System.out.println(conservationID);
            if(!Objects.isNull(conservationID) &&!conservationID.isEmpty()&& !conservationID.isBlank())
                Main.socketClient.addRequestToQueue(GetListMemberConservationRequest.builder().conservationID(conservationID).build());
            Stage newStage = new Stage();
            newStage.setTitle("List Member");
            newStage.setScene(SceneController.staticGetScene("SettingConservation"));
            newStage.show();
        });
        settingConservation.getChildren().add(0,setting);
        spContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ThreadRespone.spContainer = spContainer;
        ThreadRespone.conservationContainer =conservationContainer;
        sendMsgBtn.setOnMouseClicked(e -> {
            System.out.println(conservationID);
            System.out.println(jtfmsg.getText());
            if(!jtfmsg.getText().isEmpty() && !utc.getUserName().isEmpty()&& !utc.getUserName().equals("") )
                Main.socketClient.addRequestToQueue(SendMessageRequest.builder().username(Main.UserName).conservationID(UserHomeController.conservationID).message(jtfmsg.getText()).build());
        });

        contactBtn.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(GetListConservationRequest.builder().username(Main.UserName).build());
        });
        friendOnlineBtn.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(GetListFriendOnlineRequest.builder().username(Main.UserName).build());
        });
        logoutBtn.setOnMouseClicked(e -> {
            conservationContainer.setContent(new VBox());
            Main.stage.setTitle("Login");
            Main.stage.setScene(SceneController.staticGetScene("Login"));
            Main.stage.show();
        });
        addFrdBtn.setOnMouseClicked(e -> {
            System.out.println(Main.UserName);
            Main.socketClient.addRequestToQueue(GetListRequestFriendRequest.builder().username(Main.UserName).build());
            Stage newStage = new Stage();
            newStage.setTitle("List Request add Friend");
            newStage.setScene(SceneController.staticGetScene("ListAddFriendReq"));
            newStage.show();
        });
        createGroupbtn.setOnMouseClicked(e -> {
            System.out.println(Main.UserName);
            Stage newStage = new Stage();
            newStage.setOnHidden(event->{
                CreateGroupChatController.listMember = new ArrayList<>();
                ThreadRespone.listMemberContainer.setContent(new VBox());
            });
            newStage.setTitle("Create Group");
            newStage.setScene(SceneController.staticGetScene("CreateGroup"));
            newStage.show();
        });
        friendBtn.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(GetListFriendRequest.builder().username(Main.UserName).build());
        });
        searchAllBtn.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(SearchMessageAllRequest.builder().text(searchAll.getText()).username(Main.UserName).build());
        });
        searchBtn.setOnMouseClicked(e -> {
            if(!Objects.isNull(conservationID) &&!conservationID.isEmpty()&& !conservationID.isBlank())
                Main.socketClient.addRequestToQueue(SearchMessageConservationRequest.builder().conservationID(conservationID).text(search.getText()).build());
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
