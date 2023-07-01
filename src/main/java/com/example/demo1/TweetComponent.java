package com.example.demo1;

import common.Tweet;
import common.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;

public class TweetComponent extends AnchorPane {
    static Image redHeart = new Image(""); //todo add path to red heart picture
    static Image greyHeart = new Image(""); //todo add path to gray heart picture
    ImageView likeImage;
    static Image retweetImage = new Image(""); // todo add path to retweet picture
    static Image retweeted = new Image(""); // todo add path to retweeted picture
    ImageView retweetView;
    static Image replyImage = new Image(""); // todo add path to comment picture
    ImageView replyview = new ImageView(replyImage);
    private Tweet tweet;
    Label usernameLabel;
    TextArea textArea;
    ImageView tweetImage;
    Label dateLabel;
    Rectangle rectangle;
    Circle ProfileCircle;
    Label like;
    Label reply;
    Label retweet;
    Button replyButton;
    Button retButton;
    Button likeButton;
    Separator separator1;
    Separator separator2;
    User user;
    public TweetComponent(Tweet tweet , User user) throws SQLException, IOException, ClassNotFoundException, InterruptedException {
        this.tweet = tweet;
        this.user = user;
        IO.out.writeObject("check-like"); // todo create method
        Thread.sleep(50);
        IO.out.writeObject(tweet);
        Thread.sleep(50);
        IO.out.writeObject(user);
        Thread.sleep(50);
        String respond = (String)IO.in.readObject();
        if (respond.equals("true")){
            likeImage = new ImageView(redHeart);
        }else {
            likeImage = new ImageView(greyHeart);
        }
        IO.out.writeObject("check-retweet"); // todo create method
        Thread.sleep(50);
        IO.out.writeObject(tweet);
        Thread.sleep(50);
        IO.out.writeObject(user);
        Thread.sleep(50);
        String respond1 = (String)IO.in.readObject();
        if (respond1.equals("true")){
            retweetView = new ImageView(retweeted);
        }else {
            retweetView = new ImageView(retweetImage);
        }
        usernameLabel = new Label(tweet.getUserId());
        textArea = new TextArea(tweet.getText());
        if (tweet.getPicLink() != null) {
            byte[] imageData1 = Base64.getDecoder().decode(tweet.getPicLink());
            ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(imageData1);
            Image myTweetImage = new Image(byteArrayInputStream1);
            tweetImage = new ImageView(myTweetImage);
        }else {
            tweetImage = new ImageView();
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mma.MM/dd/yyyy.");
        dateLabel = new Label(format.format(tweet.getDate()));
        like = new Label(String.valueOf(tweet.getLikes()));
        retweet = new Label(String.valueOf(tweet.getRetweet()));
        reply = new Label(String.valueOf(tweet.getComment()));
        retButton = new Button();
        likeButton = new Button();
        replyButton = new Button();
        separator1 = new Separator();
        separator2 = new Separator();
        ProfileCircle = new Circle(25);
        //byte[] receivedImageInByte = Base64.getDecoder().decode(tweet.getImageInByte());
        ProfileCircle.setStroke(Color.BLACK);
        ProfileCircle.setFill(Color.BLACK);
        byte[] imageData = Base64.getDecoder().decode(user.getProfPicName());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
        Image image = new Image(inputStream);
        ProfileCircle.setFill(new ImagePattern(image));
        this.getChildren().addAll(ProfileCircle,usernameLabel,textArea,dateLabel,like,retweet,reply,replyButton,likeButton,retButton,separator1,separator2,tweetImage);
        setConfig();
        setLocation();
        setActions();
    }
    private void setLocation()
    {
        AnchorPane.setTopAnchor(ProfileCircle,12.0);
        AnchorPane.setLeftAnchor(ProfileCircle,10.0);

        AnchorPane.setTopAnchor(usernameLabel,12.0);
        AnchorPane.setLeftAnchor(usernameLabel,65.0);
//    AnchorPane.setTopAnchor(userIdLabel,45.0);
//    AnchorPane.setLeftAnchor(userIdLabel,65.0);
        AnchorPane.setTopAnchor(textArea,70.0);
        AnchorPane.setLeftAnchor(textArea,30.0);

        AnchorPane.setTopAnchor(tweetImage,120.0);
        AnchorPane.setLeftAnchor(tweetImage,24.0);

        AnchorPane.setTopAnchor(dateLabel,230.0);
        AnchorPane.setLeftAnchor(dateLabel,30.0);
//    AnchorPane.setTopAnchor(typeOfAppLabel,370.0);
//    AnchorPane.setLeftAnchor(typeOfAppLabel,160.0);

        AnchorPane.setTopAnchor(separator1,248.0);
        AnchorPane.setLeftAnchor(separator1,0.0);

        AnchorPane.setTopAnchor(separator2,280.0);
        AnchorPane.setLeftAnchor(separator2,0.0);

        AnchorPane.setTopAnchor(retweet,255.0);
        AnchorPane.setLeftAnchor(retweet,130.0);

//    AnchorPane.setTopAnchor(retweetLabel,432.0);
//    AnchorPane.setLeftAnchor(retweetLabel,44.0);

        AnchorPane.setTopAnchor(reply,255.0);
        AnchorPane.setLeftAnchor(reply,30.0);

//    AnchorPane.setTopAnchor(quoteLabel,432.0);
//    AnchorPane.setLeftAnchor(quoteLabel,143.0);

        AnchorPane.setTopAnchor(like,255.0);
        AnchorPane.setLeftAnchor(like,230.0);

//    AnchorPane.setTopAnchor(likeLabel,432.0);
//    AnchorPane.setLeftAnchor(likeLabel,258.0);

        AnchorPane.setLeftAnchor(replyButton,42.0);
        AnchorPane.setBottomAnchor(replyButton,38.0);

        AnchorPane.setBottomAnchor(retButton,38.0);
        AnchorPane.setLeftAnchor(retButton,140.0);

        AnchorPane.setBottomAnchor(likeButton,40.0);
        AnchorPane.setLeftAnchor(likeButton,240.0);
    }
    private void bindCountLabel()
    {
        retweet.textProperty().bind(new SimpleIntegerProperty(tweet.getRetweet()).asString());
        reply.textProperty().bind(new SimpleIntegerProperty(tweet.getComment()).asString());
        like.textProperty().bind(new SimpleIntegerProperty(tweet.getLikes()).asString());
    }
    private void setConfig()
    {
        this.setPrefSize(580,320);
        this.setMinSize(580,320);
        this.setMaxSize(580,320);
        this.setStyle("-fx-background-color:#FFFFFF;" +
                "-fx-background-radius:10;" +
                "-fx-border-radius:10;" +
                "-fx-border-color:#3E3B3B");
        usernameLabel.setFont(Font.font("Roboto-Bold", FontWeight.BOLD,24));
        usernameLabel.setStyle("-fx-text-fill:#000000;");
//    userIdLabel.setFont(Font.font("Roboto", FontWeight.BOLD,12));
//    userIdLabel.setStyle("-fx-text-fill:gray;");
        textArea.setStyle("-fx-background-color:#000000;" +
                "-fx-text-fill:#000000;");
        textArea.setMaxSize(550.0,50.0);
        textArea.setMinSize(550.0,50.0);
        textArea.setFont(Font.font("Roboto",FontWeight.BLACK,14));
        textArea.setOpacity(1);
        textArea.setEditable(false);

        tweetImage.setFitHeight(110);
        tweetImage.setFitWidth(180);
        rectangle = new Rectangle(180,110);
        rectangle.setArcHeight(20.0);
        rectangle.setArcWidth(20.0);
        tweetImage.setClip(rectangle);

        dateLabel.setFont(Font.font("Roboto", FontWeight.BOLD,12));
        dateLabel.setStyle("-fx-text-fill:#000000;");

//    typeOfAppLabel.setFont(Font.font("Roboto", FontWeight.BOLD,12));
//    typeOfAppLabel.setStyle("-fx-text-fill:#3366CC;");

        separator1.setPrefSize(580.0,1.0);
        separator1.setStyle("-fx-background-color:#000000;");
        separator2.setPrefSize(580.0,1.0);
        separator2.setStyle("-fx-background-color:#000000;");
        separator1.setOpacity(0.15);
        separator2.setOpacity(0.15);
//    retImageView.setFitWidth(24);
//    retImageView.setFitWidth(24);

        retweet.setFont(Font.font("Roboto", FontWeight.BOLD,14));
        retweet.setTextFill(Paint.valueOf("#000000"));
//    retweetLabel.setFont(Font.font("Roboto", FontWeight.BLACK,12));
//    retweetLabel.setTextFill(Paint.valueOf("gray"));

        reply.setFont(Font.font("Roboto", FontWeight.BOLD,14));
        reply.setTextFill(Paint.valueOf("#000000"));

//    quoteLabel.setFont(Font.font("Roboto", FontWeight.BLACK,12));
//    quoteLabel.setTextFill(Paint.valueOf("gray"));

        like.setFont(Font.font("Roboto", FontWeight.BLACK,12));
        like.setTextFill(Paint.valueOf("#000000"));


//    likeLabel.setFont(Font.font("Roboto", FontWeight.BLACK,12));
//    likeLabel.setTextFill(Paint.valueOf("gray"));

//    quoteImageView.setFitHeight(20);
//    quoteImageView.setFitWidth(20);
        retweetView.setFitHeight(20);
        retweetView.setFitWidth(20);
        likeImage.setFitWidth(20);
        likeImage.setFitHeight(20);
        replyview.setFitHeight(20);
        replyview.setFitWidth(20);

//    quoteButton.setGraphic(quoteImageView);
//    quoteButton.setPrefSize(14,14);
//    quoteButton.setRipplerFill(Paint.valueOf("#858080"));
//    quoteButton.setStyle("-fx-background-radius: 50");


        retButton.setGraphic(retweetView);
        retButton.setPrefSize(20,20);
        //retButton.setRipplerFill(Paint.valueOf("#858080"));
        retButton.setStyle("-fx-background-color : #FFFFFF");
//    retButton.setStyle("-fx-background-radius: 50");

        likeButton.setGraphic(likeImage);
        likeButton.setPrefSize(20,20);
        //likeButton.setRipplerFill(Paint.valueOf("#858080"));
        likeButton.setStyle("-fx-background-color : #FFFFFF");
//    likeButton.setStyle("-fx-background-radius: 50");
        replyButton.setGraphic(replyview);
        replyButton.setPrefSize(10,10);
        replyButton.setStyle("-fx-background-color : #FFFFFF");
    }

    private void setActions()
    {
        replyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//          tweet.setQuotes(tweet.getCommentsOrReplies() + 1);
//          countOfQuo.setText(String.valueOf(tweet.getQuotes()));
            }
        });

        retButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    IO.out.writeObject("retweet");
                    IO.out.writeObject(tweet);
                    IO.out.writeObject(user);
                    IO.out.writeObject("checkRetweet");
                    IO.out.writeObject(tweet);
                    IO.out.writeObject(user);
                    String respond = (String)IO.in.readObject();
                    if (respond.equals("false")){
                        retweetView = new ImageView(retweetImage);
                        retweetView.setFitWidth(20);
                        retweetView.setFitHeight(20);
                        retButton.setGraphic(retweetView);
                        retButton.setPrefSize(20,20);
                        //likeButton.setRipplerFill(Paint.valueOf("#858080"));
                        retButton.setStyle("-fx-background-color : #FFFFFF");
//                likeButton.setStyle("-fx-background-radius: 50");
                        retweet.setText(String.valueOf(Integer.parseInt(retweet.getText()) - 1));
                    }else {
                        retweetView = new ImageView(retweeted);
                        retweetView.setFitWidth(20);
                        retweetView.setFitHeight(20);
                        retButton.setGraphic(retweetView);
                        retButton.setPrefSize(20,20);
                        //likeButton.setRipplerFill(Paint.valueOf("#858080"));
                        retButton.setStyle("-fx-background-color : #FFFFFF");
//                likeButton.setStyle("-fx-background-radius: 50");
                        retweet.setText(String.valueOf(Integer.parseInt(retweet.getText()) + 1));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        likeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    IO.out.writeObject("like");
                    IO.out.writeObject(tweet);
                    IO.out.writeObject(user);
                    IO.out.writeObject("checkLike");
                    IO.out.writeObject(tweet);
                    IO.out.writeObject(user);
                    String respond = (String)IO.in.readObject();
                    if (respond.equals("true")){
                        likeImage = new ImageView(redHeart);
                        likeImage.setFitWidth(20);
                        likeImage.setFitHeight(20);
                        likeButton.setGraphic(likeImage);
                        likeButton.setPrefSize(20,20);
                        //likeButton.setRipplerFill(Paint.valueOf("#858080"));
                        likeButton.setStyle("-fx-background-color : #FFFFFF");
//                likeButton.setStyle("-fx-background-radius: 50");
                        like.setText(String.valueOf(Integer.parseInt(like.getText()) + 1));
                    }else {
                        likeImage = new ImageView(greyHeart);
                        likeImage.setFitWidth(20);
                        likeImage.setFitHeight(20);
                        likeButton.setGraphic(likeImage);
                        likeButton.setPrefSize(20,20);
                        //likeButton.setRipplerFill(Paint.valueOf("#858080"));
                        likeButton.setStyle("-fx-background-color : #FFFFFF");
//                likeButton.setStyle("-fx-background-radius: 50");
                        like.setText(String.valueOf(Integer.parseInt(like.getText()) - 1));
                    }
                }
                catch (IOException e){

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}