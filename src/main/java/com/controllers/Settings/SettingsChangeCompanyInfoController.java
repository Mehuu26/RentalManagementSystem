package com.controllers.Settings;

import com.Main;
import com.api.GoBack;
import com.mongodb.Mongo;
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

public class SettingsChangeCompanyInfoController extends MongoRequests implements Initializable, GoBack {
    public SettingsChangeCompanyInfoController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/settingsChangeCompanyInfo.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button backButton;
    @FXML
    private Button updateButton;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField closeTimeTextField;
    @FXML
    private TextField openTimeTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField percentageTextField;
    @FXML
    private Label noDataProvidedLabel;

    private String _id = new String();

    @Override
    public void back() {
        new SettingsController();
    }

    private boolean isNumeric(String string) {
        int tempInt;

        try {
            tempInt = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("string can't be parsed to integer");
        }
        return false;
    }

    private void update(){
        if(phoneTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || titleTextField.getText().isEmpty() || closeTimeTextField.getText().isEmpty() || openTimeTextField.getText().isEmpty() || addressTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("Enter value");
            return;
        }
        try{
        if(!isNumeric(percentageTextField.getText())){  //check if the percentage is numeric
            noDataProvidedLabel.setText("percentage value need to be numeric");
            return;
        }else if(!isNumeric(closeTimeTextField.getText())) {  //check if the percentage is numeric
            noDataProvidedLabel.setText("close time value need to be numeric");
            return;
        }else if(!isNumeric(openTimeTextField.getText())) {  //check if the percentage is numeric
            noDataProvidedLabel.setText("open time value need to be numeric");
            return;
        }else if(!isNumeric(phoneTextField.getText())) {  //check if the percentage is numeric
            noDataProvidedLabel.setText("phone value need to be numeric");
            return;
        } else if(Double.valueOf(openTimeTextField.getText()) > Double.valueOf(closeTimeTextField.getText())) {  //check if the percentage is numeric
            noDataProvidedLabel.setText("open time can't be set after close time value need to be numeric");
            return;
        }else {
            noDataProvidedLabel.setText("");
            MongoRequests.updateCompanyInfo(_id,
                    phoneTextField.getText(),
                    emailTextField.getText(),
                    titleTextField.getText(),
                    closeTimeTextField.getText(),
                    openTimeTextField.getText(),
                    addressTextField.getText(),
                    percentageTextField.getText()
            );
            setTextFields();
        }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setTextFields(){
        Document document = MongoRequests.getObjectFilter("company", "", "");   //get object from db
        //System.out.println(document);
        _id = document.get("_id").toString();   //temporary _id, need to found updated document in db

        phoneTextField.setText(document.get("phone").toString());
        emailTextField.setText(document.get("email").toString());
        titleTextField.setText(document.get("title").toString());
        closeTimeTextField.setText(document.get("close").toString());
        openTimeTextField.setText(document.get("open").toString());
        addressTextField.setText(document.get("address").toString());
        percentageTextField.setText(document.get("percentage").toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFields();
        backButton.setOnAction(event -> back());
        updateButton.setOnAction(event -> update());
    }
}
