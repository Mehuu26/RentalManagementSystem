package com.controllers;

import com.Main;
import com.api.Client;
import com.api.FullTableView;
import com.api.GoBack;
import com.api.Rental;
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
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public ReturnController(String value){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/return.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        productIdTextField.setText(value);
        returnEquipment();
    }

    private Client client;
    private ArrayList<Document> returnArrayList = new ArrayList<>();

    @FXML
    private Button backButton;
    @FXML
    private Button returnButton;
    @FXML
    private TextField productIdTextField;
    @FXML
    private Label noDataProvidedLabel;
    @FXML
    private Label currentClientLabel;
    @FXML
    private TableView<Rental> rentalTableView;
    @FXML
    private TableColumn<Rental, String> rentalTypeTableColumn;
    @FXML
    private TableColumn<Rental, String> rentalModelTableColumn;
    @FXML
    private TableColumn<Rental, String> rentalSizeTableColumn;
    @FXML
    private TableColumn<Rental, String> rentalProductIdTableColumn;
    @FXML
    private TableColumn<Rental, String> rentalStartDateTableColumn;

    @FXML
    private TableView<Rental> returnTableView;
    @FXML
    private TableColumn<Rental, String> returnTypeTableColumn;
    @FXML
    private TableColumn<Rental, String> returnModelTableColumn;
    @FXML
    private TableColumn<Rental, String> returnSizeTableColumn;
    @FXML
    private TableColumn<Rental, String> returnProductIdTableColumn;
    @FXML
    private TableColumn<Rental, String> returnStartDateTableColumn;
    @FXML
    private TableColumn<Rental, String> returnFinishDateTableColumn;

    @Override
    public void fullTableView() {
        ArrayList<Document> tempRentalList = new ArrayList<>();
        ArrayList<Document> tempEquipmentList = new ArrayList<>();

        tempRentalList = getCollectionFilter("rentals", "userId", client.get_id());
        tempEquipmentList = getCollection("items");

        Document tempRental = new Document();
        Document tempEquipment = new Document();

        ObservableList<Rental> observableList = FXCollections.observableArrayList();

        Long milliSeconds;
        Calendar calendar = Calendar.getInstance();
        String stringTime;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for(int i = 0; i<tempRentalList.size(); i++) {  //check tempRentalList
            if(tempRentalList.get(i).get("status").equals("true")) {    //check if status is true (is rented)
                for (int h = 0; h < tempEquipmentList.size(); h++) {    //check tempEquipmentList for
                    if (tempEquipmentList.get(h).get("productId").equals(tempRentalList.get(i).get("productId"))) {   //if found equipment from rental list
                        //parsing milliseconds into date format
                        milliSeconds = Long.parseLong(tempRentalList.get(i).get("startDate").toString());
                        calendar.setTimeInMillis(milliSeconds);
                        stringTime = formatter.format(calendar.getTime());

                        //adding new objects into observable list
                        observableList.add(new Rental(tempEquipmentList.get(h).get("type").toString(),
                                tempEquipmentList.get(h).get("model").toString(),
                                tempEquipmentList.get(h).get("size").toString(),
                                tempEquipmentList.get(h).get("productId").toString(),
                                tempRentalList.get(i).get("userId").toString(),
                                stringTime,
                                tempRentalList.get(i).get("status").toString(),
                                tempRentalList.get(i).get("_id").toString()
                        ));
                        break;
                    }
                }
            }else continue;

        }

        rentalTypeTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("type"));
        rentalModelTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("model"));
        rentalSizeTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("size"));
        rentalProductIdTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("productId"));
        rentalStartDateTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("startDate"));

        rentalTypeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        rentalModelTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        rentalSizeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        rentalProductIdTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        rentalStartDateTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        rentalTableView.setItems(observableList);
    }

    private void fullReturnTableView(Document tempRental) {  //value from text field. Store equipment we are getting
        ArrayList<Document> tempEquipmentList = getCollection("items");

        ObservableList<Rental> observableList = FXCollections.observableArrayList();
        returnArrayList.add(tempRental);

        Long milliSeconds;
        Calendar calendar = Calendar.getInstance();
        String stringTime;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        System.out.println("wielkosc return array list to: " + returnArrayList.size());
        System.out.println(returnArrayList.get(0));
            for (int i = 0; i < returnArrayList.size(); i++) {
                for(int h = 0; h<tempEquipmentList.size(); h++){
                    if(tempEquipmentList.get(h).get("productId").equals(returnArrayList.get(i).get("productId"))){
                        //parsing milliseconds into date format
                        milliSeconds = Long.parseLong(returnArrayList.get(i).get("startDate").toString());
                        calendar.setTimeInMillis(milliSeconds);
                        stringTime = formatter.format(calendar.getTime());

                        observableList.add(new Rental(tempEquipmentList.get(h).get("type").toString(),
                                tempEquipmentList.get(h).get("model").toString(),
                                tempEquipmentList.get(h).get("size").toString(),
                                tempEquipmentList.get(h).get("productId").toString(),
                                returnArrayList.get(i).get("userId").toString(),    //user id from rental collection
                                stringTime,
                                returnArrayList.get(i).get("status").toString(),    //from rental collection
                                returnArrayList.get(i).get("_id").toString()    //from rental collection
                        ));

                        break;
                    }
                }

            }

            returnTypeTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("type"));
            returnModelTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("model"));
            returnSizeTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("size"));
            returnProductIdTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("productId"));
            returnStartDateTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("startDate"));
            returnFinishDateTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("finishDate"));

            returnTypeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            returnModelTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            returnSizeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            returnProductIdTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            returnStartDateTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            returnFinishDateTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            returnTableView.setItems(observableList);
        }


    @Override
    public void tableViewDoubleClicked() {
        Rental rental = rentalTableView.getSelectionModel().getSelectedItem();  //get rental object
        Document tempDocument = MongoRequests.getObjectFilter("rentals", "productId", rental.getProductId());   //found rental object in database

        rentalTableView.getItems().remove(rental);
        fullReturnTableView(tempDocument);  //add it to return table view
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
        // TODO: 04.01.2022 check tempDocument is returning rental but not checking if it is rented or returned 
        Document tempDocument = MongoRequests.getObjectFilter("rentals", "productId", productIdTextField.getText());
        Document tempEquipment = MongoRequests.getObjectFilter("items", "productId", productIdTextField.getText());

        if (productIdTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("enter product Id");
            return;
        } else if (tempDocument == null) {
            noDataProvidedLabel.setText("this product is not rented");
            return;
        } else if (client == null) {        //first usage
            Document tempClient = MongoRequests.getObjectFilterById("users", "_id", tempDocument.get("userId").toString());

            System.out.println(tempDocument.get("userId"));
            System.out.println("temp client form return equipment: " + tempClient);

            System.out.println("temp clinet get name: " + tempClient.get("name"));

            client = new Client(tempClient.get("name").toString(),
                    tempClient.get("surname").toString(),
                    tempClient.get("phone").toString(),
                    tempClient.get("idCard").toString(),
                    tempClient.get("_id").toString());

            currentClientLabel.setText(client.getName() + " " + client.getSurname() + " " + client.getIdCard());

            fullReturnTableView(tempDocument);
            fullTableView();

            for(int i = 0; i<rentalTableView.getItems().size(); i++){
                System.out.println("im in loop seariching for item to remove");
                if(rentalTableView.getItems().get(i).getProductId().equals(productIdTextField.getText())){
                    System.out.println("I found object to remove");
                    rentalTableView.getItems().remove(i);
                }
            }


        }else if(client.get_id().equals(tempDocument.get("userId"))) {
            for(int i = 0; i<returnArrayList.size(); i++){
                if(returnArrayList.get(i).get("productId").equals(productIdTextField.getText())){
                    noDataProvidedLabel.setText("this product has been already returned");
                    return;
                }
            }
            for(int i = 0; i<rentalTableView.getItems().size(); i++) {
                System.out.println("im in loop seariching for item to remove");
                if (rentalTableView.getItems().get(i).getProductId().equals(productIdTextField.getText())) {
                    System.out.println("I found object to remove");
                    rentalTableView.getItems().remove(i);
                }
            }

            fullReturnTableView(tempDocument);  //delete value from rental table view and add same to return table view
            noDataProvidedLabel.setText("");
        }else{
            new ReturnController(productIdTextField.getText());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        returnButton.setOnAction(event -> returnEquipment());

        rentalTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!rentalTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });
    }
}
