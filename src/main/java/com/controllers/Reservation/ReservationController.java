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
    private TableColumn<Reservation, String> modelTableColumn;
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
                if(reservationArrayList.get(i).get("status").equals("done") || reservationArrayList.get(i).get("status").equals("cancelled")) {
                    continue;
                }
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
                        tempClient.get("_id").toString(),
                        tempEquipment.get("type").toString(),
                        tempEquipment.get("productId").toString(),
                        tempEquipment.get("model").toString(),
                        startStringDate,
                        finishStringDate,
                        reservationArrayList.get(i).get("status").toString(),
                        reservationArrayList.get(i).get("_id").toString()
                ));
            }
        }catch (Exception e){}

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("name"));
        surnameTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("surname"));
        productTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("product"));
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("productId"));
        modelTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("model"));
        startTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("start"));
        endTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("end"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("status"));


        nameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productIdTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        startTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        endTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        reservationTableView.setItems(observableList);
    }

    private void rentReservated(){
        if (reservationTableView.getSelectionModel().isEmpty()){
            noDataProvidedLabel.setText("select reservation or double click on it");
        }else{
            Reservation reservation = reservationTableView.getSelectionModel().getSelectedItem();
            Document tempDocumentClient = MongoRequests.getObjectFilterById("users", "_id", reservation.getUserId().toString());
            Document tempDocumentEquipment = MongoRequests.getObjectFilter("items", "productId", reservation.getProductId());

            System.out.println(tempDocumentClient);
            System.out.println(tempDocumentEquipment);

            Client client = new Client(tempDocumentClient.get("name").toString(),
                    tempDocumentClient.get("surname").toString(),
                    tempDocumentClient.get("phone").toString(),
                    tempDocumentClient.get("idCard").toString(),
                    tempDocumentClient.get("_id").toString()
                    );

            Equipment equipment = new Equipment(tempDocumentEquipment.get("type").toString(),
                    tempDocumentEquipment.get("producer").toString(),
                    tempDocumentEquipment.get("model").toString(),
                    tempDocumentEquipment.get("size").toString(),
                    tempDocumentEquipment.get("productId").toString()
                    );

            //checking if every value in client is filled up
            if(client.getPhone().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation); //if not go to edit client controller
            }else if(client.getIdCard().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation);
            }else if(client.getName().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation);
            }else if(client.getSurname().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation);
            }else{
                new RentEquipmentController(client, equipment, reservation); //if yes go to rent reservated controller
            }
        }

    }

    @Override
    public void tableViewDoubleClicked() {
        if (reservationTableView.getSelectionModel().isEmpty()){
            noDataProvidedLabel.setText("select reservation or double click on it");
        }else{
            Reservation reservation = reservationTableView.getSelectionModel().getSelectedItem();
            System.out.println(reservation.getUserId());
            Document tempDocumentClient = MongoRequests.getObjectFilterById("users", "_id", reservation.getUserId().toString());
            Document tempDocumentEquipment = MongoRequests.getObjectFilter("items", "productId", reservation.getProductId());

            Client client = new Client(tempDocumentClient.get("name").toString(),
                    tempDocumentClient.get("surname").toString(),
                    tempDocumentClient.get("phone").toString(),
                    tempDocumentClient.get("idCard").toString(),
                    tempDocumentClient.get("_id").toString()
            );

            Equipment equipment = new Equipment(tempDocumentEquipment.get("type").toString(),
                    tempDocumentEquipment.get("producer").toString(),
                    tempDocumentEquipment.get("model").toString(),
                    tempDocumentEquipment.get("size").toString(),
                    tempDocumentEquipment.get("productId").toString()
            );

            //checking if every value in client is filled up
            if(client.getPhone().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation); //if not go to edit client controller
            }else if(client.getIdCard().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation);
            }else if(client.getName().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation);
            }else if(client.getSurname().isEmpty()){
                new ReservationEditClientController(client, equipment, reservation);
            }else{
                new RentEquipmentController(client, equipment, reservation); //if yes go to rent equipment controller
            }
        }

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



        rentReservatedButton.setOnAction(event -> {
            if (reservationTableView.getSelectionModel().isEmpty()){
                noDataProvidedLabel.setText("Choose reservation from table");
            } else{
                rentReservated();
            }
        });



        reservationTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!reservationTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

    }
}
