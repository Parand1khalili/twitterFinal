package com.example.demo1;

import common.Tweet;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class timeLine implements Initializable {
    @FXML
    ScrollPane scrollPane;
    VBox tweetContent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("time line entered by" + logedUser.loggedUser.getFirstName() + " " + logedUser.loggedUser.getLastName());
        tweetContent = new VBox();
        scrollPane.setContent(tweetContent);
        tweetContent.setSpacing(10);
        tweetContent.setStyle("-fx-background-color:gray");
        tweetContent.setPadding(new Insets(7,7,7,7));
        ArrayList<Tweet> tweets = new ArrayList<>();
        ArrayList<Tweet> RevTweets = new ArrayList<>();
        try {
            IO.out.writeObject("timeline");
            Thread.sleep(50);
            IO.out.writeObject(logedUser.loggedUser);
            Thread.sleep(50);
            tweets = (ArrayList<Tweet>) IO.in.readObject();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = tweets.size()-1; i >= 0; i--) {
            RevTweets.add(tweets.get(i));
        }
        for (Tweet tweet : RevTweets){
            try {
                TweetComponent tweetComponent = new TweetComponent(tweet,logedUser.loggedUser);
                tweetContent.getChildren().add(tweetComponent);
            } catch (SQLException | ClassNotFoundException | InterruptedException | IOException e) {
                throw new RuntimeException(e);
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
    ImageView search;
    @FXML
    protected void onSearchClick(Event event){
        ImageView imageView = (ImageView) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search.fxml"));
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
