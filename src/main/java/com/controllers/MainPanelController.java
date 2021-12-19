package com.controllers;

import com.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {
    public MainPanelController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/mainPanel.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private Button addEquipmentButton;

    public void addEquipment(){
        new AddEquipmentController();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addEquipmentButton.setOnAction(event -> addEquipment());
    }
}
