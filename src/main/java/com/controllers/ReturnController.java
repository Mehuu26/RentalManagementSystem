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
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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
    private ArrayList<Document> returnArrayList = new ArrayList<>();    //returnArrayList stores rentals, which we get in second, return table view.
    private long sum;

    @FXML
    private Button backButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button finishButton;
    @FXML
    private TextField productIdTextField;
    @FXML
    private Label noDataProvidedLabel;
    @FXML
    private Label currentClientLabel;
    @FXML
    private Label sumUpLabel;
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
    public void fullTableView() {   //full first, rental table view
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
                                "",
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

    private void fullReturnTableView(Document tempRental) {  //value from text field. Store equipment we are getting. Full second, rental table view
        ArrayList<Document> tempEquipmentList = getCollection("items");

        ObservableList<Rental> observableList = FXCollections.observableArrayList();


        sumUp();    //method which calculate amount of money, and show it in label

        //inicialize values
        Long milliSeconds;
        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarFinishDate = Calendar.getInstance();
        String stringStartTime;  //string
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //format

        //get current time as a time stamp
        Instant instant = Instant.now();    //getting timeStamp, time since 01.01.1970 till 19.01.2038
        long finishTimeStampMillis = instant.toEpochMilli();    //this is converted to date format, for user to see
        //string value to database
        String stringFinishTime;  //string
        String finishTimeInMillis = Long.toString(finishTimeStampMillis);

        returnArrayList.add(tempRental);

        System.out.println("wielkosc return array list to: " + returnArrayList.size());
        System.out.println(returnArrayList.get(0));
            for (int i = 0; i < returnArrayList.size(); i++) {
                for(int h = 0; h<tempEquipmentList.size(); h++){
                    if(tempEquipmentList.get(h).get("productId").equals(returnArrayList.get(i).get("productId"))){
                        //parsing milliseconds into date format
                        milliSeconds = Long.parseLong(returnArrayList.get(i).get("startDate").toString());
                        calendarStartDate.setTimeInMillis(milliSeconds);
                        stringStartTime = formatter.format(calendarStartDate.getTime());

                        calendarStartDate.setTimeInMillis(finishTimeStampMillis);
                        stringFinishTime = formatter.format(calendarStartDate.getTime());

                        observableList.add(new Rental(tempEquipmentList.get(h).get("type").toString(),
                                tempEquipmentList.get(h).get("model").toString(),
                                tempEquipmentList.get(h).get("size").toString(),
                                tempEquipmentList.get(h).get("productId").toString(),
                                returnArrayList.get(i).get("userId").toString(),    //user id from rental collection
                                stringStartTime,
                                stringFinishTime,
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
        Document tempDocument = MongoRequests.getObjectDoubleFilter("rentals", "productId", productIdTextField.getText(), "status", "true");
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

    // TODO: 05.01.2022 calculate time between two days and sum it up 
    private void finish(){
        for(int i = 0; i < returnTableView.getItems().size(); i++){    //for goes through the Table View which stores rental data which client want to return
            String startDateString=returnTableView.getItems().get(i).getStartDate();
            String finishDateString=returnTableView.getItems().get(i).getFinishDate();

            //parsing string formats into date format
            try {
                Date startDate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(startDateString);
                Date finishDate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(finishDateString);

                // TODO: 07.01.2022 somewhere there is problem 
                //calculating duration time
                long diff = finishDate.getTime() - startDate.getTime();
                System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                System.out.println("days " + days);
                System.out.println ("Hours: " + TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS));
                long hours = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                System.out.println("Hours " + hours);
                //float days = (diff / (1000*60*60*24));    //second converter
                //System.out.println("second conferter: " + days);

                if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 0){    //if days == 0;
                    Document tempPrice = MongoRequests.getObjectFilter("prices", "type", returnTableView.getItems().get(i).getType());
                    long hoursPrice = Long.parseLong(tempPrice.get("hour").toString());
                    long daysPrice = Long.parseLong(tempPrice.get("day").toString());
                    if(hours*hoursPrice > daysPrice) {
                        System.out.println("hours price is bigger then days price");
                        sum += daysPrice;
                        System.out.println(sum);
                    }else{
                        System.out.println("hours price is lower then days price");
                        sum += hours*hoursPrice;
                        System.out.println(sum);
                    }
                }

                sumUpLabel.setText(Long.toString(sum));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            //returnArrayList.get(i).append()
            //System.out.println(returnArrayList.get(i));
            //MongoRequests.updateRental(returnTableView.getItems().get(i).get_id(), returnTableView.getItems().get(i).getFinishDate(), "false", "100");
        }


    }

    private void sumUp(){   //

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        returnButton.setOnAction(event -> returnEquipment());

        rentalTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!rentalTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

        finishButton.setOnAction(event -> finish());


    }
}
