package org.nmjava.chatapp.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.nmjava.chatapp.client.networks.SocketClient;
import org.nmjava.chatapp.client.utils.SceneController;

public class Main extends Application {
    public static SocketClient socketClient;
    public static Stage stage;
    public static String UserName;

    private void registerScene() {
        SceneController sc = new SceneController();
        sc.addScene("Login", "/org/nmjava/chatapp/client/views/Login.fxml");
        sc.addScene("Signup", "/org/nmjava/chatapp/client/views/Signup.fxml");
        sc.addScene("ForgotPw", "/org/nmjava/chatapp/client/views/ForgotPw.fxml");

        sc.addScene("AdminHome", "/org/nmjava/chatapp/client/views/AdminHome.fxml");
        sc.addScene("AdminGroup", "/org/nmjava/chatapp/client/views/AdminGroup.fxml");
        sc.addScene("AdminLogin", "/org/nmjava/chatapp/client/views/AdminLogin.fxml");

        sc.addScene("UserHome", "/org/nmjava/chatapp/client/views/UserHome.fxml");
        sc.addScene("UserFriendOnline", "/org/nmjava/chatapp/client/views/UserFriendOnline.fxml");

        sc.addScene("Test", "/org/nmjava/chatapp/client/views/Test.fxml");
        sc.addScene("TestingComponent", "/org/nmjava/chatapp/client/views/TestingComponents.fxml");
        sc.addScene("ListAddFriendReq", "/org/nmjava/chatapp/client/views/ListReqAddFriend.fxml");
        sc.addScene("CreateGroup", "/org/nmjava/chatapp/client/views/CreateGroupChat.fxml");
        sc.addScene("SettingConservation", "/org/nmjava/chatapp/client/views/SettingConservation.fxml");
    }

    @Override
    public void start(Stage primaryStage) {
        stage = new Stage();
        stage.setScene(SceneController.staticGetScene("Login"));
        stage.setTitle("Login");
        socketClient = new SocketClient();
        socketClient.startConnection("localhost", 9999);
        // Sent Request
//        socketClient.addRequestToQueue(AuthenticationRequest.builder().username("something herer").password("something herer").build());
        // Get Response
//        socketClient.getResponseFromQueue();

        registerScene();

        primaryStage.setTitle("Hello!");
        primaryStage.setScene(SceneController.staticGetScene("Login"));
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}