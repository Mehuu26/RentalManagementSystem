package com.controllers;

import com.Main;
import com.api.GoBack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, GoBack {
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

    @Override
    public void back() {
        new MainPanelController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        addTypeButton.setOnAction(event -> addType());
    }

    public void addType(){
        new SettingsAddTypeAndPriceListController();
    }


}
