package com.controllers;

import com.Main;
import com.api.Client;
import com.api.Equipment;
import com.api.FullTableView;
import com.api.GoBack;
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
import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.soap.Text;

import java.io.IOException;
import java.net.URL;

public class ClientsController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public ClientsController() {
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
    private Button updateButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
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
    @FXML
    private TableColumn<Client, String> _idTableColumn;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField idCardTextField;
    @FXML
    private Label noDataProvidedLabel;

    private String _idDoubleClicked = new String();

    @Override
    public void fullTableView() {
        ArrayList<Document> clientsList = new ArrayList<>();
        clientsList = getCollection("users");

        //System.out.println(clientsList.get(1).get("_id").toString()); //string to show in terminal

        ObservableList<Client> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < clientsList.size(); i++) {
            observableList.add(new Client(
                    clientsList.get(i).get("name").toString(),
                    clientsList.get(i).get("surname").toString(),
                    clientsList.get(i).get("phone").toString(),
                    clientsList.get(i).get("idCard").toString(),
                    clientsList.get(i).get("_id").toString()
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


        clientsTableView.setItems(observableList);
    }

    @Override
    public void tableViewDoubleClicked() {
        Client client = clientsTableView.getSelectionModel().getSelectedItem();
        if (clientsTableView.getSelectionModel().isEmpty()) {
            return;
        } else {
            nameTextField.setText(client.getName());
            surnameTextField.setText(client.getSurname());
            phoneTextField.setText(client.getPhone());
            idCardTextField.setText(client.getIdCard());

            _idDoubleClicked = client.get_id();
            setDisableFalse();
        }
    }

    @Override
    public void clearTextFields() {
        nameTextField.clear();
        surnameTextField.clear();
        phoneTextField.clear();
        idCardTextField.clear();
    }

    @Override
    public void setDisableTrue() {
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @Override
    public void setDisableFalse() {
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }


    @Override
    public void back() {
        new MainPanelController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        fullTableView();

        clientsTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!clientsTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });

        updateButton.setOnAction(event -> updateExistingClient());

        addButton.setOnAction(event -> addNewClient());

        deleteButton.setOnAction(event -> deleteExistingClient());

//        String temp = "U2FsdGVkX19zz6XZ00cUP1DiM1VRszGKgDeckFzoPOA=";
//        byte[] test = temp.getBytes();
//
//        System.out.println(test);
//        byte[] encrypted = encrypt("hello world");
//        System.out.println("Encrypted text: " + encrypted.toString());
//        String decrypted = decrypt(encrypted);
//        System.out.println("Encrypted text back to plain text: " + decrypted);
    }

//    private static byte[] encrypt(String s) {
//        if (s.isEmpty()) return null;
//        String password = "orange";
//        JNCryptor cryptor = new AES256JNCryptor();
//        try {
//            return cryptor.encryptData(s.getBytes(), password.toCharArray());
//        }
//        catch (CryptorException e)
//        {
//            // Something went wrong
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static String decrypt(byte[] msg) {
//        String password = "orange";
//
//        JNCryptor cryptor = new AES256JNCryptor();
//        try
//        {
//            return (new String(cryptor.decryptData(msg, password.toCharArray())));
//        }
//        catch (CryptorException e)
//        {
//            // Something went wrong
//            e.printStackTrace();
//            return null;
//        }
//    }

    private void addNewClient() {
        if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || phoneTextField.getText().isEmpty() || idCardTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("No data provided");
            return;
        }else
            if(!addClient(nameTextField.getText(), surnameTextField.getText(), phoneTextField.getText(), idCardTextField.getText())){
                noDataProvidedLabel.setText("Same ID Card detected");
                return;
            }
            noDataProvidedLabel.setText("");
            fullTableView();
            clearTextFields();
            setDisableTrue();
    }

    private void updateExistingClient() {
        if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || phoneTextField.getText().isEmpty() || idCardTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm edit operation");
            alert.setHeaderText(null);
            alert.setContentText("You are updating clients with none value");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) { //if user press ok
                if(!updateClient(nameTextField.getText(), surnameTextField.getText(), phoneTextField.getText(), idCardTextField.getText(), _idDoubleClicked)){
                    noDataProvidedLabel.setText("same ID Card detected");
                }
            }
            fullTableView();
            clearTextFields();

            setDisableTrue();


        }else {
            if(!updateClient(nameTextField.getText(), surnameTextField.getText(), phoneTextField.getText(), idCardTextField.getText(), _idDoubleClicked)){
                noDataProvidedLabel.setText("same ID Card detected");
                return;
            }
            fullTableView();
            clearTextFields();
            setDisableTrue();
        }


    }

    private void deleteExistingClient(){
        if (clientsTableView.getSelectionModel().isEmpty()) {
            noDataProvidedLabel.setText("select row which you want to delete");
            return;
        } else noDataProvidedLabel.setText("");


        Client client = clientsTableView.getSelectionModel().getSelectedItem();

        //check if client does have active rental if yes you can not delete him
        if(MongoRequests.checkObjectDoubleFilterExists("rentals", "userId", client.get_id(), "status", "true")){
            noDataProvidedLabel.setText("user with active rental can not be delete");
            return;
        }


        MongoRequests.deleteClient(client.get_id());//deleting client
        MongoRequests.deleteReservations("userId", client.get_id());//delete all clients reservations
        //if there are old rentals delete them, higher in this code there is "if" checking if the rental is active
        MongoRequests.deleteRentals("userId", client.get_id());

        fullTableView();    //to refresh table view
        clearTextFields();
        setDisableTrue();
    }
}
