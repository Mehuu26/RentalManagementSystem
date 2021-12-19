package com.controllers;

import com.requests.LogInRequest;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.Main;

public class LogInController implements Initializable {

    public LogInController(){   //
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/logIn.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 600, 400));
        //primaryStage.show();
    }

    @FXML
    private Button logInButton;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label wrongLogInLabel;

    public void userLogIn(){
        try{
            if(dataCheck()){
                new MainPanelController();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean dataCheck() throws IOException {   //check if the data is ok or wrong
        LogInRequest logIn = new LogInRequest();

        try {
            logIn.LogInDataCheck(userNameTextField.getText().toString(), passwordField.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        };

            if (!userNameTextField.getText().isEmpty() && !passwordField.getText().isEmpty()){ //if there is not empty
                if(userNameTextField.getText().toString().equals("admin") && passwordField.getText().toString().equals("admin")) {


                    //połaczenie bazy danych


                    return true;
                }else{
                    wrongLogInLabel.setText("wrong user login or password");
                    return false;
                }
            }else{
                wrongLogInLabel.setText("enter user login or password");
                return false;
            }

        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        logInButton.setOnAction(event -> userLogIn());
    }
}


