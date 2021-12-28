package com.controllers.Settings;

import com.Main;
import com.api.FullTableView;
import com.api.GoBack;
import com.api.TypeAndPriceList;
import com.controllers.Settings.SettingsController;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsAddTypeAndPriceListController extends MongoRequests implements Initializable, GoBack, FullTableView {
    public SettingsAddTypeAndPriceListController(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/settingsAddTypeAndPriceList.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button backButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private TableColumn<TypeAndPriceList, String> typeTableColumn;
    @FXML
    private TableColumn<TypeAndPriceList, String> dayTableColumn;
    @FXML
    private TableColumn<TypeAndPriceList, String> hourTableColumn;
    @FXML
    private TableView<TypeAndPriceList> typeAndPriceListTableView;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField hourTextField;
    @FXML
    private TextField dayTextField;
    @FXML
    private Label noDataProvidedLabel;
    @FXML
    private Label questionMarkLabel;

    private String typeDoubleClicked = new String();

    @Override
    public void back() {
        new SettingsController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullTableView();
        backButton.setOnAction(event -> back());
        addButton.setOnAction(event -> addNewType());
        typeAndPriceListTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!typeAndPriceListTableView.getSelectionModel().isEmpty()))
                tableViewDoubleClicked();
        });
        updateButton.setOnAction(event -> updateType());

        questionMarkLabel.setTooltip(new Tooltip("double click on type in table you want to edit"));

        deleteButton.setOnAction(event -> deleteType());

    }

    @Override
    public void fullTableView() {
        ArrayList<Document> tempTypeAndPriceArrayList = new ArrayList<>();
        tempTypeAndPriceArrayList = getCollection("prices");

        ObservableList<TypeAndPriceList> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < tempTypeAndPriceArrayList.size(); i++) {
            observableList.add(new TypeAndPriceList(
                    tempTypeAndPriceArrayList.get(i).get("type").toString(),
                    tempTypeAndPriceArrayList.get(i).get("hour").toString(),
                    tempTypeAndPriceArrayList.get(i).get("day").toString()
                    )
            );
        }

        typeTableColumn.setCellValueFactory(new PropertyValueFactory<TypeAndPriceList, String>("type"));
        hourTableColumn.setCellValueFactory(new PropertyValueFactory<TypeAndPriceList, String>("hour"));
        dayTableColumn.setCellValueFactory(new PropertyValueFactory<TypeAndPriceList, String>("day"));

        typeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        hourTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dayTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        typeAndPriceListTableView.setItems(observableList);
    }

    private void addNewType(){
        if(!isDouble(dayTextField)){    //check if the values are numeric data
            noDataProvidedLabel.setText("Enter numeric data prices");
            return;
        }

        if(!isDouble(hourTextField)){    //check if the values are numeric data
            noDataProvidedLabel.setText("Enter numeric data prices");
            return;
        }

        if (typeTextField.getText().isEmpty() || hourTextField.getText().isEmpty() || dayTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("No data provided");
            return;
        }else{
            if(addPrices(typeTextField.getText(), hourTextField.getText(), dayTextField.getText())){
                noDataProvidedLabel.setText("");    //just to "no data provided" text disapear
                typeTextField.clear(); //just to clear the productId text field
                hourTextField.clear();
                dayTextField.clear();
                fullTableView();
            }else {
                noDataProvidedLabel.setText("Same product type detected");
                return;
            }
        }
        clearTextFields();
    }

    private void updateType(){
        if(!isDouble(dayTextField)){    //check if the values are numeric data
            noDataProvidedLabel.setText("Enter numeric data prices");
            return;
        }

        if(!isDouble(hourTextField)){    //check if the values are numeric data
            noDataProvidedLabel.setText("Enter numeric data prices");
            return;
        }

        ArrayList<Document> equipmentList = new ArrayList();
        if (typeTextField.getText().isEmpty() || hourTextField.getText().isEmpty() || dayTextField.getText().isEmpty()) {
            noDataProvidedLabel.setText("No data provided");
            return;
        } else {
            if(checkObjectFilter("prices", "type", typeTextField.getText(), typeDoubleClicked)) { //2 product id's becouse one is old and second one is new. If condition check if the type value already exist
                updatePrices(typeTextField.getText(), hourTextField.getText(), dayTextField.getText(), typeDoubleClicked);
                noDataProvidedLabel.setText("");
                if (!(typeDoubleClicked.equals(typeTextField.getText()))) {   //condition if there is no type value edited
                    equipmentList = getCollectionFilter("items", "type", typeDoubleClicked);    //looking for equipment where type = old type (double clicked) to change vales from old type to new type.
                    for (int i = 0; i < equipmentList.size(); i++) {
                        updateEquipment(typeTextField.getText(), equipmentList.get(i).get("producer").toString(), equipmentList.get(i).get("model").toString(), equipmentList.get(i).get("size").toString(), equipmentList.get(i).get("productId").toString(), equipmentList.get(i).get("productId").toString());
                    }
                }
            }else{
                noDataProvidedLabel.setText("Same type of product detected");
            }
        }

        fullTableView();
        clearTextFields();
        setDisableTrue();
    }

    @Override
    public void tableViewDoubleClicked(){
        TypeAndPriceList tempType = typeAndPriceListTableView.getSelectionModel().getSelectedItem();
        if (typeAndPriceListTableView.getSelectionModel().isEmpty()) {
            return;
        } else {
            typeTextField.setText(tempType.getType());
            hourTextField.setText(tempType.getHour());
            dayTextField.setText(tempType.getDay());

            typeDoubleClicked = typeTextField.getText();

            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    @Override
    public void clearTextFields() {
        typeTextField.clear();
        hourTextField.clear();
        dayTextField.clear();
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

    private boolean isDouble(TextField f) {
        try
        {
            Double.parseDouble(f.getText());
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private void deleteType(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete operation");
        alert.setHeaderText(null);
        alert.setContentText("When deleting type and price list you will delete every deleting type value equipment");

        Optional<ButtonType> result = alert.showAndWait();
        ArrayList<Document> equipmentList = new ArrayList<>();
        if (result.get() == ButtonType.OK){ //if user press ok
            deletePrices(typeDoubleClicked);
            equipmentList = getCollectionFilter("items", "type", typeDoubleClicked);
            for (int i = 0; i<equipmentList.size(); i++){
                deleteEquipment(equipmentList.get(i).get("productId").toString());
            }


        } else {
        }
        fullTableView();
        clearTextFields();
        setDisableTrue();
    }
}
