package org.nmjava.chatapp.client.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.utils.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.nmjava.chatapp.commons.requests.CreateAccountRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfUsername;

    @FXML
    private TextField fdPassword;
    @FXML
    private TextField fdComfierPw;
    @FXML
    private TextField tfFullName;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private TextField tfAddress;
    @FXML
    private DatePicker pickerDob;

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSignup;

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
        if (fdPassword.getText().equals(fdComfierPw.getText())) {
            System.out.println(fdPassword.getText());
            Main.socketClient.addRequestToQueue(CreateAccountRequest.builder().fullName(tfFullName.getText()).email(tfEmail.getText()).address(tfAddress.getText()).dateOfBirth(pickerDob.getValue()).gender(cbGender.getValue()).username(tfUsername.getText()).password(fdPassword.getText()).build());
        } else {
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
        cbGender.getItems().clear();
        cbGender.getItems().addAll("Male", "Female");
        cbGender.getSelectionModel().selectFirst();
    }
}
