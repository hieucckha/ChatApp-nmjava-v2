package org.nmjava.chatapp.client.components;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactMessageList extends VBox implements Initializable {
    public ContactMessageList() {
        loadFXML();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void loadFXML() {
        URL url = Avatar.class.getResource("ContactMessageList.fxml");

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
