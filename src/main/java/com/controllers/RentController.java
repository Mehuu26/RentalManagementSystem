package com.controllers;

import com.Main;
import com.api.FullTableView;
import com.api.GoBack;
import com.mongodb.Mongo;
import com.requests.MongoRequests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RentController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public RentController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/rent.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private Button backButton;

    @Override
    public void fullTableView() {

    }

    @Override
    public void tableViewDoubleClicked() {

    }

    @Override
    public void clearTextFields() {

    }

    @Override
    public void setDisableTrue() {

    }

    @Override
    public void setDisableFalse() {

    }

    @Override
    public void back() {
        new MainPanelController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());

    }
}
