package com.example.demo1;

import common.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class UserComponent extends AnchorPane {
    Circle ProfileCircle;
    Label userId;
    Label userBio;
    Button follow;
    User theUser;
    public UserComponent(User user) throws IOException, ClassNotFoundException {
        theUser = user;
        this.userId = new Label(user.getId());
        this.userBio = new Label(user.getBio());
        IO.out.writeObject("check-follow");
        IO.out.writeObject(logedUser.loggedUser);
        IO.out.writeObject(user);
        if(((String)IO.in.readObject()).equals("true")){
            this.follow = new Button("unfollow");
        }
        else{
            this.follow = new Button("follow");
        }
        ProfileCircle = new Circle(20);
        byte[] avatarInByte = Base64.getDecoder().decode(user.getProfPicName());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(avatarInByte);
        Image avatarImage = new Image(inputStream);
        ProfileCircle.setFill(new ImagePattern(avatarImage));
        this.getChildren().addAll(ProfileCircle,follow,userId,userBio);
        setLocation();
    }

    private void setLocation() {
        AnchorPane.setTopAnchor(ProfileCircle,5.0);
        AnchorPane.setLeftAnchor(ProfileCircle,25.0);

        AnchorPane.setTopAnchor(userId,5.0);
        AnchorPane.setLeftAnchor(userId,75.0);

        AnchorPane.setTopAnchor(userBio,30.0);
        AnchorPane.setLeftAnchor(userBio,75.5);

        AnchorPane.setTopAnchor(follow,17.5);
        AnchorPane.setLeftAnchor(follow,150.0);
    }

    private void setConfig() {
        this.setPrefSize(580,80);
        this.setMinSize(580,80);
        this.setMaxSize(580,80);
        this.setStyle("-fx-background-color:#FFFFFF;");
        userId.setFont(Font.font("Roboto-Bold", FontWeight.BOLD,28));
        userId.setStyle("-fx-text-fill:#000000;");
        userBio.setFont(Font.font("Roboto",FontWeight.NORMAL,26));
        userBio.setStyle("-fx-text-fill:#000000;");
        follow.setStyle("-fx-text-fill:#1DA1F2;");
        follow.setPrefSize(85,25);
    }
    public void setAction(){
        ProfileCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { // todo othersProfile
//                try {
//                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("othersProfile.fxml"));
//                    Scene scene = new Scene(fxmlLoader.load());
//                    stage.setTitle("twitter2.0");
//                    Image icon = new Image("D:/apps/twitter2.0/Client/src/main/resources/com/example/client/download.png");
//                    stage.getIcons().add(icon);
//                    stage.setScene(scene);
//                    stage.show();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });
        follow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    IO.out.writeObject("check-follow");
                    IO.out.writeObject(logedUser.loggedUser);
                    IO.out.writeObject(theUser);
                    if (((String) IO.in.readObject()).equals("true")){
                        IO.out.writeObject("unfollow");
                        IO.out.writeObject(logedUser.loggedUser);
                        IO.out.writeObject(theUser.getId());
                        follow.setText("follow");
                    }else {
                        IO.out.writeObject("follow");
                        IO.out.writeObject(logedUser.loggedUser);
                        IO.out.writeObject(theUser.getId());
                        follow.setText("following");
                        System.out.println("followed");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
