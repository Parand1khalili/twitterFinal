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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class editProfile implements Initializable {
    @FXML
    File newHeader;
    @FXML
    File newAvatar;
    @FXML
    Label userName;
    @FXML
    TextArea bio;
    @FXML
    TextArea location;
    @FXML
    TextArea web;
    @FXML
    Label error;
    @FXML
    ImageView header;
    @FXML
    protected void onHeaderClick(Event event){
        FileChooser fileChooser = new FileChooser();
        this.newHeader = fileChooser.showOpenDialog(null);
    }
    @FXML
    ImageView avatar;
    @FXML
    protected void onAvatarClick(Event event){
        FileChooser fileChooser = new FileChooser();
        this.newAvatar = fileChooser.showOpenDialog(null);
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        byte[] headerInByte = Base64.getDecoder().decode(logedUser.loggedUser.getHeaderPicName());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(headerInByte);
        Image headerImage = new Image(inputStream);
        header.setImage(headerImage);
        byte[] avatarInByte = Base64.getDecoder().decode(logedUser.loggedUser.getProfPicName());
        inputStream = new ByteArrayInputStream(avatarInByte);
        Image avatarImage = new Image(inputStream);
        avatar.setImage(avatarImage);
        userName.setText(logedUser.loggedUser.getId());
        bio.setText(logedUser.loggedUser.getBio());
        location.setText(logedUser.loggedUser.getLocation());
        web.setText(logedUser.loggedUser.getWeb());
    }
    @FXML
    Button save;
    @FXML
    protected void onSaveClick(Event event){
        if(newHeader != null) {
            byte[] newHeaderInByte = null;
            FileInputStream newHeaderInputStream;
            try {
                newHeaderInputStream = new FileInputStream(newHeader);
                newHeaderInByte = newHeaderInputStream.readAllBytes();
            } catch (IOException e) {
                error.setText("cant transfer file!");
            }
            String newHeaderString = Base64.getEncoder().encodeToString(newHeaderInByte);
            String serverRespondH = null;
            try {
                IO.out.writeObject("edit-header");
                Thread.sleep(50);
                IO.out.writeObject(logedUser.loggedUser);
                Thread.sleep(50);
                IO.out.writeObject(newHeaderString);
                Thread.sleep(50);
                serverRespondH =(String) IO.in.readObject();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                error.setText("check your connection to server");
            }
            if(serverRespondH.equals("success"))
                System.out.println("header updated");
        }
        if(newAvatar != null){
            byte[] newAvatarInByte = null;
            FileInputStream newAvatarInputStream;
            try {
                newAvatarInputStream = new FileInputStream(newAvatar);
                newAvatarInByte = newAvatarInputStream.readAllBytes();
            } catch (IOException e) {
                error.setText("cant transfer file!");
            }
            String newAvatarString = Base64.getEncoder().encodeToString(newAvatarInByte);
            String serverRespondA = null;
            try {
                IO.out.writeObject("edit-avatar");
                Thread.sleep(50);
                IO.out.writeObject(logedUser.loggedUser);
                Thread.sleep(50);
                IO.out.writeObject(newAvatarString);
                Thread.sleep(50);
                serverRespondA =(String) IO.in.readObject();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                error.setText("check your connection to server");
            }
            if(serverRespondA.equals("success"))
                System.out.println("avatar updated");
        }
        if(!bio.getText().equals(logedUser.loggedUser.getBio())){
            String serverRespondB = null;
            try {
                IO.out.writeObject("edit-bio");
                Thread.sleep(50);
                IO.out.writeObject(logedUser.loggedUser);
                Thread.sleep(50);
                IO.out.writeObject(bio.getText());
                Thread.sleep(50);
                serverRespondB =(String) IO.in.readObject();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                error.setText("check your connection to server");
            }
            if(serverRespondB.equals("success"))
                System.out.println("bio updated");
        }
        if(!location.getText().equals(logedUser.loggedUser.getLocation())){
            String serverRespondL = null;
            try {
                IO.out.writeObject("edit-location");
                Thread.sleep(50);
                IO.out.writeObject(logedUser.loggedUser);
                Thread.sleep(50);
                IO.out.writeObject(location.getText());
                Thread.sleep(50);
                serverRespondL =(String) IO.in.readObject();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                error.setText("check your connection to server");
            }
            if(serverRespondL.equals("success"))
                System.out.println("location updated");
        }
        if(!web.getText().equals(logedUser.loggedUser.getWeb())){
            String serverRespondW = null;
            try {
                IO.out.writeObject("edit-web");
                Thread.sleep(50);
                IO.out.writeObject(logedUser.loggedUser);
                Thread.sleep(50);
                IO.out.writeObject(web.getText());
                Thread.sleep(50);
                serverRespondW =(String) IO.in.readObject();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                error.setText("check your connection to server");
            }
            if(serverRespondW.equals("success"))
                System.out.println("web updated");
        }
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
