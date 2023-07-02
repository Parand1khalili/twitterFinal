package com.example.demo1;

import common.Tweet;
import common.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FollowerScene implements Initializable {
    @FXML
    ScrollPane scrollPane;
    VBox followersView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        followersView = new VBox();
        scrollPane.setContent(followersView);
        followersView.setSpacing(10);
        followersView.setStyle("-fx-background-color:gray");
        followersView.setPadding(new Insets(7,7,7,7));
        ArrayList<User> users = new ArrayList<>();
        try {

        }
    }
    @FXML
    Button back;
    @FXML
    protected void onBackClick(Event event){
        logedUser.follow = null;
        Button button = (Button) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ownProfile.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = null;
        if(root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
        stage.show();
    }
}
