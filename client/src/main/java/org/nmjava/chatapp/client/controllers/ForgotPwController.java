package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.client.Main;
import org.nmjava.chatapp.client.utils.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.nmjava.chatapp.commons.requests.ForgotPasswordRequest;
import org.nmjava.chatapp.commons.requests.GetListConservationRequest;

public class ForgotPwController {
    public TextField fdEmail;
    public Button btnResetPw;
    public Button btnCancel;

    @FXML
    protected void handleButtonClicks(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (actionEvent.getSource() == btnResetPw) {
            onResetPwBtnClick(stage);
        } else if (actionEvent.getSource() == btnCancel) {
            onCancelBtnClick(stage);
        }
    }

    private void onResetPwBtnClick(Stage stage) {
        Main.socketClient.addRequestToQueue(ForgotPasswordRequest.builder().email(fdEmail.getText()).build());
    }

    private void onCancelBtnClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("Login"));
        stage.show();
    }
}
