package com.controllers;

import com.Main;
import com.api.Equipment;
import com.api.FullTableView;
import com.api.GoBack;
import com.mongodb.Mongo;
import com.requests.MongoRequests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddEquipmentController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public AddEquipmentController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/addEquipmentPanel.fxml"));
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
    private Button addSimilarButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField producerTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField sizeTextField;
    @FXML
    private TextField productIdTextField;
    @FXML
    private Label noDataProvidedLabel;
    @FXML
    private TableView<Equipment> equipmentTableView;
    @FXML
    private TableColumn<Equipment, String> typeTableColumn;
    @FXML
    private TableColumn<Equipment, String> producerTableColumn;
    @FXML
    private TableColumn<Equipment, String> modelTableColumn;
    @FXML
    private TableColumn<Equipment, String> sizeTableColumn;
    @FXML
    private TableColumn<Equipment, String> productIdTableColumn;
    @FXML
    private ComboBox<String> typeComboBox;

    private String productIdDoubleClicked = new String(); //temporary string just to store productId


    public void addNewEquipment(String type, String producer, String model, String size, String productId) { //method usage in fxml file
        try {
            if (typeComboBox.getSelectionModel().isEmpty() || producerTextField.getText().isEmpty() || modelTextField.getText().isEmpty() || sizeTextField.getText().isEmpty() || productIdTextField.getText().isEmpty()) {
                noDataProvidedLabel.setText("No data provided");
                return;
            } else {
                if (addEquipment(type, producer, model, size, productId)) {    //adding new equpment
                    noDataProvidedLabel.setText("");    //just to "no data provided" text disapear
                    productIdTextField.clear(); //just to clear the productId text field
                    fullTableView();
                } else {
                    noDataProvidedLabel.setText("Same productId detected");
                    return;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSimilarEquipment() {
        //System.out.println("jestem w addSimi"); //just to check
        if (equipmentTableView.getSelectionModel().isEmpty()) {
            noDataProvidedLabel.setText("select row which you want to add similar");
            return;
        } else noDataProvidedLabel.setText("");

        Equipment equipment = equipmentTableView.getSelectionModel().getSelectedItem();
        String tempString;

        //strings used only for terminal show

        //System.out.println(equipment.getType());
        //System.out.println(equipment.getProducer());
        //System.out.println(equipment.getModel());
        //System.out.println(equipment.getSize());
        //System.out.println(equipment.getProductId());


        //checking if comboBox is empty, if yes put new value in
        boolean isMyComboBoxEmpty = typeComboBox.getSelectionModel().isEmpty();

        if(isMyComboBoxEmpty) {
            typeComboBox.setValue(equipment.getType().toString());
            System.out.println("get Type = "+ equipment.getType());
        }

        //in case there's no value in text fields.
        //if (typeComboBox.getSelectionModel().getSelectedItem().isEmpty()) typeComboBox.setValue(equipment.getType());
        if (producerTextField.getText().isEmpty()) producerTextField.setText(equipment.getProducer());
        if (modelTextField.getText().isEmpty()) modelTextField.setText(equipment.getModel());
        if (sizeTextField.getText().isEmpty()) sizeTextField.setText(equipment.getSize());
        //if(productIdTextField.getText().isEmpty())   productIdTextField.setText(equipment.getProductId());


        //tempString = productIdTextField.getText().toString();

        //check if there is new product ID
        if (productIdTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("Enter new product ID");
            return;
        } else {
            addNewEquipment(equipment.getType(), equipment.getProducer(), equipment.getModel(), equipment.getSize(), productIdTextField.getText());
        }
    }

    //add similar button add new if there's no product id value
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        fillTypeComboBox();
        addSimilarButton.setOnAction(event -> addSimilarEquipment());

        addButton.setOnAction(event -> {
            if (!typeComboBox.getSelectionModel().isEmpty())    //checking if the combo box with value is empty
                addNewEquipment(typeComboBox.getSelectionModel().getSelectedItem().toString(), producerTextField.getText().toString(), modelTextField.getText().toString(), sizeTextField.getText().toString(), productIdTextField.getText().toString());
            else{noDataProvidedLabel.setText("Choose type value");}
        });
        backButton.setOnAction(event -> back());

        equipmentTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!equipmentTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

        //equipmentTableView.setOnMouseClicked(event -> clearTextFieldsWhenClicked()); //if we want to clear text fields

        updateButton.setOnAction(event -> updateExistingEquipment());

        updateButton.setTooltip(new Tooltip("Double click on equipment which you want to change, then put new values in"));

        deleteButton.setOnAction(event -> deleteExistingEquipment());

        setDisableTrue();

    }

    @Override
    public void back() {
        new MainPanelController();
    }

    @Override
    public void fullTableView() {
        ArrayList<Document> tempEquipmentArrayList = new ArrayList<>();
        tempEquipmentArrayList = getCollection("items");

        ObservableList<Equipment> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < tempEquipmentArrayList.size(); i++) {
//            Equipment tempEquipment = new Equipment(
//                    tempEquipmentArrayList.get(i).get("type").toString(),
//                    tempEquipmentArrayList.get(i).get("producer").toString(),
//                    tempEquipmentArrayList.get(i).get("model").toString(),
//                    tempEquipmentArrayList.get(i).get("size").toString(),
//                    tempEquipmentArrayList.get(i).get("productId").toString()
//            );
            //System.out.println("im adding list number"+i);    //just to show in terminal

            //System.out.println(tempEquipmentArrayList.get(i).get("type").toString()); //just to show in termianl

            observableList.add(new Equipment(
                            tempEquipmentArrayList.get(i).get("type").toString(),
                            tempEquipmentArrayList.get(i).get("producer").toString(),
                            tempEquipmentArrayList.get(i).get("model").toString(),
                            tempEquipmentArrayList.get(i).get("size").toString(),
                            tempEquipmentArrayList.get(i).get("productId").toString()
                    )
            );
        }
        //new Equipment("type", "producer", "model", "size", "productId")

        typeTableColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("type"));
        producerTableColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("producer"));
        modelTableColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("model"));
        sizeTableColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("size"));
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("productId"));

        typeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        producerTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sizeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productIdTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        equipmentTableView.setItems(observableList);
    }

    private void updateExistingEquipment() {
        if (typeComboBox.getSelectionModel().isEmpty() || producerTextField.getText().isEmpty() || modelTextField.getText().isEmpty() || sizeTextField.getText().isEmpty() || productIdTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("No data provided");
            return;
        } else if(productIdDoubleClicked.equals(productIdTextField)){ //check if theres no change in product Id.
            updateEquipment(typeComboBox.getSelectionModel().getSelectedItem().toString(), producerTextField.getText(), modelTextField.getText(), sizeTextField.getText(), productIdDoubleClicked, productIdTextField.getText()); //2 product id's becouse one is old and second one is new
            noDataProvidedLabel.setText("");
        }else if(checkObjectFilter("items", "productId", productIdTextField.getText(), productIdDoubleClicked)){    //check if there is product with same product Id
            updateEquipment(typeComboBox.getSelectionModel().getSelectedItem().toString(), producerTextField.getText(), modelTextField.getText(), sizeTextField.getText(), productIdDoubleClicked, productIdTextField.getText());
            updateIdProductInReservation(productIdTextField.getText()); //updating product id in reservation collection
            noDataProvidedLabel.setText("");
        }else{
            noDataProvidedLabel.setText("Same productId found");
        }

        fullTableView();
        clearTextFields();

        setDisableTrue();
    }

    @Override
    public void tableViewDoubleClicked() {
        Equipment equipment = equipmentTableView.getSelectionModel().getSelectedItem();
        if (equipmentTableView.getSelectionModel().isEmpty()) {
            return;
        } else {
            typeComboBox.setValue(equipment.getType());
            producerTextField.setText(equipment.getProducer());
            modelTextField.setText(equipment.getModel());
            sizeTextField.setText(equipment.getSize());
            productIdTextField.setText(equipment.getProductId());

            productIdDoubleClicked = productIdTextField.getText();

            setDisableFalse();
        }
    }

    @Override
    public void clearTextFields() {
        typeComboBox.valueProperty().set(null);
        producerTextField.clear();
        modelTextField.clear();
        sizeTextField.clear();
        productIdTextField.clear();
    }

    @Override
    public void setDisableTrue() {
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @Override
    public void setDisableFalse() {
        deleteButton.setDisable(false);
        updateButton.setDisable(false);
    }

    // TODO: 29.12.2021 check if the equipment isn't already rented 
    private void deleteExistingEquipment() {
        if (equipmentTableView.getSelectionModel().isEmpty()) {
            noDataProvidedLabel.setText("select row which you want to delete");
            return;
        } else noDataProvidedLabel.setText("");

        Equipment equipment = equipmentTableView.getSelectionModel().getSelectedItem();

        MongoRequests.deleteEquipment(equipment.getProductId());
        MongoRequests.deleteReservations("productId", equipment.getProductId());

        fullTableView();    //to refresh table view
        clearTextFields();
        setDisableTrue();
    }

    private void fillTypeComboBox(){
        ArrayList<Document> typeArrayList= new ArrayList<>();
        typeArrayList = getCollection("prices");

        ObservableList<String> typeObservableList = FXCollections.observableArrayList();

        for(int i = 0; i<typeArrayList.size(); i++){
            typeObservableList.add(typeArrayList.get(i).get("type").toString());
        }
        typeComboBox.setItems(typeObservableList);
    }

    private void updateIdProductInReservation(String productId){
        ArrayList<Document> reservationsArrayList= new ArrayList<>();
        reservationsArrayList = getCollectionFilter("reservations", "productId", productIdDoubleClicked);

        for(int i = 0; i<reservationsArrayList.size(); i++){
                updateReservations(
                        productId,  //new product id
                        reservationsArrayList.get(i).get("userId").toString(),
                        reservationsArrayList.get(i).get("startDate").toString(),
                        reservationsArrayList.get(i).get("finishDate").toString(),
                        reservationsArrayList.get(i).get("price").toString(),
                        reservationsArrayList.get(i).get("status").toString(),
                        productIdDoubleClicked
                        );
            }
        }
    }



