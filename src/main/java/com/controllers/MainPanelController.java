package com.controllers;

import com.Main;
import com.controllers.Rent.RentChooseClientController;
import com.controllers.Settings.SettingsController;
import com.requests.MongoRequests;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPanelController extends MongoRequests implements Initializable {
    public MainPanelController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/mainPanel.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private Button addEquipmentButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button clientsButton;
    @FXML
    private Button reservationButton;
    @FXML
    private Button rentReservatedButton;
    @FXML
    private Button rentButton;

    private void addEquipment(){
        new AddEquipmentController();
    }

    private void settings(){
        new SettingsController();
    }

    private void clients(){
        new ClientsController();
    }

    private void reservation(){
        new ReservationController();
    }

    private void rentReservated(){
        new ReturnController();
    }

    private void rent(){
        new RentChooseClientController();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addEquipmentButton.setOnAction(event -> addEquipment());
        settingsButton.setOnAction(event -> checkOut());
        clientsButton.setOnAction(event -> clients());
        reservationButton.setOnAction(event -> reservation());
        rentReservatedButton.setOnAction(event -> rentReservated());
        rentButton.setOnAction(event -> rent());
    }

    private void checkOut() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Log In");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if(!username.getText().equals("admin")) return null;
                if (MongoRequests.getEmployee(username.getText(), password.getText()))
                    new SettingsController();
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });


    }

}
