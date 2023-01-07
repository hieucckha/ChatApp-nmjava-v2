package org.nmjava.chatapp.client.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
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

    @FXML
    private TextField adresstf;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private TextField fdComfierPw;

    @FXML
    private TextField fdEmail;

    @FXML
    private TextField fdPassword;

    @FXML
    private TextField fullnametf;

    @FXML
    private TextField gender;

    @FXML
    private TextField username;
    @FXML
    private DatePicker dobpicker;


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
            System.out.println(fdPassword.getText());
            Main.socketClient.addRequestToQueue(CreateAccountRequest.builder().fullName(fullnametf.getText()).email(fdEmail.getText()).address(adresstf.getText()).dateOfBirth(dobpicker.getValue()).gender(gender.getText()).username(username.getText()).password(fdPassword.getText()).build());
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
