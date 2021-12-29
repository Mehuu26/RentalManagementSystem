package com.controllers.Settings;

import com.Main;
import com.api.FullTableView;
import com.api.GoBack;
import com.api.Worker;
import com.requests.MongoRequests;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SettingsAddWorkerController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public SettingsAddWorkerController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/settingsAddWorker.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button backButton;
    @FXML
    private Button addButton;
    @FXML
    private TableView<Worker> workersTableView;
    @FXML
    private TableColumn<Worker, String> userTableColumn;
    @FXML
    private TableColumn<Worker, String> nameTableColumn;
    @FXML
    private TableColumn<Worker, String> surnameTableColumn;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField userTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private PasswordField confirmPasswordPasswordField;
    @FXML
    private Label noDataProvidedLabel;


    @Override
    public void back() {
        new SettingsController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        backButton.setOnAction(event -> back());
        addButton.setOnAction(event -> addNewWorker());
    }

    @Override
    public void fullTableView() {
        ArrayList<Document> tempEmployee = new ArrayList<>();
        tempEmployee = getCollection("employee");

        ObservableList<Worker> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < tempEmployee.size(); i++) {

            observableList.add(new Worker(
                    tempEmployee.get(i).get("user").toString(),
                    tempEmployee.get(i).get("name").toString(),
                    tempEmployee.get(i).get("surname").toString()
                    )
            );
        }
        //new Equipment("type", "producer", "model", "size", "productId")

        userTableColumn.setCellValueFactory(new PropertyValueFactory<Worker, String>("user"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Worker, String>("name"));
        surnameTableColumn.setCellValueFactory(new PropertyValueFactory<Worker, String>("surname"));

        userTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        workersTableView.setItems(observableList);
    }


    @Override
    public void tableViewDoubleClicked() {
    }

    @Override
    public void clearTextFields() {
        nameTextField.clear();
        surnameTextField.clear();
        userTextField.clear();
        passwordPasswordField.clear();
        confirmPasswordPasswordField.clear();
    }

    @Override
    public void setDisableTrue() {

    }

    @Override
    public void setDisableFalse() {
    }

    private void addNewWorker() {
        try {
            if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || userTextField.getText().isEmpty() || passwordPasswordField.getText().isEmpty() || confirmPasswordPasswordField.getText().isEmpty()) {
                noDataProvidedLabel.setText("No data provided");
                return;
            } else if(passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText())){
                if(addEmployee(userTextField.getText(), passwordPasswordField.getText(), nameTextField.getText(), surnameTextField.getText())){
                    noDataProvidedLabel.setText("");
                    fullTableView();
                    clearTextFields();
                }else{
                    noDataProvidedLabel.setText("Same user name detected");
                }
            }else{
                noDataProvidedLabel.setText("Wrong confirmed password");
            }
        } catch (Exception e) {
        }
    }
}


