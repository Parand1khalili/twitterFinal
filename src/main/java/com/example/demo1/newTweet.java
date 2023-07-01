package com.example.demo1;

import common.Tweet;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class newTweet implements Initializable{
    @FXML
    TextField tweetText;
    @FXML
    Label newTweetSceneLabel;
    @FXML
    Label error;
    @FXML
    File tweetImage;
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
    @FXML
    ImageView addPicture;
    @FXML
    protected void  onAddPicture(Event event){
        newTweetSceneLabel.setText("");
        FileChooser fileChooser = new FileChooser();
        this.tweetImage = fileChooser.showOpenDialog(null);
        if(tweetImage==null){
            newTweetSceneLabel.setText("click on the image to add a picture");
        }
        System.out.println("picture added");
    }
    @FXML
    Button tweet;
    @FXML
    protected void onTweetClick(Event event){
        if(tweetText.getText() == null || tweetText.getText().isEmpty()){
            error.setText("tweet text cant be empty");
            return;
        }
        if(tweetText.getText().length() > 280){
            error.setText("text should be less than 280 characters");
        }
        if(tweetImage != null){
            if(!tweetImage.getName().endsWith(".png") && !tweetImage.getName().endsWith(".jpg")){
                error.setText("file format is not correct select a .png or .jpg format");
                return;
            }
            byte[] picInByte = null;
            FileInputStream fIn;
            try {
                fIn = new FileInputStream(tweetImage);
                picInByte = fIn.readAllBytes();
            } catch (IOException e) {
                error.setText("cant transfer file!");
            }
            String picString = Base64.getEncoder().encodeToString(picInByte);
            Tweet newTweet = new Tweet(tweetText.getText(),picString,logedUser.loggedUser.getId());
            String serverRespond = " ";
            try {
                IO.out.writeObject("new-tweet");
                Thread.sleep(50);
                IO.out.writeObject(newTweet);
                Thread.sleep(50);
                serverRespond = (String) IO.in.readObject();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                error.setText("check your connection to server");
            }
            if(serverRespond.equals("success")){
                System.out.println("tweet with image uploaded");
                newTweetSceneLabel.setText("tweet with image uploaded");
                newTweetSceneLabel.setText("SUCCESS");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    error.setText("Thread cant sleep");
                }
                Button button = (Button) event.getSource();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newTweet.fxml"));
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
        else{
            Tweet newTweet = new Tweet(tweetText.getText(),null,logedUser.loggedUser.getId());
            String serverRespond = " ";
            try {
                IO.out.writeObject("new-tweet");
                Thread.sleep(50);
                IO.out.writeObject(newTweet);
                Thread.sleep(50);
                serverRespond = (String) IO.in.readObject();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                error.setText("check your connection to server");
            }
            if(serverRespond.equals("success")){
                System.out.println("tweet uploaded");
                newTweetSceneLabel.setText("tweet uploaded");
                newTweetSceneLabel.setText("SUCCESS");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    error.setText("Thread cant sleep");
                }
                Button button = (Button) event.getSource();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newTweet.fxml"));
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
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newTweetSceneLabel.setText("click on the image to add a picture");
        error.setText("");
        tweetText.setText("");
    }
}
