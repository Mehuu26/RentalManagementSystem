package com.controllers.Settings;

import com.Main;
import com.api.GoBack;
import com.controllers.MainPanelController;
import com.mongodb.Mongo;
import com.requests.MongoRequests;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsController extends MongoRequests implements Initializable, GoBack {
    public SettingsController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/settings.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Button backButton;
    @FXML
    private Button addTypeButton;
    @FXML
    private Button addNewWorkerButton;
    @FXML
    private Button deleteUsersButton;
    @FXML
    private Button deleteEquipmentButton;
    @FXML
    private Button changeCompanyInfoButton;

    @Override
    public void back() {
        new MainPanelController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        addTypeButton.setOnAction(event -> addType());
        addNewWorkerButton.setOnAction(event -> addWorker());
        deleteUsersButton.setOnAction(event -> deleteUsers());
        deleteEquipmentButton.setOnAction(event -> deleteEquipment());
        changeCompanyInfoButton.setOnAction(event -> changeCompanyInfo());
    }

    private void addType() {
        new SettingsAddTypeAndPriceListController();
    }

    private void addWorker() {
        new SettingsAddWorkerController();
    }

    private void deleteUsers(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm edit operation");
        alert.setHeaderText(null);
        alert.setContentText("By clicking 'OK' you will delete every user and reservation in database");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) { //if user press ok
            MongoRequests.deleteEveryObject("users");
            MongoRequests.deleteEveryObject("reservations");
        }
    }

    private void deleteEquipment(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm edit operation");
        alert.setHeaderText(null);
        alert.setContentText("By clicking 'OK' you will delete every equipment and reservation in database");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) { //if user press ok
            MongoRequests.deleteEveryObject("items");
            MongoRequests.deleteEveryObject("reservations");
        }
    }

    private void changeCompanyInfo(){
        new SettingsChangeCompanyInfoController();
    }
}

