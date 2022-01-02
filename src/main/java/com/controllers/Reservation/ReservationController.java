package com.controllers.Reservation;

import com.Main;
import com.api.*;
import com.controllers.MainPanelController;
import com.controllers.Rent.RentEditClientController;
import com.controllers.Rent.RentEquipmentController;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Button rentReservatedButton;
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
    @FXML
    private Label noDataProvidedLabel;

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

        Long startMilliSeconds;
        Calendar startCalendar = Calendar.getInstance();
        String startStringDate;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Long finishMilliSeconds;
        Calendar finishCalendar = Calendar.getInstance();
        String finishStringDate;


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

                //parsing start and finish milliseconds into date format
                startMilliSeconds = Long.parseLong(reservationArrayList.get(i).get("startDate").toString());
                startCalendar.setTimeInMillis(startMilliSeconds);
                startStringDate = formatter.format(startCalendar.getTime());

                finishMilliSeconds = Long.parseLong(reservationArrayList.get(i).get("finishDate").toString());
                finishCalendar.setTimeInMillis(finishMilliSeconds);
                finishStringDate = formatter.format(finishCalendar.getTime());

                //adding new reservation object
                observableList.add(new Reservation(
                        tempClient.get("name").toString(),
                        tempClient.get("surname").toString(),
                        tempEquipment.get("type").toString(),
                        tempEquipment.get("productId").toString(),
                        startStringDate,
                        finishStringDate,
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

    // TODO: 02.01.2022 searching for client and equipment and sending it as a constructor to another class
    private void rentReservated(){
        if (reservationTableView.getSelectionModel().isEmpty()){
            noDataProvidedLabel.setText("select reservation or double click on it");
        }else{
            Reservation reservation = reservationTableView.getSelectionModel().getSelectedItem();
        }
    }
    
    // TODO: 02.01.2022 searching for client and equipment and sending it as a constructor to another class
    @Override
    public void tableViewDoubleClicked() {
        Reservation reservation = reservationTableView.getSelectionModel().getSelectedItem();
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


    // TODO: 02.01.2022 add disable true and false for the rent button while tableView row is not clicked 
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        backButton.setOnAction(event -> back());
    }
}
