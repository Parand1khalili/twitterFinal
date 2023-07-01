package com.example.demo1;

import common.Tweet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

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
        tweetContent = new VBox();
        scrollPane.setContent(tweetContent);
        tweetContent.setSpacing(10);
        tweetContent.setStyle("-fx-background-color:gray" + "-fx-padding: 8;");
        ArrayList<Tweet> tweets = new ArrayList<>();
        try {
            IO.out.writeObject("time-line");
            Thread.sleep(50);
            IO.out.writeObject(logedUser.loggedUser);
            Thread.sleep(50);
            tweets = (ArrayList<Tweet>) IO.in.readObject();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Tweet tweet : tweets){
            try {
                TweetComponent tweetComponent = new TweetComponent(tweet,logedUser.loggedUser);
                tweetContent.getChildren().add(tweetComponent);
            } catch (SQLException | ClassNotFoundException | InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
