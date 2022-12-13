package org.nmjava.chatapp.client.components;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Avatar extends AnchorPane implements Initializable {
    @FXML
    private Label activeSymbol;
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Avatar(double width, double height) {
        loadFXML();
        setAvatarWidth(width);
        setAvatarHeight(height);

        URL url = getClass().getResource("/org/nmjava/chatapp/client/assects/images/avatar.png");
        setImage(String.valueOf(url));
    }

    public void loadFXML() {
        URL url = Avatar.class.getResource("Avatar.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();

            this.prefHeightProperty().bind(this.heightProperty());
            this.prefWidthProperty().bind(this.widthProperty());

            this.image.fitHeightProperty().bind(this.heightProperty());
            this.image.fitWidthProperty().bind(this.widthProperty());

            this.activeSymbol.prefWidthProperty().bind(Bindings.multiply(0.25, this.widthProperty()));
            this.activeSymbol.prefHeightProperty().bind(this.activeSymbol.prefWidthProperty());
        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }
    }

    public void circleImage() {
        final Circle clip = new Circle(image.getFitWidth() / 2, image.getFitWidth() / 2, image.getFitWidth() / 2);
        this.image.setClip(clip);
    }

    public void setImage(String path) {
        this.image.setImage(new Image(path));
        circleImage();
    }

    public void setImage(Image image) {
        this.image.setImage(image);
        circleImage();
    }

    public void setAvatarWidth(double width) {
        this.setWidth(width);
    }

    public void setAvatarHeight(double height) {
        this.setHeight(height);
    }


}
