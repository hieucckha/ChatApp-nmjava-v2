package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.client.utils.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {

    public TextField fdEmail;
    public TextField fdPassword;
    public TextField fdComfierPw;
    public Button btnSignup;
    public Button btnLogin;

    @FXML
    protected void handleButtonClicks(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (actionEvent.getSource() == btnSignup) {
            onSignupBtnClick(stage);
        } else if (actionEvent.getSource() == btnLogin) {
            onLoginBtnClick(stage);
        }
    }

    private void onSignupBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("Login"));
        stage.show();
    }

    private void onLoginBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("Login"));
        stage.show();
    }
}
