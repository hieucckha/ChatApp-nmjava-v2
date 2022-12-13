package org.nmjava.chatapp.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import org.nmjava.chatapp.client.utils.SceneController;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField fdPassword;
    public TextField fdEmail;
    public Button btnLogin;
    public Button btnForgotPw;
    public Button btnSignup;

    @FXML
    protected void handleButtonClicks(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (actionEvent.getSource() == btnLogin) {
            onLoginBtnClick(stage);
        } else if (actionEvent.getSource() == btnSignup) {
            onSignupBtnClick(stage);
        } else if (actionEvent.getSource() == btnForgotPw) {
            onForgotPwBtnClick(stage);
        }
    }

    private void onLoginBtnClick(Stage stage) {
//        stage.setScene(SceneController.staticGetScene(""));
    }

    private void onSignupBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("Signup"));
        stage.show();
    }
    private void onForgotPwBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("ForgotPw"));
        stage.show();
    }
}
