package org.nmjava.chatapp.client.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import org.nmjava.chatapp.client.utils.SceneController;
import org.nmjava.chatapp.commons.daos.UserDao;
import org.nmjava.chatapp.commons.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

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
    private CheckBox selector;
    @FXML
    private TableColumn<User, String> activeColumn;
    @FXML
    private Button resetBtn;


    ObservableList<User> observableList = FXCollections.observableArrayList(new UserDao().getInfoAll());

//    TableRow<User > row = new TableRow<>();

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

    @FXML
    public void clickItem() {

        UserDao userDao = new UserDao();
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user.getActivated() == true) {
            user.setActivated(false);
        } else {
            user.setActivated(true);
        }
        userDao.update(user);


        tableView.setItems(FXCollections.observableArrayList(new UserDao().getInfoAll()));
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
            user.setOnline(true);


            userDao.save(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Account succesfully created!");
            alert.showAndWait();
        }

//        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(new UserDao().getInfoAll()));
    }


    @FXML
    private void resetUserInTable(ActionEvent event) {
        System.out.println(tableView.getSelectionModel().getSelectedItem());
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user == null) {
            Alert fail = new Alert(Alert.AlertType.INFORMATION);
            fail.setHeaderText("failure");
            fail.setContentText("you must choose account");
            fail.showAndWait();
        } else {
            user.setPassword("1234");

            UserDao userDao = new UserDao();
            userDao.resetPassword(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Account succesfully created!");
            alert.showAndWait();
        }


    }

    @FXML
    private void deleteUserInTable(ActionEvent event) {

        User user = tableView.getSelectionModel().getSelectedItem();
        if (user == null) {
            Alert fail = new Alert(Alert.AlertType.INFORMATION);
            fail.setHeaderText("failure");
            fail.setContentText("you must choose account");
            fail.showAndWait();
        } else {

            UserDao userDao = new UserDao();
            userDao.delete(user.getUsername());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Account succesfully created!");
            alert.showAndWait();
        }

//        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(new UserDao().getInfoAll()));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.setEditable(true);


        userNameTable.setCellValueFactory(new PropertyValueFactory<>("username"));
        // save to db when edit in table view

        // Name
        nameTable.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameTable.setCellFactory(TextFieldTableCell.forTableColumn());
        nameTable.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setFullName(event.getNewValue());
            System.out.println(event.getRowValue());

            UserDao userDao = new UserDao();
            userDao.update(user);
        });

        // Address
        addressTable.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressTable.setCellFactory(TextFieldTableCell.forTableColumn());
        addressTable.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setAddress(event.getNewValue());


            UserDao userDao = new UserDao();
            userDao.update(user);
        });

        // Date Of Birth
        dobTable.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dobTable.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dobTable.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setDateOfBirth(event.getNewValue());
            UserDao userDao = new UserDao();
            userDao.update(user);

        });
        // Gender
        sexTable.setCellValueFactory(new PropertyValueFactory<>("gender"));
        sexTable.setCellFactory(TextFieldTableCell.forTableColumn());
        sexTable.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setGender(event.getNewValue());
            UserDao userDao = new UserDao();
            userDao.update(user);

        });

        // Email
        emailTable.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailTable.setCellFactory(TextFieldTableCell.forTableColumn());
        emailTable.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setEmail(event.getNewValue());
            UserDao userDao = new UserDao();
            userDao.update(user);

        });

        // Is Account Suspend
        activeColumn.setCellValueFactory(cellData -> {
            boolean active = cellData.getValue().getActivated();
            String activeAsString;
            if (active == true) {
                activeAsString = "Normal";
            } else {
                activeAsString = "Block";
            }

            return new ReadOnlyStringWrapper(activeAsString);
        });
        // handle when double click --> change state user( acitve --> block)
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                clickItem();
            }
        });
        tableView.toFront();
        tableView.setItems(observableList);

        //search list
        FilteredList<User> filteredList = new FilteredList<>(observableList, b -> true);
        filterUserName.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(User -> {
                if (newValue.isBlank() || newValue.isEmpty() || newValue == null) {
                    return true;
                }
                String searchKeyWord = newValue.toLowerCase();
                if (User.getUsername().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }


}
