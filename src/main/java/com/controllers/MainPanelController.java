package com.controllers;

import com.Main;
import com.controllers.Settings.SettingsController;
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
    @FXML
    private Button settingsButton;
    @FXML
    private Button clientsButton;
    @FXML
    private Button reservationButton;
    @FXML
    private Button rentReservatedButton;
    @FXML
    private Button rentButton;

    private void addEquipment(){
        new AddEquipmentController();
    }

    private void settings(){
        new SettingsController();
    }

    private void clients(){
        new ClientsController();
    }

    private void reservation(){
        new ReservationController();
    }

    private void rentReservated(){
        new RentReservatedController();
    }

    private void rent(){
        new RentController();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addEquipmentButton.setOnAction(event -> addEquipment());
        settingsButton.setOnAction(event -> settings());
        clientsButton.setOnAction(event -> clients());
        reservationButton.setOnAction(event -> reservation());
        rentReservatedButton.setOnAction(event -> rentReservated());
        rentButton.setOnAction(event -> rent());
    }
}
