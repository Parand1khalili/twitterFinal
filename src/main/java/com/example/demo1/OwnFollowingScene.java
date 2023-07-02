package com.example.demo1;

import common.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OwnFollowingScene implements Initializable {
    @FXML
    Label label;
    @FXML
    ScrollPane scrollPane;
    VBox followings;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        followings = new VBox();
        scrollPane.setContent(followings);
        followings.setSpacing(10);
        followings.setStyle("-fx-background-color:gray");
        followings.setPadding(new Insets(7,7,7,7));
        ArrayList<User> users = new ArrayList<>();
        try {
            IO.out.writeObject("followings-list");
            IO.out.writeObject(logedUser.loggedUser);
            users = (ArrayList<User>) IO.in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(users.isEmpty()){
            label.setText("you dont follow anyone");
        }
        for(User user : users){
            UserComponent userComponent = null;
            try {
                userComponent = new UserComponent(user);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            followings.getChildren().add(userComponent);
        }
    }
    @FXML
    Button back;
    @FXML
    protected void onBackClick(Event event){
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
