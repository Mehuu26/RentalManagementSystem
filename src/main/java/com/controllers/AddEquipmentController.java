package com.controllers;

import com.Main;
import com.api.GoBack;
import com.requests.AddEquipmentRequest;
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

public class AddEquipmentController implements Initializable, GoBack {
    public AddEquipmentController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/addEquipmentPanel.fxml"));
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
    private Button addButton;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField producerTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private Button importPhotoButton;
    @FXML
    private TextField productIDTextField;
    @FXML
    private Label noDataProvidedLabel;

    public void addEquipment(){ //method usage in fxml file
        AddEquipmentRequest add = new AddEquipmentRequest();
        try {
            if(typeTextField.getText().isEmpty() || producerTextField.getText().isEmpty() || modelTextField.getText().isEmpty() || productIDTextField.getText().isEmpty()){
                noDataProvidedLabel.setText("No data provided");
                return;
            }
            else{
                add.equipment(typeTextField.getText(), producerTextField.getText(), modelTextField.getText(), productIDTextField.getText());//adding new equpment
                noDataProvidedLabel.setText("");    //just to "no data provided text disapear
            }
        }
        catch (Exception e){
            e.printStackTrace();
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(event -> addEquipment());

        backButton.setOnAction(event -> backToMenu());
    }

    @Override
    public void backToMenu() {
        new MainPanelController();
    }
}
