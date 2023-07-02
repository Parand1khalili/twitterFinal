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
    Button following;
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
        joinedDate.setText(logedUser.otherUser.getRegisterDate().toString());
        followers.setText("followers " + logedUser.otherUser.getFollowerNum());
        following.setText("followings " + logedUser.otherUser.getFollowingNum());
        byte[] headerInByte = Base64.getDecoder().decode(logedUser.otherUser.getHeaderPicName());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(headerInByte);
        Image headerImage = new Image(inputStream);
        header.setImage(headerImage);
        headerReactangel = new Rectangle(580, 118);
        header.setFitHeight(118);
        header.setFitWidth(580);
        System.out.println("check");
        header.setClip(headerReactangel);
        byte[] avatarInByte = Base64.getDecoder().decode(logedUser.otherUser.getProfPicName());
        inputStream = new ByteArrayInputStream(avatarInByte);
        Image avatarImage = new Image(inputStream);
        avatar.setImage(avatarImage);
    }
}
