package org.nmjava.chatapp.client.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FriendOnlineCard extends HBox implements Initializable {
    private Avatar avatar;
    @FXML
    private Label userNameLb;

    public FriendOnlineCard(String userName) {
        loadFXML();

        this.setWidth(300);
        this.setHeight(100);

        this.avatar = new Avatar(50, 50);
        this.getChildren().add(0, avatar);

        setUserName(userName);

    }

    public void loadFXML() {
        URL url = Avatar.class.getResource("ContactMessageCard.fxml");

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

    public void setAvatarImage(String path) {
        avatar.setImage(path);
    }

    public void setAvatarImage(Image image) {
        avatar.setImage(image);
    }

    public void setUserName(String userName) {
        this.userNameLb.setText(userName);
    }
}
