package org.nmjava.chatapp.client.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.utils.SceneController;
import org.nmjava.chatapp.commons.requests.AcceptRequestFriendRequest;
import org.nmjava.chatapp.commons.requests.AuthenticationRequest;
import org.nmjava.chatapp.commons.requests.RejectRequestFriendRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReqAddFriendCard extends HBox implements Initializable {
    private Avatar avatar;
    @FXML
    private Button acceptBtn;

    @FXML
    private Button declineBtn;

    @FXML
    private Label lbName;


    public ReqAddFriendCard(String userName) {
        loadFXML();

        this.setWidth(300);
        this.setHeight(100);

        this.avatar = new Avatar(50, 50);
        this.getChildren().add(0, avatar);

        setUserName(userName);
        acceptBtn.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(AcceptRequestFriendRequest.builder().user(Main.UserName).friend(lbName.getText()).build());
        });
        declineBtn.setOnMouseClicked(e -> {
            Main.socketClient.addRequestToQueue(RejectRequestFriendRequest.builder().user(Main.UserName).friend(lbName.getText()).build());
        });
    }
    public void loadFXML() {
        URL url = Avatar.class.getResource("ReqAddFriendCard.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void setUserName(String userName) {
        this.lbName.setText(userName);
    }
}
