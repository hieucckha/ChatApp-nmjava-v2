package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.commons.daos.FriendDao;
import org.nmjava.chatapp.commons.daos.ListLogDao;
import org.nmjava.chatapp.commons.daos.UserDao;
import org.nmjava.chatapp.commons.models.Friend;
import org.nmjava.chatapp.commons.models.modelLoginList;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminLoginController implements Initializable {
    @FXML
    private Button listUserBtn;
    @FXML
    private  Button listLoginBtn;
    @FXML
    private  Button listGroupBtn;
    @FXML
    private TableColumn <modelLoginList, String> tableLoginUserName;
    @FXML
    private TableColumn <modelLoginList, String> tableLoginName;
    @FXML
    private TableColumn <modelLoginList,String> tableCreateAt;
    @FXML
    private TableView <modelLoginList> tableView;
    @FXML
    private TextArea textAreaLoginTimes;
    @FXML
    private TextArea textAreaFriendList;


    @FXML
    public void onClickItem()
    {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            //login list
            modelLoginList selected = tableView.getSelectionModel().getSelectedItem();
            ArrayList<modelLoginList> log_list = (ArrayList<modelLoginList>) new ListLogDao().getListLog(selected.getUserName());
            StringBuilder line = new StringBuilder();
            for( modelLoginList list : log_list)
            {
                line.append(list.getTimes()+"\n");
            }
            textAreaLoginTimes.setText(String.valueOf(line));

            //friend list
            StringBuilder friendline = new StringBuilder();
            ArrayList<Friend> friends= (ArrayList<Friend>) new FriendDao().getListFriend(selected.getUserName());
            for(Friend friend: friends)
            {
                friendline.append(friend.getUsername()+"\n");
            }
            textAreaFriendList.setText(String.valueOf(friendline));

        }
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(new ListLogDao().getInfoAll()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableLoginUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tableLoginName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCreateAt.setCellValueFactory(new PropertyValueFactory<>("times"));
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                onClickItem();
            }
        });
        tableView.setItems(list);
    }

    private ObservableList<modelLoginList> list = FXCollections.observableArrayList(new ListLogDao().getInfoAll());

    @FXML
    protected void handleBtn(ActionEvent actionEvent)
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
