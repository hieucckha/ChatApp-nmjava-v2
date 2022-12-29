package org.nmjava.chatapp.client.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.networks.ThreadRespone;
import org.nmjava.chatapp.client.utils.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.nmjava.chatapp.commons.requests.AuthenticationRequest;
import org.nmjava.chatapp.commons.requests.CreateAccountRequest;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    public TextField fdEmail;
    public TextField fdPassword;
    public TextField fdComfierPw;
    public Button btnSignup;
    public Button btnLogin;

    public static Stage stage;
    @FXML
    protected void handleButtonClicks(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (actionEvent.getSource() == btnSignup) {
            onSignupBtnClick(stage);
        } else if (actionEvent.getSource() == btnLogin) {
            onLoginBtnClick(stage);
        }
    }

    private void onSignupBtnClick(Stage stage) {
        if (fdPassword.getText().equals(fdComfierPw.getText())){
            Main.socketClient.addRequestToQueue(CreateAccountRequest.builder().fullName(fdEmail.getText()).email("").address("").dateOfBirth(LocalDate.now()).gender("").username(fdEmail.getText()).password(fdPassword.getText()).build());
        }
        else {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Password and Comfirm Password is different");
            a.show();
        }

    }

    private void onLoginBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("Login"));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ThreadRespone loginThrd = new ThreadRespone("Signup");
    }
}
