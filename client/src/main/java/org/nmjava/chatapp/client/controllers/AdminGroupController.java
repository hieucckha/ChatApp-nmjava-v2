package org.nmjava.chatapp.client.controllers;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.nmjava.chatapp.client.utils.SceneController;
import org.nmjava.chatapp.commons.daos.ConservationDao;
import org.nmjava.chatapp.commons.daos.ListLogDao;
import org.nmjava.chatapp.commons.daos.UserDao;
import org.nmjava.chatapp.commons.models.Conservation;
import org.nmjava.chatapp.commons.models.modelGroupID;
import org.nmjava.chatapp.commons.models.modelLoginList;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminGroupController implements Initializable {
    @FXML
    private Button listUserBtn;
    @FXML
    private Button listLoginBtn;
    @FXML
    private Button listGroupBtn;
    @FXML
    private TableColumn<Conservation, String> tableGroupID;
    @FXML
    public TableColumn<Conservation, String> tableGroupName;
    @FXML
    private TableColumn<Conservation, String> tableUserName;
    @FXML
    private TableColumn<Conservation, String> tableRole;
    @FXML
    private TableView<Conservation> tableView;

    @FXML
    public void onClickItem()
    {
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(new ConservationDao().getListGroup()));
    }
    @Override
    public void initialize(URL arg0, ResourceBundle agr1) {
        System.out.println("true");
        tableGroupID.setCellValueFactory(new PropertyValueFactory<>("conservationID"));
        tableGroupID.setCellFactory(TextFieldTableCell.forTableColumn());

        tableGroupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableGroupName.setCellFactory(TextFieldTableCell.forTableColumn());

        tableUserName.setCellValueFactory(new PropertyValueFactory<>("full_name"));
        tableUserName.setCellFactory(TextFieldTableCell.forTableColumn());

//        tableRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                onClickItem();
            }
        });
        tableRole.setCellValueFactory(cellData -> {
            Integer role = cellData.getValue().getRole();
            String activeAsString;
            if (role == 0) {
                activeAsString = "Creator group";
            } else if(role==1){
                activeAsString = "Admin";
            }
            else {
                activeAsString="member";
            }

            ReadOnlyStringWrapper readOnlyStringWrapper = new ReadOnlyStringWrapper(activeAsString);
            return readOnlyStringWrapper;
        });


        tableView.setItems(list);
    }

    private ObservableList<Conservation> list = FXCollections.observableArrayList(new ConservationDao().getListGroup());


    @FXML
    protected void handleBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (actionEvent.getSource() == listUserBtn) {
            listUserClick(stage);
        } else if (actionEvent.getSource() == listGroupBtn) {
            listGroupClick(stage);
        } else if (actionEvent.getSource() == listLoginBtn) {
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

