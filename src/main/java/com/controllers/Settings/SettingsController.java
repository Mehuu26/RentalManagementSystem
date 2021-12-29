package com.controllers.Settings;

import com.Main;
import com.api.GoBack;
import com.controllers.MainPanelController;
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

    @Override
    public void back() {
        new MainPanelController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        addTypeButton.setOnAction(event -> addType());
        addNewWorkerButton.setOnAction(event -> addWorker());
    }

    private void addType() {
        new SettingsAddTypeAndPriceListController();
    }

    private void addWorker() {
        new SettingsAddWorkerController();
    }


}
