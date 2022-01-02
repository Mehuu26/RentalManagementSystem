package com.controllers.Settings;

import com.Main;
import com.api.GoBack;
import com.requests.MongoRequests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsChangeCompanyInfoController extends MongoRequests implements Initializable, GoBack {
    public SettingsChangeCompanyInfoController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/changeCompanyInfo.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button backButton;

    @Override
    public void back() {
        new SettingsController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
    }
}
