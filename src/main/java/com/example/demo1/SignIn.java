package com.example.demo1;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignIn {
    @FXML
    TextField userName;
    @FXML
    TextField password;
    @FXML
    Label error;
    @FXML
    Button back;
    @FXML
    protected void onBackClick(Event event){
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
    Button login;
    @FXML
    protected void onLoginClick(Event event){
        error.setText("");
        boolean canLogin = true;
        if(userName.getText().equals("")){
            error.setText("user name cant be empty");
            canLogin = false;
        }
        if(password.getText().equals("")){
            error.setText("password cant be empty");
            canLogin = false;
        }
        if(!canLogin){
            return;
        }
        User theUser = new User(userName.getText(),password.getText());
        try {
            IO.out.writeObject("sign-in");
            Thread.sleep(500);
            IO.out.writeObject(theUser);
        } catch (IOException | InterruptedException e) {
            error.setText("check your connection to server");
        }
        try {
            if(((String) IO.in.readObject()).equals("not-found")){
                error.setText("user not found try again");
            }
            else if(((String) IO.in.readObject()).equals("wrong-pass")){
                error.setText("incorrect password try again");
            }
            else if(((String) IO.in.readObject()).equals("success")){
                Button button = (Button) event.getSource();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
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
        } catch (ClassNotFoundException | IOException e) {
            error.setText("check your connection to server");
        }

    }

}
