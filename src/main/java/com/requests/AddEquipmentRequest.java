package com.requests;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.internal.MongoBatchCursorAdapter;
import javafx.fxml.Initializable;
import okhttp3.*;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.util.Collections;
import com.api.*;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class AddEquipmentRequest extends MongoRequests {
    public AddEquipmentRequest(){
    }

    public void equipment(String type, String producer, String model, String productID) throws Exception{
        //addNewEquipment(type, producer);
        getCollection("items", "ski");
            //testRequest(type, producer, model, productID);
    }

//    public void test() {
//        //Creating a MongoDB client
//        MongoClient mongo = new MongoClient();
//        //Connecting to the database
//        MongoDatabase database = mongo.getDatabase("myDatabase");
//        //Creating a collection
//        database.createCollection("students");
//        //Preparing a document
//        Document document = new Document();
//        document.append("name", "Ram");
//        document.append("age", 26);
//        document.append("city", "Hyderabad");
//        //Inserting the document into the collection
//        database.getCollection("students").insertOne(document);
//        System.out.println("Document inserted successfully");
//    }

    private void testRequest(String type, String producer, String model, String productID) throws Exception {

        JsonBuilderFactory builderFactory = Json.createBuilderFactory(Collections.<String, Object>emptyMap()); //check if it is ok
        JsonObject equipmentData = builderFactory.createObjectBuilder()
                .add("type", type)
                .add("producer", producer)
                .add("model", model)
                .add("photo", "null")
                .add("productId", productID).build();

        //insertRequest("items", equipmentData);
    }
}
