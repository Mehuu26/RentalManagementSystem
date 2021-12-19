package com.requests;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongoRequests {
    static String uri = "mongodb+srv://tester:test@rental-system.yjcmg.mongodb.net/rental-system?retryWrites=true&w=majority";
    static MongoClient mongoClient = MongoClients.create(uri);
    static MongoDatabase database = mongoClient.getDatabase("rental-data");

    public static void getCollection(String collectionName, String value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        System.out.println("jestem w kolekcji");

        Document doc = collection.find(eq("type", value)).first();
        System.out.println("znalazlem narte");
        System.out.println(doc.toJson());
    }


}
