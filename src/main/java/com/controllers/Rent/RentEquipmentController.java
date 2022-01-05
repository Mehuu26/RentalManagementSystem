package com.controllers.Rent;

import com.Main;
import com.api.*;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class RentEquipmentController extends MongoRequests implements GoBack, Initializable, FullTableView {
    public RentEquipmentController(Client client) {
        //System.out.println("konstruktor");
        this.client=client;
        this.clientId = this.client.get_id();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/rentEquipment.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("klient z konstrukora " + client);
//        System.out.println("klient id z konstrukotra " + client.get_id());
//        System.out.println("klient private stworzony " + this.client);
//        System.out.println("klient id z private stworzonego " + this.client.get_id());

    }

    public RentEquipmentController(Client client, Equipment equipment, Reservation reservation){
        //System.out.println("konstruktor");
        this.client=client;
        this.equipment=equipment;
        this.reservation=reservation;
        this.clientId = this.client.get_id();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/rentEquipment.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Reservation reservation;
    private Equipment equipment;
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

    @Override
    public void fullTableView() {
        ArrayList<Document> tempRentalList = new ArrayList<>();
        ArrayList<Document> tempEquipmentList = new ArrayList<>();

        tempRentalList = getCollection("rentals");
        tempEquipmentList = getCollection("items");

        Document tempRental = new Document();
        Document tempEquipment = new Document();

        ObservableList<Rental> observableList = FXCollections.observableArrayList();

        Long milliSeconds;
        Calendar calendar = Calendar.getInstance();
        String stringTime;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (int i = 0; i < tempRentalList.size(); i++) {   //first looking for match between rental object and user id, which we get from constructor
            if (clientId.equals(tempRentalList.get(i).get("userId"))) {//if match found
                System.out.println("znalazlem pare z user id");
                //tempRental = tempRentalList.get(i);//saving temporary file
                for(int h = 0; h<tempEquipmentList.size(); h++){    //looking for product id match between rental and equipment list
                    if(tempRentalList.get(i).get("productId").equals(tempEquipmentList.get(h).get("productId"))){   //if match found
                        System.out.println("znalazlem pare z product id");

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
        Rental rental = rentalTableView.getSelectionModel().getSelectedItem();
        MongoRequests.deleteObject("rentals", rental.get_id());
        fullTableView();
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
        new RentChooseClientController();
    }

    private boolean rent(){
        Instant instant = Instant.now();    //getting timeStamp, time since 01.01.1970 till 19.01.2038
        long timeStampMillis = instant.toEpochMilli();
        String timeInMillis = Long.toString(timeStampMillis);

        if(productIdTextField.getText().isEmpty()){
            noDataProvidedLabel.setText("enter product Id");
            return false;
        }

        if(MongoRequests.checkObjectFileterExists("items", "productId", productIdTextField.getText())) { //if product exists
            System.out.println("znalazlem equipment o podanym product id");
            if (!MongoRequests.checkObjectFileterExists("rentals", "productId", productIdTextField.getText())){    //check if there's no product id already rented
                System.out.println("produkt nie jest wypozyczony");
                noDataProvidedLabel.setText("");
                MongoRequests.addRental(productIdTextField.getText(), client.get_id(), timeInMillis, "true");
                fullTableView();
                return true;
            }else {
                noDataProvidedLabel.setText("this product has been already rented");
                return false;
            }
        }else{
            noDataProvidedLabel.setText("no product detected");
            return false;
        }
    }

    private ArrayList showSimilar(){
        if(!equipment.getProductId().equals(productIdTextField.getText())){ //if there's no same product id in text field change the product
            Document newEquipment = MongoRequests.getObjectFilter("items", "productId", productIdTextField.getText());

            //change equipment we are looking for
            this.equipment.setType(newEquipment.get("type").toString());
            this.equipment.setProducer(newEquipment.get("producer").toString());
            this.equipment.setModel(newEquipment.get("model").toString());
            this.equipment.setSize(newEquipment.get("size").toString());
            this.equipment.setProductId(newEquipment.get("productId").toString());
        }

        ArrayList<Document> tempEquipmentList = MongoRequests.getCollectionFilter("items", "type", equipment.getType());
        ArrayList<Document> tempSimilarEquipmentList = new ArrayList<>();
        ArrayList<Document> tempRentalList = MongoRequests.getCollectionFilter("rentals", "status", "true");

        boolean flag = true; //flag to store value if there is rented product or not.

        for(int i = 0; i<tempEquipmentList.size(); i++){    //check collection if there is similar product, search by model and size.
            if(tempEquipmentList.get(i).get("model").equals(equipment.getModel()) && tempEquipmentList.get(i).get("size").equals(equipment.getSize())){
                for(int h = 0; h<tempRentalList.size(); h++){
                    if(tempRentalList.get(h).get("productId").equals(tempEquipmentList.get(i).get("productId"))) {   //check rental list with rental status = true, when
                        flag = false;
                        break;
                    }
                }
                if(flag) {  //check flag if previous for found rental with status true and product id we checking. If true not found.
                    tempSimilarEquipmentList.add(tempEquipmentList.get(i));
                }else{
                    flag = true;
                }
            }
        }

        if(tempSimilarEquipmentList.isEmpty()){
            System.out.println("there's no similar product");
            return null;
        }else{
            for(int i = 0; i<tempSimilarEquipmentList.size(); i++){
            System.out.println("found similar free to rent product " + tempSimilarEquipmentList.get(i));
            }
            return tempSimilarEquipmentList;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!(equipment == null)){   //check which controller we are using. This with equipment in it or not.
            productIdTextField.setText(equipment.getProductId());
        }

        System.out.println("initialize");
        clientLabel.setText(client.getName() + " " + client.getSurname() + " " + client.getIdCard());
        fullTableView();
        backButton.setOnAction(event -> back());

        rentalTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!rentalTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

        rentButton.setOnAction(event -> {   //rent button
            if(!(equipment == null)){   //check if rent from reservation
                if(rent()) {    //check if you can rent
                    MongoRequests.updateReservationById(reservation.getReservationId(), "status",  "done"); //change status in reservation as done
                }else if(productIdTextField.getText().isEmpty()){  //otherwise return
                    return;
                }else{
                    ArrayList <Document> similarProductList = showSimilar();
                    System.out.println(equipment);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reservated product has been alread rented");
                    if (similarProductList == null){
                        alert.setHeaderText("No similar product found");
                        String s ="There's no left similar product";
                        alert.setContentText(s);
                        alert.show();
                    }else{
                    alert.setHeaderText("Product with these ID's are available to rent.");
                    String s = new String();

                    for(int i = 0; i<similarProductList.size(); i++){
                        s += similarProductList.get(i).get("productId");
                        s += ", ";
                    }

                    alert.setContentText(s);
                    alert.show();
                }
                }
            }else{  //if rented not from reservation
                rent();
            }
        });
    }
}
