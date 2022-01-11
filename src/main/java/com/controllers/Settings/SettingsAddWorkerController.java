package com.controllers.Settings;

import com.Main;
import com.api.*;
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
    private Button updateButton;
    @FXML
    private Button deleteButton;
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

    private String userDoubleClicked = new String();

    @Override
    public void back() {
        new SettingsController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        backButton.setOnAction(event -> back());
        addButton.setOnAction(event -> addNewWorker());

        workersTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!workersTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

        updateButton.setOnAction(event -> updateExistingWorker());

        deleteButton.setOnAction(event -> deleteExistingWorker());

        setDisableTrue();

    }

    @Override
    public void fullTableView() {
        ArrayList<Document> tempEmployee = new ArrayList<>();
        tempEmployee = getCollection("employee");

        ObservableList<Worker> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < tempEmployee.size(); i++) {

            observableList.add(new Worker(
                    Crypt.decrypt(Crypt.password, tempEmployee.get(i).get("user").toString()),
                    Crypt.decrypt(Crypt.password, tempEmployee.get(i).get("name").toString()),
                    Crypt.decrypt(Crypt.password, tempEmployee.get(i).get("surname").toString())
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
        Worker worker = workersTableView.getSelectionModel().getSelectedItem();
        if (workersTableView.getSelectionModel().isEmpty()) {
            return;
        } else {
            clearTextFields();
            userTextField.setText(worker.getUser());
            nameTextField.setText(worker.getName());
            surnameTextField.setText(worker.getSurname());

            userDoubleClicked = worker.getUser();

            setDisableFalse();
        }
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
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @Override
    public void setDisableFalse() {
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
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

    private void updateExistingWorker(){
        if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || userTextField.getText().isEmpty() || passwordPasswordField.getText().isEmpty() || confirmPasswordPasswordField.getText().isEmpty()) {
            noDataProvidedLabel.setText("No data provided");
            return;
        }
        if (MongoRequests.checkObjectFilter("employee", "user", userTextField.getText().toString(), userDoubleClicked)) { //check if there is no same user name
            if(passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText())) {    //check if the passwords are the same
                if (MongoRequests.getEmployee(userDoubleClicked, passwordPasswordField.getText())) {  //check if the password is correct in data base
                    if(userDoubleClicked.equals("admin")){  //if the editing worker is admin if not, update worker
                        if(userDoubleClicked.equals(userTextField.getText())){  //check if someone is editing user name of admin
                            System.out.println("im editing user name of admin");
                            MongoRequests.updateEmployee(userTextField.getText(), passwordPasswordField.getText(), nameTextField.getText(), surnameTextField.getText(), userDoubleClicked);
                            noDataProvidedLabel.setText("");
                            fullTableView();
                            clearTextFields();
                            setDisableTrue();
                            return;
                        }else{
                            noDataProvidedLabel.setText("user name admin, can not be changed");
                            return;
                        }
                    }else if(!userTextField.getText().equals("admin")){ //changed user name can not be admin
                        System.out.println("im not editing user name of admin ");
                        MongoRequests.updateEmployee(userTextField.getText(), passwordPasswordField.getText(), nameTextField.getText(), surnameTextField.getText(), userDoubleClicked);
                        noDataProvidedLabel.setText("");
                        fullTableView();
                        clearTextFields();
                        setDisableTrue();
                        return;
                    }
                }else{
                    noDataProvidedLabel.setText("wrong password to user");
                    return;
                }
            }else {
                noDataProvidedLabel.setText("Wrong confirm password");
                return;
            }
        }else {
            noDataProvidedLabel.setText("Same user detected");
            return;
        }
    }

    private void deleteExistingWorker(){
        if (workersTableView.getSelectionModel().isEmpty()) {
            noDataProvidedLabel.setText("select row which you want to delete");
            return;
        } else noDataProvidedLabel.setText("");

        if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || userTextField.getText().isEmpty() || passwordPasswordField.getText().isEmpty() || confirmPasswordPasswordField.getText().isEmpty()) {
            noDataProvidedLabel.setText("No data provided");
            return;
        }

        Worker worker = workersTableView.getSelectionModel().getSelectedItem();

        if(userDoubleClicked.equals("admin")){
            noDataProvidedLabel.setText("Admin cant be deleted");
            return;
        }
        if(passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText())) {   //if the password and confirm password is not the same
            if (getEmployee(userDoubleClicked, passwordPasswordField.getText())) {
                MongoRequests.deleteEmployee(worker.getUser());
                fullTableView();    //to refresh table view
                clearTextFields();
                setDisableTrue();
            }else {
                noDataProvidedLabel.setText("password incorrect");
                return;
            }
        }else {
                noDataProvidedLabel.setText("passwords are not the same");
            }

        }

    }



