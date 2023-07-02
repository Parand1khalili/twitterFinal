package com.example.demo1;

import common.Tweet;
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
import java.sql.SQLException;
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
    VBox searchHashtag;
    @FXML
    protected void onSearchClick(Event event) {
        if(searchBar.getText().equals("")){
            label.setText("search cant be empty");
            return;
        }

        ArrayList<User> users = new ArrayList<>();
        ArrayList<Tweet> tweets = new ArrayList<>();
        String serverResponse;
        if(searchBar.getText().charAt(0)=='#'){
            searchHashtag = new VBox();
            scrollPane.setContent(searchHashtag);
            searchHashtag.setSpacing(10);
            searchHashtag.setStyle("-fx-background-color:gray");
            searchHashtag.setPadding(new Insets(7, 7, 7, 7));
            try {
                IO.out.writeObject("hashtag");
                IO.out.writeObject(searchBar.getText());
                tweets = (ArrayList<Tweet>) IO.in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            for (Tweet tweet : tweets) {
                TweetComponent tweetComponent = null;
                try {
                    IO.out.writeObject("get-user");
                    IO.out.writeObject(tweet.getUserId());
                    User TUser = (User) IO.in.readObject();
                    tweetComponent = new TweetComponent(tweet,TUser);
                } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
                    throw new RuntimeException(e);
                }
                searchedUsers.getChildren().add(tweetComponent);
            }
        }
        else {
            searchedUsers = new VBox();
            scrollPane.setContent(searchedUsers);
            searchedUsers.setSpacing(10);
            searchedUsers.setStyle("-fx-background-color:gray");
            searchedUsers.setPadding(new Insets(7, 7, 7, 7));
            try {
                IO.out.writeObject("search");
                IO.out.writeObject(searchBar.getText());
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
                for (User user : users) {
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
