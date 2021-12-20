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
    private TextField typeTextField;
    @FXML
    private TextField producerTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField sizeTextField;
    @FXML
    private TextField productIDTextField;
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



    public void addEquipment(){ //method usage in fxml file
        //AddEquipmentRequest add = new AddEquipmentRequest();
        try {
            if(typeTextField.getText().isEmpty() || producerTextField.getText().isEmpty() || modelTextField.getText().isEmpty() || sizeTextField.getText().isEmpty() || productIDTextField.getText().isEmpty()){
                noDataProvidedLabel.setText("No data provided");
                return;
            }
            else{
                addEquipment(typeTextField.getText(), producerTextField.getText(), modelTextField.getText(), sizeTextField.getText(), productIDTextField.getText());//adding new equpment
                noDataProvidedLabel.setText("");    //just to "no data provided" text disapear
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        fullTableView();



        addButton.setOnAction(event -> addEquipment());

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
