package org.nmjava.chatapp.client.components;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FriendOnlineList extends VBox implements Initializable {
    public FriendOnlineList() {
        loadFXML();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void loadFXML() {
        URL url = Avatar.class.getResource("FriendOnlineList.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }
    }
}
