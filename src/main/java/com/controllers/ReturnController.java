package com.controllers;

import com.Main;
import com.api.FullTableView;
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
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReturnController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public ReturnController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/return.fxml"));
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
    private Button returnButton;
    @FXML
    private TextField productIdTextField;
    @FXML
    private TextField chooseClientTextField;
    @FXML
    private Label noDataProvidedLabel;

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

    private void returnEquipment() {
        if(productIdTextField.getText().isEmpty()){
            noDataProvidedLabel.setText("enter product Id");
            return;
        }else {
            Document tempDocument = MongoRequests.getObjectFilter("rentals", "productId", productIdTextField.getText());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        returnButton.setOnAction(event -> returnEquipment());
    }
}
