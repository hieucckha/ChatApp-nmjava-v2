package org.nmjava.chatapp.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.nmjava.chatapp.client.networks.SocketClient;
import org.nmjava.chatapp.client.utils.SceneController;

public class Main extends Application {
    private SocketClient socketClient;

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

//        sc.addScene("Test", "/org/nmjava/chFatapp/client/views/Test.fxml");
        sc.addScene("TestingComponent", "/org/nmjava/chatapp/client/views/TestingComponents.fxml");
    }

    @Override
    public void start(Stage primaryStage) {
//        socketClient = new SocketClient();
//        socketClient.startConnection("localhost", 9999);
        // Sent Request
//        socketClient.addRequestToQueue(AuthenticationRequest.builder().username("something herer").password("something herer").build());
        // Get Response
//        socketClient.getResponseFromQueue();

        registerScene();

        primaryStage.setTitle("Hello!");
        primaryStage.setScene(SceneController.staticGetScene("AdminLogin"));
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}