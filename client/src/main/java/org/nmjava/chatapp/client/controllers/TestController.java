package org.nmjava.chatapp.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.LightBase;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {
    @FXML
    private VBox root;
    @FXML
    private Button btn;
    @FXML
    private Label lb;

    public TestController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn.setOnAction(e -> {
            System.out.println(root.getWidth());
            System.out.println(root.getHeight());
            lb.setText("Somethign ajsdl;fkj;alskdfjlkasdjfl;kasdjf;lkasdjfkl;asdjfdl;kasj");
        });
    }
}
