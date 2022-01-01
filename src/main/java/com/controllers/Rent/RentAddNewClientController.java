package com.controllers.Rent;

import com.Main;
import com.api.Client;
import com.api.GoBack;
import com.requests.MongoRequests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RentAddNewClientController extends MongoRequests implements Initializable, GoBack {
    public RentAddNewClientController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/rentAddNewClient.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField idCardTextField;
    @FXML
    private Label noDataProvidedLabel;

    @Override
    public void back() {
        new RentChooseClientController();
    }

    private void next(){
        if (nameTextField.getText().isEmpty()){
            noDataProvidedLabel.setText("enter name");
            return;
        }else if(surnameTextField.getText().isEmpty()){
            noDataProvidedLabel.setText("enter surname");
            return;
        }else if(phoneTextField.getText().isEmpty()){
            noDataProvidedLabel.setText("enter phone");
            return;
        }else if(idCardTextField.getText().isEmpty()){
            noDataProvidedLabel.setText("enter ID Card");
            return;
        }else{//add new client not checking if the id card value is unique
            if(!MongoRequests.addClient(nameTextField.getText(), surnameTextField.getText(), phoneTextField.getText(), idCardTextField.getText())){
                noDataProvidedLabel.setText("same ID Card detected");
                return;
            }
            Client client = MongoRequests.getClient(idCardTextField.getText());
            new RentEquipmentController(client);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        nextButton.setOnAction(event -> next());
    }
}
