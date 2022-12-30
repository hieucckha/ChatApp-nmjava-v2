package org.nmjava.chatapp.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.networks.ThreadRespone;
import org.nmjava.chatapp.client.utils.SceneController;
import javafx.stage.Stage;
import org.nmjava.chatapp.commons.requests.AuthenticationRequest;
import org.nmjava.chatapp.commons.requests.GetListConservationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField fdPassword;
    public TextField fdEmail;
    public Button btnLogin;
    public Button btnForgotPw;
    public Button btnSignup;

    @FXML
    protected void handleButtonClicks(ActionEvent actionEvent) {
        Main.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Main.stage.setTitle("Login");

        if (actionEvent.getSource() == btnLogin) {
            onLoginBtnClick( Main.stage);
        } else if (actionEvent.getSource() == btnSignup) {
            onSignupBtnClick( Main.stage);
        } else if (actionEvent.getSource() == btnForgotPw) {
            onForgotPwBtnClick( Main.stage);
        }
    }

    private void onLoginBtnClick(Stage stage) {
//        stage.setScene(SceneController.staticGetScene(""));
        // Gửi request Xác thực
        Main.UserName = fdEmail.getText();
        Main.socketClient.addRequestToQueue(GetListConservationRequest.builder().username(Main.UserName).build());
        Main.socketClient.addRequestToQueue(AuthenticationRequest.builder().username(fdEmail.getText()).password(fdPassword.getText()).build());
    }

    private void onSignupBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("Signup"));
        stage.show();
    }
    private void onForgotPwBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("ForgotPw"));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ThreadRespone loginThrd = new ThreadRespone("Login");
        // Cái luồng này chạy bình thường

    }
}
