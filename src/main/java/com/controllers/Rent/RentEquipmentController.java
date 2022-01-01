package com.controllers.Rent;

import com.Main;
import com.api.Client;
import com.api.FullTableView;
import com.api.GoBack;
import com.api.Rental;
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
import java.util.function.ToDoubleBiFunction;

public class RentEquipmentController extends MongoRequests implements GoBack, Initializable, FullTableView {
    public RentEquipmentController(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/rentEquipment.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.client=client;
        clientLabel.setText(client.getName() + " " + client.getSurname() + " " + client.getIdCard());
        this.clientId = client.get_id();
    }

    private Client client;
    private String clientId;
    private String clientName = new String();


    @FXML
    private Button backButton;
    @FXML
    private Button rentButton;
    @FXML
    private Label clientLabel;
    @FXML
    private Label noDataProvidedLabel;
    @FXML
    private TextField productIdTextField;
    @FXML
    private TableView<Rental> rentalTableView;
    @FXML
    private TableColumn<Rental, String> typeTableColumn;
    @FXML
    private TableColumn<Rental, String> modelTableColumn;
    @FXML
    private TableColumn<Rental, String> sizeTableColumn;
    @FXML
    private TableColumn<Rental, String> productIdTableColumn;
    @FXML
    private TableColumn<Rental, String> startDateTableColumn;

    // TODO: 02.01.2022 check why I can not use client in this method 
    @Override
    public void fullTableView() {
        ArrayList<Document> tempRentalList = new ArrayList<>();
        ArrayList<Document> tempEquipmentList = new ArrayList<>();

        tempRentalList = getCollection("rentals");
        tempEquipmentList = getCollection("items");

        Document tempRental = new Document();
        Document tempEquipment = new Document();

        ObservableList<Rental> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < tempRentalList.size(); i++) {   //first looking for match between rental object and user id, which we get from constructor
            if (clientId.equals(tempRentalList.get(i).get("userId"))) {//if match found
                System.out.println("znalazlem pare z user id");
                //tempRental = tempRentalList.get(i);//saving temporary file
                for(int h = 0; h<tempEquipmentList.size(); h++){    //looking for product id match between rental and equipment list
                    if(tempRentalList.get(i).get("productId").equals(tempEquipmentList.get(h).get("productId"))){   //if match found
                        System.out.println("znalazlem pare z product id");
                        //tempEquipment = tempEquipmentList.get(i);   //saving
                        observableList.add(new Rental(tempEquipmentList.get(h).get("type").toString(),
                                tempEquipmentList.get(h).get("model").toString(),
                                tempEquipmentList.get(h).get("size").toString(),
                                tempEquipmentList.get(h).get("productId").toString(),
                                tempRentalList.get(i).get("userId").toString(),
                                tempRentalList.get(i).get("startDate").toString(),
                                tempRentalList.get(i).get("status").toString()
                                ));
                    }
                }
            }
        }

        typeTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("type"));
        modelTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("model"));
        sizeTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("size"));
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("productId"));
        startDateTableColumn.setCellValueFactory(new PropertyValueFactory<Rental, String>("startDate"));

        typeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sizeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productIdTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        startDateTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        rentalTableView.setItems(observableList);
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

    // TODO: 02.01.2022 when done with client problem, make add new rental
    @Override
    public void back() {
        new RentChooseClientController();
    }

    private void rent(){
        if(productIdTextField.getText().isEmpty()){
            noDataProvidedLabel.setText("enter product Id");
            return;
        }

        if(MongoRequests.checkObjectFileterExists("items", "productId", productIdTextField.getText())){
            noDataProvidedLabel.setText("");
            //add new rental

            fullTableView();
        }else{
            noDataProvidedLabel.setText("no product detected");
            return;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        backButton.setOnAction(event -> back());
    }
}
