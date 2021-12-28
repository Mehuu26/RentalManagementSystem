package com.controllers;

import com.Main;
import com.api.Client;
import com.api.Equipment;
import com.api.FullTableView;
import com.api.GoBack;
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientsController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public ClientsController(){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/clients.fxml"));
                loader.setController(this);
                Main.STAGE.setScene(new Scene(loader.<Parent>load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @FXML
    private Button backButton;
    @FXML
    private TableView<Client> clientsTableView;
    @FXML
    private TableColumn<Client, String> nameTableColumn;
    @FXML
    private TableColumn<Client, String> surnameTableColumn;
    @FXML
    private TableColumn<Client, String> phoneTableColumn;
    @FXML
    private TableColumn<Client, String> idCardTableColumn;

    @Override
    public void fullTableView() {
        ArrayList<Document> clientsList = new ArrayList<>();
        clientsList = getCollection("users");

        ObservableList<Client> observableList = FXCollections.observableArrayList();

        for (int i = 0; i<clientsList.size(); i++){
            observableList.add(new Client(
                    clientsList.get(i).get("name").toString(),
                    clientsList.get(i).get("surname").toString(),
                    clientsList.get(i).get("phone").toString(),
                    clientsList.get(i).get("idCard").toString()
                    )
            );
        }
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        surnameTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("surname"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("phone"));
        idCardTableColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("idCard"));

        nameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idCardTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        clientsTableView.setItems(observableList);
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
        backButton.setOnAction(event -> back());
        fullTableView();
    }
}
