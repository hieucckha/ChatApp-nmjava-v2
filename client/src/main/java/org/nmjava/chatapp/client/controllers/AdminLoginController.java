package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.client.models.modelGroupID;
import org.nmjava.chatapp.client.models.modelLoginList;
import org.nmjava.chatapp.client.utils.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.EventListener;
import java.util.ResourceBundle;
import java.util.Vector;

public class AdminLoginController implements Initializable {

    @FXML
    private Button listUserBtn;
    @FXML
    private  Button listLoginBtn;
    @FXML
    private  Button listGroupBtn;
    @FXML
    private TableColumn <modelLoginList,String> tableLoginUserName;
    @FXML
    private TableColumn <modelLoginList,String> tableLoginName;
    @FXML
    private TableView <modelLoginList> tableView;
    @FXML
    private TextArea textAreaLoginTimes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableLoginUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tableLoginName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.setItems(list);
    }
    @FXML
    public void onClickCollumn(MouseEvent e)
    {
        TablePosition tablePosition = tableView.getSelectionModel().getSelectedCells().get(0);
        int row =tablePosition.getRow();
        modelLoginList timeList=tableView.getItems().get(row);
//        System.out.println(timeList.getTimes());
        textAreaLoginTimes.setText(timeList.getTimes());


    }

    private ObservableList<modelLoginList> list = FXCollections.observableArrayList(

            new modelLoginList("nguyenhau","Chicken game","ngày 1\nngày 2\nngày 3\n"),
            new modelLoginList("nguyehn hung","Chicken game","ngày 1\nngày 2\n ")

    );
    @FXML
    protected  void handleBtn ( ActionEvent actionEvent)
    {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (actionEvent.getSource() == listUserBtn ){
            listUserClick(stage);
        } else if (actionEvent.getSource() == listGroupBtn) {
            listGroupClick(stage);
        }
        else if (actionEvent.getSource()==listLoginBtn)
        {
            listLoginClick(stage);
        }
    }

    private void listUserClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("AdminHome"));
        stage.show();
    }


    private void listGroupClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("AdminGroup"));
        stage.show();
    }

    private void listLoginClick(Stage stage) {
        stage.setScene(SceneController.staticGetScene("AdminLogin"));
        stage.show();
    }
}
