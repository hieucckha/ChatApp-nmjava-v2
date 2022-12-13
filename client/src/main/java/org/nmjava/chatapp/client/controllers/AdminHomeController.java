package org.nmjava.chatapp.client.controllers;

import org.nmjava.chatapp.client.daos.UserDao;
import org.nmjava.chatapp.client.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import org.nmjava.chatapp.client.utils.SceneController;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.FormatStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class AdminHomeController implements Initializable {

    @FXML
    private Button listUserBtn;
    @FXML
    private Button listLoginBtn;
    @FXML
    private Button listGroupBtn;
    @FXML
    private VBox addUser;
    @FXML
    private VBox filterUser;
    @FXML
    private Button cancelAddBtn;
    @FXML
    private Button addBtn;
    @FXML
    private BorderPane borderPanelSub;
    @FXML
    private BorderPane borderPanelSub1;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField dobTextField;
    @FXML
    private TextField sexTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> userNameTable;
    @FXML

    private TableColumn<User, String> nameTable;
    @FXML

    private TableColumn<User, String> addressTable;
    @FXML

    private TableColumn<User, LocalDate> dobTable;
    @FXML

    private TableColumn<User, String> sexTable;
    @FXML

    private TableColumn<User, String> emailTable;
    @FXML
    private TextField filterUserName;


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


    @FXML
    public void cancelAddButtonOnAction(ActionEvent event) {

        tableView.toFront();

        addUser.setVisible(false);
        System.out.println("false");

    }

    public void addButtonOnAction(ActionEvent event) {
        borderPanelSub.toFront();
        addUser.setVisible(true);
//        stateAdd.set(true);
        System.out.println("true");
    }

    public void cancelFilterButtonOnAction(ActionEvent event) {
        tableView.toFront();
        filterUser.setVisible(false);

        System.out.println("false");

    }

    public void filterButtonOnAction(ActionEvent event) {
        borderPanelSub1.toFront();
        filterUser.setVisible(true);
//        stateFilter.set(true);
        System.out.println("true");

    }

    public void AddDataOnAction(ActionEvent e) throws SQLException, ParseException {
        UserDao userDao = new UserDao();
        User user = new User();
        if (userNameTextField.getText().isBlank() && nameTextField.getText().isBlank() && dobTextField.getText().isBlank() &&
                sexTextField.getText().isBlank() && addressTextField.getText().isBlank() && emailTextField.getText().isBlank()) {

            Alert fail = new Alert(Alert.AlertType.INFORMATION);
            fail.setHeaderText("failure");
            fail.setContentText("you haven't typed something");
            fail.showAndWait();

        } else {

            user.setUsername(userNameTextField.getText());
            user.setFullName(nameTextField.getText());
            user.setDateOfBirth(LocalDate.parse(dobTextField.getText()));
            user.setPassword("1234");
            user.setAddress(addressTextField.getText());
            user.setEmail(emailTextField.getText());
            user.setActivated(true);
            user.setCreateAt(LocalDateTime.now());
            user.setGender(sexTextField.getText());



            userDao.save(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Account succesfully created!");
            alert.showAndWait();
        }

        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(new UserDao().getInfoAll()));

    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("true");

        filterUser.setVisible(false);
        userNameTable.setCellValueFactory(new PropertyValueFactory<>("username"));
        // save to db when edit in table view

        nameTable.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameTable.setCellFactory(TextFieldTableCell.forTableColumn());
        nameTable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> event) {
                User user =event.getRowValue();
                user.setFullName(event.getNewValue());
                System.out.println(user.getUsername());

                UserDao userDao =new UserDao();
                userDao.update(user);

            }
        });

        addressTable.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressTable.setCellFactory(TextFieldTableCell.forTableColumn());
        addressTable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> event) {
                User user =event.getRowValue();
                user.setAddress(event.getNewValue());
                UserDao userDao =new UserDao();
                userDao.update(user);

            }
        });

        dobTable.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dobTable.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dobTable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, LocalDate>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, LocalDate> event) {
                User user =event.getRowValue();
                user.setDateOfBirth(event.getNewValue());
                UserDao userDao =new UserDao();
                userDao.update(user);

            }
        });

        sexTable.setCellValueFactory(new PropertyValueFactory<>("gender"));
        sexTable.setCellFactory(TextFieldTableCell.forTableColumn());
        sexTable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> event) {
                User user =event.getRowValue();
                user.setGender(event.getNewValue());
                UserDao userDao =new UserDao();
                userDao.update(user);

            }
        });

        emailTable.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailTable.setCellFactory(TextFieldTableCell.forTableColumn());
        emailTable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> event) {
                User user =event.getRowValue();
                user.setEmail(event.getNewValue());
                UserDao userDao =new UserDao();
                userDao.update(user);

            }
        });
        //filtering


        tableView.toFront();
       tableView.setItems(observableList);
       tableView.setEditable(true);
//        tableView.setItems(observableList);


    }


    ObservableList<User> observableList = FXCollections.observableArrayList(new UserDao().getInfoAll());
}
