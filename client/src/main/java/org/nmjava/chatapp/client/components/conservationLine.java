package org.nmjava.chatapp.client.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.nmjava.chatapp.commons.models.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class conservationLine extends HBox implements Initializable {

    @FXML
    private Label message;
    @FXML
    private HBox messageBox;
    @FXML
    private VBox messageInfor;

    public conservationLine(Message msg, Boolean type) {
        loadFXML();
        this.message.setText(msg.getMessage());
        this.message.setPadding(new Insets(5,5,5,5));
        this.message.setWrapText(true);
        this.message.setPadding(new Insets(5, 0, 5, 0));

        if(!type==true)
        {
            this.messageBox.setPadding(new Insets(15, 12, 15, 12));
            this.messageBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, new  CornerRadii(10),null)));
            this.setAlignment(Pos.CENTER_LEFT);
            messageInfor.getChildren().add(0,new Label(msg.getSender()));
        }
        else{
            this.messageBox.setPadding(new Insets(15, 12, 15, 12));
            this.messageBox.setBackground(new Background(new BackgroundFill(Color.DARKORANGE, new  CornerRadii(10),null)));
            this.setAlignment(Pos.CENTER_RIGHT);
//            this.messageBox.getStyleClass().set(0,"send");
        }
    }

    public void loadFXML() {
        URL url = Avatar.class.getResource("conservationLine.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



}
