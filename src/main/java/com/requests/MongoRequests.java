package com.requests;

import com.api.DataBaseRequests;
import com.api.Equipment;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Cursor;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.json.Json;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoRequests implements DataBaseRequests {
    static String uri = "mongodb+srv://tester:test@rental-system.yjcmg.mongodb.net/rental-system?retryWrites=true&w=majority";
    static MongoClient mongoClient = MongoClients.create(uri);
    static MongoDatabase database = mongoClient.getDatabase("rental-data");

    @Override
    public void getCollection(String collectionName, String fieldName, String fieldValue) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        System.out.println("jestem w kolekcji");

        Document doc = collection.find(eq(fieldName, fieldValue)).first();
        System.out.println("znalazlem narte");
        System.out.println(doc.toJson());
    }

    public static boolean addEquipment(String type, String producer, String model, String size, String productId) {
        MongoCollection<Document> collection = database.getCollection("items");

        Document tempDocument = collection.find(eq("productId", productId)).first();

        //System.out.println(tempDocument); //just to check

        if(tempDocument == null) {
            Document doc = new Document();
            doc.append("type", type);
            doc.append("producer", producer);
            doc.append("model", model);
            doc.append("size", size);
            doc.append("productId", productId);

            collection.insertOne(doc);
            return true;
        }else {
            return false;
        }
    }

    protected static boolean getEmployee(String login, String password) {
        MongoCollection<Document> collection = database.getCollection("employee");

        Document test = collection.find(and(eq("user", login), eq("password", password))).first();

        if (test != null) {
            return true;
        } else {
            return false;
        }
    }


    protected static ArrayList getEquipment() {
        MongoCollection<Document> collection = database.getCollection("items");
        ArrayList<Document> equipmentList = new ArrayList<>();
        Document tempDocument = new Document();
        String tempString = new String();

        ObjectMapper mapper = new ObjectMapper();
        //Equipment equipment = mapper.readValue(cur)


        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                equipmentList.add(cursor.next());

                //case do robicia calego stringa
                //nastepnie dodanie obiektu klasy Equipment
                //następnie dodanie go do observable list
                //a liste dodac do tableView

                //equipmentList.add(cursor.next());
            }
            return equipmentList;


        }

    }

    // TODO: 25.12.2021 need the document to be parse into BSON file to update equipment
    protected static void updateEquipment(String type, String producer, String model, String size, String productId){
        MongoCollection<Document> collection = database.getCollection("items");
        Document tempDocument = collection.find(eq("productId", productId)).first();

        Document updatedDocument = new Document();
            updatedDocument.append("type", type);
            updatedDocument.append("producer", producer);
            updatedDocument.append("model", model);
            updatedDocument.append("size", size);
            updatedDocument.append("productId", productId);



        UpdateResult updateResult = collection.updateOne(tempDocument, updatedDocument);
    }
}
