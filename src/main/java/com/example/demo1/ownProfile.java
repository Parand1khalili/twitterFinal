package com.example.demo1;
import common.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ownProfile implements Initializable {
    @FXML
    ImageView header;
    @FXML
    ImageView profPhoto;
    @FXML
    Label userId;
    @FXML
    Label bio;
    @FXML
    Label joinedDate;
    @FXML
    Label location;
    @FXML
    Label web;
    @FXML
    Button editProfile;
    @FXML
    Button followers;
    @FXML
    Button following;
    @FXML
    Tab tweets;
    @FXML
    VBox allTweets;
    @FXML
    Tab replies;
    @FXML
    Tab likes;
    @FXML
    Button timeLine;
    @FXML
    Button search;
    @FXML
    Button newTweet;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Profile loggedUserProfile = null;
        ArrayList<Tweet> loggedUserTweets;
        try {
            IO.out.writeObject("get-profile");
            Thread.sleep(50);
            IO.out.writeObject(logedUser.loggedUser);
            Thread.sleep(50);
            IO.out.writeObject(logedUser.loggedUser);
            Thread.sleep(50);
            loggedUserProfile = (Profile) IO.in.readObject();
            loggedUserTweets = (ArrayList<Tweet>) IO.in.readObject();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            // todo label error
        }
        if(loggedUserProfile != null) {
            userId.setText(logedUser.loggedUser.getId());
            bio.setText(loggedUserProfile.getBio());
            location.setText(loggedUserProfile.getLocation());
            web.setText(loggedUserProfile.getWeb());
            followers.setText("followers " + loggedUserProfile.getFollowerNum());
            following.setText("followings " + loggedUserProfile.getFollowingNum());
            // todo set header
            // todo set avatar
           // for(Tweet tweet : loggedUserTweets){
                //Label tweetLabel = new Label(Tweet.);
           // }
        }
    }
}
