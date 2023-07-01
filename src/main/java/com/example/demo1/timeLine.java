package com.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class timeLine implements Initializable {
    @FXML
    ScrollPane scrollPane;
    @FXML
    VBox tweetContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tweetContent = new VBox();
        scrollPane.setContent(tweetContent);
        tweetContent.setSpacing(10);

        tweetContent.setStyle("-fx-background-color:gray");

    }

}
