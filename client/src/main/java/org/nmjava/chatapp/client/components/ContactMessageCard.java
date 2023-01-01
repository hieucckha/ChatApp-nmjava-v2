package org.nmjava.chatapp.client.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.controllers.UserHomeController;
import org.nmjava.chatapp.client.utils.SceneController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactMessageCard extends HBox implements Initializable {
    private static final int IS_ONLINE = -1;

    @FXML
    private VBox infoContainer;
    private Avatar avatar;
    @FXML
    private Label userNameLb;
    @FXML
    private Label lastOnlineLb;
    @FXML
    private Label lastMessageLb;

    public ContactMessageCard(String userName, int lastOnlineHour, String lastMessage) {
        loadFXML();

        this.setWidth(300);
        this.setHeight(100);

        this.avatar = new Avatar(50, 50);
        this.getChildren().add(0, avatar);

        setUserName(userName);
        setLastOnlineHour(lastOnlineHour);
        setLastMessage(lastMessage);
    }

    public void loadFXML() {
        URL url = Avatar.class.getResource("ContactMessageCard.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

            this.infoContainer.prefWidthProperty().bind(this.maxWidthProperty());
            this.infoContainer.prefHeightProperty().bind(this.maxHeightProperty());
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

    public void setLastOnlineHour(int lastOnlineHour) {
        String status = lastOnlineHour == IS_ONLINE ? "Online" : (lastOnlineHour + " hours ago");
        this.lastOnlineLb.setText(status);
    }

    public void setLastMessage(String message) {
        this.lastMessageLb.setText(message);
    }

}
