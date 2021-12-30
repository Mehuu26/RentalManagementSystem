package com.controllers.Rent;


import com.Main;
import com.api.Client;
import com.api.GoBack;
import com.mongodb.Mongo;
import com.requests.MongoRequests;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RentEditClientController extends MongoRequests implements Initializable, GoBack {
    public RentEditClientController(Client client){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenesFXML/rentEditClient.fxml"));
            loader.setController(this);
            Main.STAGE.setScene(new Scene(loader.<Parent>load()));
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    @Override
    public void back() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
