package com.requests;

import com.api.DataBaseRequests;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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

    public static void addEquipment(String type, String producer, String model, String productID){
        MongoCollection<Document> collection = database.getCollection("items");

        Document doc = new Document();
        doc.append("type", type);
        doc.append("producer", producer);
        doc.append("model", model);
        doc.append("productId", productID);

        collection.insertOne(doc);
    }

    protected static boolean getEmployee(String login, String password){
        MongoCollection<Document> collection = database.getCollection("employee");

        Document test = collection.find(and(eq("user", login), eq("password", password))).first();

            if(test != null ) {
                return true;
            }else {
                return false;
            }
    }



}
