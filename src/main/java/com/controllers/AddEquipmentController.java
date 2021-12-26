package com.controllers;

import com.Main;
import com.api.Equipment;
import com.api.FullTableView;
import com.api.GoBack;
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
    private TextField typeTextField;
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

    private String productIdDoubleClicked = new String(); //temporary string just to store productId


    public void addNewEquipment(String type, String producer, String model, String size, String productId) { //method usage in fxml file
        //AddEquipmentRequest add = new AddEquipmentRequest();
        try {
            if (typeTextField.getText().isEmpty() || producerTextField.getText().isEmpty() || modelTextField.getText().isEmpty() || sizeTextField.getText().isEmpty() || productIdTextField.getText().isEmpty()) {
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

        //in case there's no value in text fields.
        if (typeTextField.getText().isEmpty()) typeTextField.setText(equipment.getType());
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
        addSimilarButton.setOnAction(event -> addSimilarEquipment());

        addButton.setOnAction(event -> addNewEquipment(typeTextField.getText().toString(), producerTextField.getText().toString(), modelTextField.getText().toString(), sizeTextField.getText().toString(), productIdTextField.getText().toString()));
        backButton.setOnAction(event -> back());

        equipmentTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!equipmentTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

        //equipmentTableView.setOnMouseClicked(event -> clearTextFieldsWhenClicked()); //if we want to clear text fields

        updateButton.setOnAction(event -> updateExistingEquipment());

        updateButton.setTooltip(new Tooltip("Double click on equipment which you want to change, then put new values in"));

        deleteButton.setOnAction(event -> deleteExistingEquipment());

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
        if (typeTextField.getText().isEmpty() || producerTextField.getText().isEmpty() || modelTextField.getText().isEmpty() || sizeTextField.getText().isEmpty() || productIdTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("No data provided");
            return;
        } else {
            updateEquipment(typeTextField.getText(), producerTextField.getText(), modelTextField.getText(), sizeTextField.getText(), productIdDoubleClicked, productIdTextField.getText()); //2 product id's becouse one is old and second one is new
        }

        fullTableView();


    }
    @Override
    public void tableViewDoubleClicked() {
        Equipment equipment = equipmentTableView.getSelectionModel().getSelectedItem();
        if (equipmentTableView.getSelectionModel().isEmpty()) {
            return;
        } else {
            typeTextField.setText(equipment.getType());
            producerTextField.setText(equipment.getProducer());
            modelTextField.setText(equipment.getModel());
            sizeTextField.setText(equipment.getSize());
            productIdTextField.setText(equipment.getProductId());

            productIdDoubleClicked = productIdTextField.getText();
        }
    }

    private void clearTextFieldsWhenClicked() {
        typeTextField.clear();
        producerTextField.clear();
        modelTextField.clear();
        sizeTextField.clear();
        productIdTextField.clear();
    }

    private void deleteExistingEquipment() {
        if (equipmentTableView.getSelectionModel().isEmpty()) {
            noDataProvidedLabel.setText("select row which you want to delete");
            return;
        } else noDataProvidedLabel.setText("");

        Equipment equipment = equipmentTableView.getSelectionModel().getSelectedItem();

        deleteEquipment(equipment.getProductId());

        fullTableView();    //to refresh table view

    }
}


