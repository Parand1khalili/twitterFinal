package com.example.demo1;

import common.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class otherProfile implements Initializable {
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("otherFollowerScene.fxml"));
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
    Button followings;
    @FXML
    protected void onFollowingsClick(Event event){
        Button button = (Button) event.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("otherFollowingScene.fxml"));
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
    Button blockButton;
    @FXML
    protected void onBlockClick(Event event) {
        String serverResponse = " ";
        try {
            IO.out.writeObject("check-block");
            IO.out.writeObject(logedUser.loggedUser);
            IO.out.writeObject(logedUser.otherUser);
            serverResponse = (String) IO.in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (serverResponse.equals("true")) {
            try {
                IO.out.writeObject("unblock");
                IO.out.writeObject(logedUser.loggedUser);
                IO.out.writeObject(logedUser.otherUser);
                serverResponse = (String) IO.in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(serverResponse.equals("success")){
                blockButton.setText("block");
            }
        }
        else{
            try {
                IO.out.writeObject("block");
                IO.out.writeObject(logedUser.loggedUser);
                IO.out.writeObject(logedUser.otherUser);
                serverResponse = (String) IO.in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(serverResponse.equals("success")){
                blockButton.setText("unblock");
            }
        }
    }
    @FXML
    Button followButton;
    @FXML
    protected void onFollowClick(Event event){
        String serverResponse = " ";
        try {
            IO.out.writeObject("check-follow");
            IO.out.writeObject(logedUser.loggedUser);
            IO.out.writeObject(logedUser.otherUser);
            serverResponse = (String) IO.in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (serverResponse.equals("true")) {
            try {
                IO.out.writeObject("unfollow");
                IO.out.writeObject(logedUser.loggedUser);
                IO.out.writeObject(logedUser.otherUser);
                serverResponse = (String) IO.in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(serverResponse.equals("success")){
                blockButton.setText("follow");
            }
        }
        else{
            try {
                IO.out.writeObject("follow");
                IO.out.writeObject(logedUser.loggedUser);
                IO.out.writeObject(logedUser.otherUser);
                serverResponse = (String) IO.in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(serverResponse.equals("success")){
                blockButton.setText("unfollow");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User updatedUser = null;
        try {
            IO.out.writeObject("get-user");
            Thread.sleep(50);
            IO.out.writeObject(logedUser.otherUser.getId());
            Thread.sleep(50);
            updatedUser =(User) IO.in.readObject();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        logedUser.otherUser = updatedUser;
        System.out.println(logedUser.otherUser.getId());
        System.out.println(logedUser.otherUser.getBio());
        userId.setText(logedUser.otherUser.getId());
        bio.setText(logedUser.otherUser.getBio());
        location.setText(logedUser.otherUser.getLocation());
        web.setText(logedUser.otherUser.getWeb());
        joinedDate.setText(logedUser.otherUser.getRegisterDate());
        followers.setText("followers " + logedUser.otherUser.getFollowerNum());
        followings.setText("followings " + logedUser.otherUser.getFollowingNum());
        byte[] headerInByte = Base64.getDecoder().decode(logedUser.otherUser.getHeaderPicName());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(headerInByte);
        Image headerImage = new Image(inputStream);
        header.setImage(headerImage);
        headerReactangel = new Rectangle(580, 118);
        header.setFitHeight(118);
        header.setFitWidth(580);
        header.setClip(headerReactangel);
        byte[] avatarInByte = Base64.getDecoder().decode(logedUser.otherUser.getProfPicName());
        inputStream = new ByteArrayInputStream(avatarInByte);
        Image avatarImage = new Image(inputStream);
        avatar.setImage(avatarImage);
        String serverResponse = " ";
        try {
            IO.out.writeObject("check-block");
            IO.out.writeObject(logedUser.loggedUser);
            IO.out.writeObject(logedUser.otherUser);
            serverResponse = (String) IO.in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(serverResponse.equals("true")){
            blockButton.setText("unblock");
        }
        else{
            blockButton.setText("block");
        }
        try {
            IO.out.writeObject("check-follow");
            IO.out.writeObject(logedUser.loggedUser);
            IO.out.writeObject(logedUser.otherUser);
            serverResponse = (String) IO.in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(serverResponse.equals("true")){
            blockButton.setText("unfollow");
        }
        else{
            blockButton.setText("follow");
        }
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
}
