package com.controllers;

import com.Main;
import com.api.Equipment;
import com.api.FullTableView;
import com.api.GoBack;
import com.api.Reservation;
import com.mongodb.Mongo;
import com.requests.MongoRequests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReservationController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public ReservationController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/reservation.fxml"));
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
    private TableView<Reservation> reservationTableView;
    @FXML
    private TableColumn<Reservation, String> nameTableColumn;
    @FXML
    private TableColumn<Reservation, String> surnameTableColumn;
    @FXML
    private TableColumn<Reservation, String> productTableColumn;
    @FXML
    private TableColumn<Reservation, String> productIdTableColumn;
    @FXML
    private TableColumn<Reservation, String> startTableColumn;
    @FXML
    private TableColumn<Reservation, String> endTableColumn;
    @FXML
    private TableColumn<Reservation, String> statusTableColumn;

    @Override
    public void fullTableView() {
        ArrayList<Document> equipmentArrayList = new ArrayList<>();
        ArrayList<Document> clientArrayList = new ArrayList<>();
        ArrayList<Document> reservationArrayList = new ArrayList<>();

        equipmentArrayList = getCollection("items");
        clientArrayList = getCollection("users");
        reservationArrayList = getCollection("reservations");

        Document tempEquipment = new Document();
        Document tempClient = new Document();

        ObservableList<Reservation> observableList = FXCollections.observableArrayList();

        try {
            for (int i = 0; i < reservationArrayList.size(); i++) {
//                System.out.println("jestem w loopie reservation");
                for (int h = 0; h < equipmentArrayList.size(); h++) {
//                    System.out.println("jestem w loopie equipment");
                    if (equipmentArrayList.get(h).get("productId").equals(reservationArrayList.get(i).get("productId"))) {
//                        System.out.println("znalazlem equipment");
                        tempEquipment = equipmentArrayList.get(h);
                        break;
                    }
                }
                for (int k = 0; k < clientArrayList.size(); k++) {
//                    System.out.println("jestem w loopie client");
//                    System.out.println("id klienta badanego: " + clientArrayList.get(k).get("_id"));
//                    System.out.println("id klienta z rezerwacji: " + reservationArrayList.get(i).get("userId"));
                    if (clientArrayList.get(k).get("_id").toString().equals(reservationArrayList.get(i).get("userId").toString())) {
//                        System.out.println("znalazlem klienta");
                        tempClient = clientArrayList.get(k);
                        break;
                    }
                }

                observableList.add(new Reservation(
                        tempClient.get("name").toString(),
                        tempClient.get("surname").toString(),
                        tempEquipment.get("type").toString(),
                        tempEquipment.get("productId").toString(),
                        reservationArrayList.get(i).get("startDate").toString(),
                        reservationArrayList.get(i).get("finishDate").toString(),
                        reservationArrayList.get(i).get("status").toString()
                ));
            }
        }catch (Exception e){}

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("name"));
        surnameTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("surname"));
        productTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("product"));
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("productId"));
        startTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("start"));
        endTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("end"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("status"));


        nameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productIdTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        startTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        endTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        reservationTableView.setItems(observableList);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        backButton.setOnAction(event -> back());
    }
}
