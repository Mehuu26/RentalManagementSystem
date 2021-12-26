package com.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoRequests{
    static String uri = "mongodb+srv://tester:test@rental-system.yjcmg.mongodb.net/rental-system?retryWrites=true&w=majority";
    static MongoClient mongoClient = MongoClients.create(uri);
    static MongoDatabase database = mongoClient.getDatabase("rental-data");

    protected static boolean getEmployee(String login, String password) {
        MongoCollection<Document> collection = database.getCollection("employee");

        Document test = collection.find(and(eq("user", login), eq("password", password))).first();

        if (test != null) {
            return true;
        } else {
            return false;
        }
    }

    protected static ArrayList getCollection(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        ArrayList<Document> tempList = new ArrayList<>();
        Document tempDocument = new Document();
        String tempString = new String();

        ObjectMapper mapper = new ObjectMapper();
        //Equipment equipment = mapper.readValue(cur)


        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                tempList.add(cursor.next());
            }
            return tempList;
        }
    }

    protected static ArrayList getCollectionFilter(String collectionName, String fieldname, String filter) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        ArrayList<Document> tempList = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find(eq(fieldname, filter)).iterator();
        try {
            while (cursor.hasNext()) {
                tempList.add(cursor.next());
                System.out.println(tempList);
            }
        }finally {
            cursor.close();
        }
            return tempList;
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

    protected static void updateEquipment(String type, String producer, String model, String size, String oldProductId, String newProductId){
        MongoCollection<Document> collection = database.getCollection("items");
        Document tempDocument = collection.find(eq("productId", oldProductId)).first();

        Document updatedDocument = new Document();
        updatedDocument.append("type", type);
        updatedDocument.append("producer", producer);
        updatedDocument.append("model", model);
        updatedDocument.append("size", size);
        updatedDocument.append("productId", newProductId);

        UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);
    }

    protected static void deleteEquipment(String productId){
        MongoCollection<Document> collection = database.getCollection("items");
        Document tempDocument = collection.find(eq("productId", productId)).first();

        try{
            collection.deleteOne(tempDocument);
        }catch(MongoException e){
            System.out.println("unable to delete object due to " + e + "error");
        }
    }

    protected static boolean addPrices(String type, String hour, String day){
        MongoCollection<Document> collection = database.getCollection("prices");

        Document tempDocument = collection.find(eq("type", type)).first();  //check if there's no same type

        if(tempDocument == null) {
            Document doc = new Document();
            doc.append("type", type);
            doc.append("hour", hour);
            doc.append("day", day);

            collection.insertOne(doc);
            return true;
        }else {
            return false;
        }
    }

    protected static void updatePrices(String type, String hour, String day, String oldType){
        MongoCollection<Document> collection = database.getCollection("prices");
        Document tempDocument = collection.find(eq("type", oldType)).first();

        if(type.equals(tempDocument.get("type"))  && hour.equals(tempDocument.get("hour")) && day.equals(tempDocument.get("day")))  return; //if the values are not changed

        Document updatedDocument = new Document();
        updatedDocument.append("type", type);
        updatedDocument.append("hour", hour);
        updatedDocument.append("day", day);

        UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);
    }
}
