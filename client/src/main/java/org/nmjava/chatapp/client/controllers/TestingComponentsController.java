package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.client.components.ContactMessageCard;
import org.nmjava.chatapp.client.components.UserTitleChat;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TestingComponentsController implements Initializable {

    @FXML
    private VBox vboxRoot;
    @FXML
    private Button addBtn;

    public TestingComponentsController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("addBtn is clicked");
//                ContactMessageCard cmc = new ContactMessageCard(300, "Hieu Nguyen", 10, "Day la loi nhan cuoi cung");
//                UserTitleChat utc = new UserTitleChat(50, "Hieu Nguyen", 10);
//                vboxRoot.getChildren().add(cmc);
            }
        });
    }
}
