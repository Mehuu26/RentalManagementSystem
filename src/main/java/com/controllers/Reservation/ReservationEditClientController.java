package com.controllers.Reservation;

import com.Main;
import com.api.*;
import com.controllers.Rent.RentEquipmentController;
import com.controllers.Reservation.ReservationController;
import com.requests.MongoRequests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReservationEditClientController extends MongoRequests implements GoBack, Initializable {
    public ReservationEditClientController(Client client, Equipment equipment, Reservation reservation){
        this.client = client;
        this.equipment = equipment;
        this.reservation = reservation;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/reservationEditClient.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private Client client;
    private Equipment equipment;
    private Reservation reservation;

    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
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

    @Override
    public void back() {
        new ReservationController();
    }

    private void next(){
            if (nameTextField.getText().isEmpty()){
                noDataProvidedLabel.setText("enter name");
                return;
            }else if(surnameTextField.getText().isEmpty()){
                noDataProvidedLabel.setText("enter surname");
                return;
            }else if(phoneTextField.getText().isEmpty()){
                noDataProvidedLabel.setText("enter phone");
                return;
            }else if(idCardTextField.getText().isEmpty()){
                noDataProvidedLabel.setText("enter ID Card");
                return;
            }else{
                if(!MongoRequests.updateClient(     //if the value is false (same id card detected)
                        nameTextField.getText(),
                        surnameTextField.getText(),
                        phoneTextField.getText(),
                        idCardTextField.getText(),
                        client.get_id())){
                    noDataProvidedLabel.setText("same id Card detected");
                    return;
                }
                new RentEquipmentController(client, equipment, reservation);
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(event -> back());
        nextButton.setOnAction(event -> next());

        if(!client.getName().isEmpty()){
            nameTextField.setText(Crypt.decrypt(Crypt.password, client.getName()));
        }
        if(!client.getSurname().isEmpty()){
            surnameTextField.setText(Crypt.decrypt(Crypt.password, client.getSurname()));
        }
        if(!client.getPhone().isEmpty()){
            phoneTextField.setText(Crypt.decrypt(Crypt.password, client.getPhone()));
        }
        if(!client.getIdCard().isEmpty()){
            idCardTextField.setText(Crypt.decrypt(Crypt.password, client.getIdCard()));
        }
    }
}
