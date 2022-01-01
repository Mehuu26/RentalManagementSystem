package com.controllers.Rent;

import com.Main;
import com.api.Client;
import com.api.Equipment;
import com.api.FullTableView;
import com.api.GoBack;
import com.controllers.MainPanelController;
import com.mongodb.Mongo;
import com.requests.MongoRequests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.bson.Document;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RentChooseClientController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public RentChooseClientController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/rent.fxml"));
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
    private Button addNewClientButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField chooseClientTextField;
    @FXML
    private TableView<Client> chooseClientTableView;
    @FXML
    private TableColumn<Client, String> nameTableColumn;
    @FXML
    private TableColumn<Client, String> surnameTableColumn;
    @FXML
    private TableColumn<Client, String> phoneTableColumn;
    @FXML
    private TableColumn<Client, String> idCardTableColumn;
    @FXML
    private TableColumn<Client, String> _idTableColumn;
    @FXML
    private Label noDataProvidedLabel;

    @Override
    public void fullTableView() {
        ArrayList<Document> tempClientArrayList = new ArrayList<>();
        tempClientArrayList = getCollection("users");

        ObservableList<Client> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < tempClientArrayList.size(); i++) {
            observableList.add(new Client(
                    tempClientArrayList.get(i).get("name").toString(),
                    tempClientArrayList.get(i).get("surname").toString(),
                    tempClientArrayList.get(i).get("phone").toString(),
                    tempClientArrayList.get(i).get("idCard").toString(),
                    tempClientArrayList.get(i).get("_id").toString()
                    )
            );
        }
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        surnameTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("surname"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("phone"));
        idCardTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("idCard"));
        _idTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("_id"));

        nameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idCardTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        _idTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        chooseClientTableView.setItems(observableList);

        //initializable filtered list to look for data in TableView
        FilteredList<Client> filteredData = new FilteredList<>(observableList, b -> true);

        chooseClientTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(Client -> {
                if(newValue.isEmpty() || newValue == null){ //if no values display all records.
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if(Client.getName().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;    //means there is name found
                }else if(Client.getSurname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;    //means there is surname found
                }else if(Client.getPhone().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;    //means there is phone found
                }else if(Client.getIdCard().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;    //means there is idCard found
                }else if(Client.get_id().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;    //means there is surname found
                }else
                    return false; // return false if there is no match found
            });
        });

        SortedList<Client> sortedData = new SortedList<>(filteredData);

        // Bind sorted results in table view
        sortedData.comparatorProperty().bind(chooseClientTableView.comparatorProperty());

        //set sorted data in table view
        chooseClientTableView.setItems(sortedData);

    }


    @Override
    public void tableViewDoubleClicked() {
        Client client = chooseClientTableView.getSelectionModel().getSelectedItem();
        if (chooseClientTableView.getSelectionModel().isEmpty()) {
            return;
        } else {
            if(client.getName().isEmpty() || client.getSurname().isEmpty() || client.getPhone().isEmpty() || client.getIdCard().isEmpty()){
                new RentEditClientController(client);
            }else{
                new RentEquipmentController(client);
            }
        }
    }

    @Override
    public void clearTextFields() {

    }

    @Override
    public void setDisableTrue() {
        nextButton.setDisable(true);
    }

    @Override
    public void setDisableFalse() {
        nextButton.setDisable(false);
    }

    @Override
    public void back() {
        new MainPanelController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        backButton.setOnAction(event -> back());

        chooseClientTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!chooseClientTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

    }
}
