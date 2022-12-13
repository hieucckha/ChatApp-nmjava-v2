package org.nmjava.chatapp.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public final class SceneController {
    private static final HashMap<String, Scene> map = new HashMap<>();

    public void addScene(String name, String path) {
        try {
            URL url = getClass().getResource(path);
            Parent root = FXMLLoader.load(url);
            map.put(name, new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene(String fxml) {
        return map.get(fxml);
    }

    public static Scene staticGetScene(String fxml) {
        return map.get(fxml);
    }
}
