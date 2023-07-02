package com.example.demo1;

import common.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Search {
    @FXML
    Label label;
    @FXML
    TextField searchBar;
    @FXML
    Button searchButton;
    @FXML
    ScrollPane scrollPane;
    VBox searchedUsers;
    @FXML
    protected void onSearchClick(Event event) {
        searchedUsers = new VBox();
        scrollPane.setContent(searchedUsers);
        searchedUsers.setSpacing(10);
        searchedUsers.setStyle("-fx-background-color:gray");
        searchedUsers.setPadding(new Insets(7, 7, 7, 7));
        ArrayList<User> users = new ArrayList<>();
        String serverResponse;
        try {
            IO.out.writeObject("search");
            IO.out.writeObject(logedUser.loggedUser);
            serverResponse = (String) IO.in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (serverResponse.equals("not-found")) {
            label.setText(serverResponse);
        } else {
            try {
                users = (ArrayList<User>) IO.in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            for(User user : users){
                UserComponent userComponent = null;
                try {
                    userComponent = new UserComponent(user);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                searchedUsers.getChildren().add(userComponent);
            }
        }
    }


    @FXML
    ImageView profile;
    @FXML
    protected void onProfileClick(Event event){
        ImageView imageView = (ImageView) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ownProfile.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) imageView.getScene().getWindow();
        Scene scene = null;
        if(root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    ImageView newTweet;
    @FXML
    protected void onNewTweetClick(Event event){
        ImageView imageView = (ImageView) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newTweet.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) imageView.getScene().getWindow();
        Scene scene = null;
        if(root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    ImageView timeLine;
    @FXML
    protected void onTimeLineClick(Event event){
        ImageView imageView = (ImageView) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("timeLine.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) imageView.getScene().getWindow();
        Scene scene = null;
        if(root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
        stage.show();
    }
}
