package com.example.demo1;
import common.*;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;

public class ownProfile implements Initializable{
    @FXML
    Rectangle headerReactangel;
    @FXML
    ImageView header;
    @FXML
    ImageView avatar;
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
    Button followers;
    @FXML
    protected void onFollowersClick(Event event){
        Button button = (Button) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ownFollowerScene.fxml"));
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
    @FXML
    Button following;
    @FXML
    protected void onFollowingsClick(Event event){
        Button button = (Button) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ownFollowingScene.fxml"));
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User updatedUser=null;
        try {
            IO.out.writeObject("get-user");
            Thread.sleep(50);
            IO.out.writeObject(logedUser.loggedUser.getId());
            Thread.sleep(50);
            updatedUser =(User) IO.in.readObject();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        logedUser.loggedUser = updatedUser;
        userId.setText(logedUser.loggedUser.getId());
        bio.setText(logedUser.loggedUser.getBio());
        location.setText(logedUser.loggedUser.getLocation());
        web.setText(logedUser.loggedUser.getWeb());
        joinedDate.setText(logedUser.loggedUser.getRegisterDate().toString());
        followers.setText("followers " + logedUser.loggedUser.getFollowerNum());
        following.setText("followings " + logedUser.loggedUser.getFollowingNum());
        byte[] headerInByte = Base64.getDecoder().decode(logedUser.loggedUser.getHeaderPicName());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(headerInByte);
        Image headerImage = new Image(inputStream);
        header.setImage(headerImage);
        headerReactangel = new Rectangle(580, 118);
        header.setFitHeight(118);
        header.setFitWidth(580);
        System.out.println("check");
        header.setClip(headerReactangel);
        byte[] avatarInByte = Base64.getDecoder().decode(logedUser.loggedUser.getProfPicName());
        inputStream = new ByteArrayInputStream(avatarInByte);
        Image avatarImage = new Image(inputStream);
        avatar.setImage(avatarImage);
    }
    @FXML
    Button logout;
    @FXML
    protected void onLogoutClick(Event event){
        logedUser.loggedUser = null;
        Button button = (Button) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
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
    @FXML
    Button editProfile;
    @FXML
    protected void onEditProfileClick(Event event){
        Button button = (Button) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editProfile.fxml"));
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
