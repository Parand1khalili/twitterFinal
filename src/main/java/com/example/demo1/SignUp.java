package com.example.demo1;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp implements Initializable {
    @FXML
    Label errorConnection;
    @FXML
    TextField userName;
    @FXML
    Label errorUserName;
    @FXML
    TextField firstName;
    @FXML
    Label errorFirstName;
    @FXML
    TextField lastName;
    @FXML
    Label errorLastName;
    @FXML
    TextField email;
    @FXML
    Label errorEmail;
    @FXML
    TextField phoneNumber;
    @FXML
    Label errorPhoneNumber;
    @FXML
    TextField password;
    @FXML
    Label errorPassword;
    @FXML
    TextField repeatPassword;
    @FXML
    Label errorRepeatPassword;
    @FXML
    DatePicker birthdate;

    @FXML
    Label errorBirthdate;
    @FXML
    ChoiceBox<String> country;
    @FXML
    Label errorCountry;
    ObservableList<String> countries= FXCollections.observableArrayList("Afghanistan","Albania","Algeria","Andorra","Angola","Antigua & Deps",
            "Argentina","Armenia","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin",
            "Bhutan","Bolivia","Bosnia Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina","Burundi","Cambodia","Cameroon","Canada","Cape Verde",
            "Central African Rep","Chad","Chile","China","Colombia","Comoros","Congo","Congo {Democratic Rep}","Costa Rica","Croatia","Cuba","Cyprus",
            "Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","East Timor","Ecuador","Egypt","El Salvador","Equatorial Guinea",
            "Eritrea","Estonia","Ethiopia","Fiji","Finland","France","Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada","Guatemala",
            "Guinea","Guinea-Bissau","Guyana","Haiti","Honduras","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland {Republic}","Israel",
            "Italy","Ivory Coast","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati","Korea North","Korea South","Kosovo","Kuwait","Kyrgyzstan",
            "Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macedonia","Madagascar","Malakhestan",
            "Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Mauritania","Mauritius","Mexico","Micronesia","Moldova","Monaco",
            "Mongolia","Montenegro","Morocco","Mozambique","Myanmar, {Burma}","Namibia","Nauru","Nepal","Netherlands","New Zealand","Nicaragua",
            "Niger","Nigeria","Norway","Oman","Pakistan","Palau","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal",
            "Qatar","Qom","Romania","Russian Federation","Rwanda","St Kitts & Nevis","St Lucia","Saint Vincent & the Grenadines","Samoa","San Marino",
            "Sao Tome & Principe","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","Solomon Islands","Somalia",
            "South Africa","South Sudan","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan",
            "Tanzania","Thailand","Togo","Tonga","Trinidad & Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","Ukraine","United Arab Emirates",
            "United Kingdom","United States","Uruguay","Uzbekistan","Vanuatu","Vatican City","Venezuela","Vietnam","Yemen","Zambia","Zimbabwe");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        country.setItems(countries);
    }
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
    Button done;
    @FXML
    protected void onDoneClick(Event event){

        errorUserName.setText("");
        errorFirstName.setText("");
        errorLastName.setText("");
        errorEmail.setText("");
        errorPhoneNumber.setText("");
        errorPassword.setText("");
        errorRepeatPassword.setText("");
        errorCountry.setText("");
        errorBirthdate.setText("");

        boolean canLogin = true;
        if(userName.getText().equals("")){
            errorUserName.setText("user name cant be empty");
            canLogin = false;
        }
        if(firstName.getText().equals("")){
            errorFirstName.setText("first name cant be empty");
            canLogin = false;
        }
        if(lastName.getText().equals("")){
            errorLastName.setText("last name cant be empty");
            canLogin = false;
        }
        if(!email.getText().equals("") && !emailValidity(email.getText())){
            errorEmail.setText("invalid email format");
            canLogin = false;
        }
        if(email.getText().equals("") && phoneNumber.getText().equals("")){
            errorEmail.setText("one of these should be filled");
            errorPhoneNumber.setText("one of these should be filled");
            canLogin = false;
        }
        if(password.getText().equals("")){
            errorPassword.setText("password cant be empty");
            canLogin = false;
        }
        if(repeatPassword.getText().equals("")){
            errorRepeatPassword.setText("please enter password again");
            canLogin = false;
        }
        if(!password.getText().equals("") && !checkPass(password.getText())){
            errorPassword.setText("invalid password format");
            canLogin = false;
        }
        if(!repeatPassword.getText().equals("") &&
                !repeatPassword.getText().equals(password.getText())){
            errorRepeatPassword.setText("repeat password correctly");
            canLogin = false;
        }
        if(country.getValue() == null){
            errorCountry.setText("pick a country");
            canLogin = false;
        }
        if(birthdate.getValue() == null){
            errorBirthdate.setText("pick  country");
            canLogin = false;
        }
        if(!canLogin){
            return;
        }
        User newUser = new User(userName.getText(),firstName.getText(),lastName.getText(),email.getText(),phoneNumber.getText(),password.getText(),country.getValue(),String.valueOf(birthdate));
        try {
            IO.out.writeObject("sign-up");
            Thread.sleep(500);
            IO.out.writeObject(newUser);
        } catch (IOException | InterruptedException e) {
            errorConnection.setText("check your connection to server");
        }
        try {
            if(((String) IO.in.readObject()).equals("duplicate-id")){
                errorUserName.setText("user name already taken");
            }
            else if(((String) IO.in.readObject()).equals("duplicate-email")){
                errorEmail.setText("email already used");
            }
            else if(((String) IO.in.readObject()).equals("duplicate-number")){
                errorPhoneNumber.setText("phone number already used");
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
            errorConnection.setText("check your connection to server");
        }
    }
    public static boolean emailValidity(String email){
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean checkPass(String pass){
        boolean isOkay=true;
        if(pass.length() < 8)
            isOkay=false;
        if(!pass.matches(".*[A-Z].*") || !pass.matches(".*[a-z]*."))
            isOkay=false;
        return isOkay;
    }
}
