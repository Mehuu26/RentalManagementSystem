package com.controllers;

import com.Main;
import com.api.Equipment;
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

public class AddEquipmentController extends MongoRequests implements Initializable, GoBack{
    public AddEquipmentController(){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/addEquipmentPanel.fxml"));
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
    private Button addButton;
    @FXML
    private Button addSimilarButton;
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



    public void addNewEquipment(String type, String producer, String model, String size, String productId){ //method usage in fxml file
        //AddEquipmentRequest add = new AddEquipmentRequest();
        try {
            if(typeTextField.getText().isEmpty() || producerTextField.getText().isEmpty() || modelTextField.getText().isEmpty() || sizeTextField.getText().isEmpty() || productIdTextField.getText().isEmpty()){
                noDataProvidedLabel.setText("No data provided");
                return;
            }
            else{
                if(addEquipment(type, producer, model, size, productId)){    //adding new equpment
                    noDataProvidedLabel.setText("");    //just to "no data provided" text disapear
                    fullTableView();
                }else {
                    noDataProvidedLabel.setText("Same productId detected");
                    return;
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // TODO: 21.12.2021 something is not yes with 109 line. Need to check 
    public void addSimilarEquipment(){
        System.out.println("jestem w addSimi");
        Equipment equipment = equipmentTableView.getSelectionModel().getSelectedItem();
        String tempString;

        //in case there's no value in text fields.
        if(typeTextField.getText().isEmpty())   typeTextField.setText(equipment.getType());
        if(producerTextField.getText().isEmpty())   producerTextField.setText(equipment.getProducer());
        if(modelTextField.getText().isEmpty())   modelTextField.setText(equipment.getModel());
        if(sizeTextField.getText().isEmpty())   sizeTextField.setText(equipment.getSize());
        //if(productIdTextField.getText().isEmpty())   productIdTextField.setText(equipment.getProductId());


        tempString = productIdTextField.getText().toString();
        //check if there is new product ID
        if(tempString.equals("")) {
            noDataProvidedLabel.setText("Enter new product ID");
            return;
        }else {
            addNewEquipment(equipment.getType(), equipment.getProducer(), equipment.getModel(), equipment.getSize(), productIdTextField.getText());
        }
    }

    //add similar button add new if there's no product id value
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        addSimilarButton.setOnAction(event -> addSimilarEquipment());
        addButton.setOnAction(event -> addNewEquipment(typeTextField.getText(), producerTextField.getText(), modelTextField.getText(), sizeTextField.getText(), productIdTextField.getText()));
        backButton.setOnAction(event -> backToMenu());
    }

    @Override
    public void backToMenu() {
        new MainPanelController();
    }

    private void fullTableView(){
        ArrayList<Document> tempEquipmentArrayList = new ArrayList<>();
        tempEquipmentArrayList = getEquipment();

        ObservableList<Equipment> observableList = FXCollections.observableArrayList();

        for(int i=0; i<tempEquipmentArrayList.size(); i++){
//            Equipment tempEquipment = new Equipment(
//                    tempEquipmentArrayList.get(i).get("type").toString(),
//                    tempEquipmentArrayList.get(i).get("producer").toString(),
//                    tempEquipmentArrayList.get(i).get("model").toString(),
//                    tempEquipmentArrayList.get(i).get("size").toString(),
//                    tempEquipmentArrayList.get(i).get("productId").toString()
//            );
            System.out.println("im adding list number"+i);

            System.out.println(tempEquipmentArrayList.get(i).get("type").toString());

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



}
