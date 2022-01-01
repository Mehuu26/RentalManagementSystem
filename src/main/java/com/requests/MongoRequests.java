package com.requests;

import com.api.Client;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoRequests {
    static String uri = "mongodb+srv://tester:test@rental-system.yjcmg.mongodb.net/rental-system?retryWrites=true&w=majority";
    static MongoClient mongoClient = MongoClients.create(uri);
    static MongoDatabase database = mongoClient.getDatabase("rental-data");

    protected static boolean checkObjectFilter(String collectionName, String fieldname, String filter, String oldFilter) {   //true if there is no similar object
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document filterDocument = collection.find(eq(fieldname, filter)).first();
        Document oldFilterDocument = collection.find(eq(fieldname, oldFilter)).first();

        try {
            if (filterDocument == null) return true;    //when theres no new object with id
            else if (filterDocument.get("_id").equals(oldFilterDocument.get("_id")))
                return true; //when the found product is same we are editing
            else if (filterDocument.get(fieldname).equals(filter))
                return false; //when we found other document then ours and filter is the same
            else return true;
        } catch (NullPointerException e) {
            System.out.println("Exception " + e);
        }
        return false;
    }

    protected static ArrayList getCollection(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        ArrayList<Document> tempList = new ArrayList<>();
        Document tempDocument = new Document();
        String tempString = new String();

        //ObjectMapper mapper = new ObjectMapper();
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
        } finally {
            cursor.close();
        }
        return tempList;
    }

    protected static Document getObjectFilter(String collectionName, String fieldName, String filter) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document tempDocument = collection.find(eq(fieldName, filter)).first();

        return tempDocument;
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

    protected static boolean addEmployee(String user, String password, String name, String surname) {
        MongoCollection<Document> collection = database.getCollection("employee");
        Document test = collection.find(and(eq("user", user))).first();
        Document insertEmployee = new Document();

        if (test == null) {
            insertEmployee.append("user", user);
            insertEmployee.append("password", password);
            insertEmployee.append("name", name);
            insertEmployee.append("surname", surname);

            collection.insertOne(insertEmployee);
            return true;
        } else
            return false;

    }

    protected static void updateEmployee(String user, String password, String name, String surname, String oldUser) {
        MongoCollection<Document> collection = database.getCollection("employee");
        Document tempDocument = collection.find(eq("user", oldUser)).first();

        Document updatedDocument = new Document();
        updatedDocument.append("user", user);
        updatedDocument.append("password", password);
        updatedDocument.append("name", name);
        updatedDocument.append("surname", surname);

        UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);
        return;
    }

    protected static void deleteEmployee(String user) {
        MongoCollection<Document> collection = database.getCollection("employee");
        Document tempDocument = collection.find(eq("user", user)).first();

        try {
            collection.deleteOne(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }

    }

    public static boolean addEquipment(String type, String producer, String model, String size, String productId) {
        MongoCollection<Document> collection = database.getCollection("items");

        Document tempDocument = collection.find(eq("productId", productId)).first();

        //System.out.println(tempDocument); //just to check

        if (tempDocument == null) {
            Document doc = new Document();
            doc.append("type", type);
            doc.append("producer", producer);
            doc.append("model", model);
            doc.append("size", size);
            doc.append("productId", productId);

            collection.insertOne(doc);
            return true;
        } else {
            return false;
        }
    }

    protected static void updateEquipment(String type, String producer, String model, String size, String oldProductId, String newProductId) {
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

    protected static void deleteEquipment(String productId) {
        MongoCollection<Document> collection = database.getCollection("items");
        Document tempDocument = collection.find(eq("productId", productId)).first();

        try {
            collection.deleteOne(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }
    }

    protected static boolean addPrices(String type, String hour, String day) {
        MongoCollection<Document> collection = database.getCollection("prices");

        Document tempDocument = collection.find(eq("type", type)).first();  //check if there's no same type

        if (tempDocument == null) {
            Document doc = new Document();
            doc.append("type", type);
            doc.append("hour", hour);
            doc.append("day", day);

            collection.insertOne(doc);
            return true;
        } else {
            return false;
        }
    }

    protected static void updatePrices(String type, String hour, String day, String oldType) {
        MongoCollection<Document> collection = database.getCollection("prices");
        Document tempDocument = collection.find(eq("type", oldType)).first();   //it tooks first product with value type. Not searching for another just to be quicker. There is no possiblity to be another same type.

        Document filterDocument = collection.find(eq("type", type)).first();

        //if(type.equals(tempDocument.get("type"))  && hour.equals(tempDocument.get("hour")) && day.equals(tempDocument.get("day")))  return false; //if the values are not changed
        //else if(type.equals(filterDocument.get("type"))) return false; //if there is the same type already in collection
        //else{

        Document updatedDocument = new Document();
        updatedDocument.append("type", type);
        updatedDocument.append("hour", hour);
        updatedDocument.append("day", day);

        UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);
        return;
    }

    protected static void deletePrices(String type) {
        MongoCollection<Document> collection = database.getCollection("prices");
        Document tempDocument = collection.find(eq("type", type)).first();

        try {
            collection.deleteOne(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }
    }

    protected static boolean updateClient(String name, String surname, String phone, String idCard, String _id) {
        MongoCollection<Document> collection = database.getCollection("users");

        Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first(); //looking for client with object id
        System.out.println("I found client");

//        if (!phone.isEmpty()) { //when phone value is not null, then looking for object with same phone number if there is no with same number going next
//            Document checkDocumentPhone = collection.find(eq("phone", phone)).first();
//            System.out.println("test check phone" + checkDocumentPhone);
//            if(checkDocumentPhone == null){
//                System.out.println("check document phone is null");
//            }else if (checkDocumentPhone.get("_id").equals(new ObjectId(_id))){//check if found phone number is the same object we are updating
//                System.out.println("same id detected");
//            }else{//if checkDocumentPhone is not empty, return. Phone number need to be uniqe
//                System.out.println("phone number exists");
//                return;
//            }
//        }

        if (!idCard.isEmpty()) {
            Document checkDocumentIdCard = collection.find(eq("idCard", idCard)).first();
            System.out.println("test check id card" + checkDocumentIdCard);
            //System.out.println("test object id " + checkDocumentIdCard.get("_id").equals(new ObjectId(_id)) + "id object= " + _id + " found object id= " + checkDocumentIdCard.get("_id"));
            if(checkDocumentIdCard == null) {
                System.out.println("check document ID Card is null");
            }else if (checkDocumentIdCard.get("_id").equals(new ObjectId(_id))){//check if found phone number is the same object we are updating
                System.out.println("same id detected");
            } else{
                System.out.println("id Card number exists");
                return false;
            }
        }

        System.out.println(tempDocument);
        if (tempDocument == null) {
            System.out.println("there's no id value object");
            return false;
        }

        Document updatedDocument = new Document();
        updatedDocument.append("name", name);
        updatedDocument.append("surname", surname);

        //System.out.println(tempDocument.getBoolean("email"));

        if (tempDocument.get("email").equals("")) updatedDocument.append("email", "");
        else updatedDocument.append("email", tempDocument.get("email"));

        if (tempDocument.get("googleId").equals("")) updatedDocument.append("googleId", "");
        else updatedDocument.append("googleId", tempDocument.get("googleId"));

        updatedDocument.append("phone", phone);

        if (tempDocument.get("password").equals("")) updatedDocument.append("password", "");
        else updatedDocument.append("password", tempDocument.get("password"));

        updatedDocument.append("idCard", idCard);

        UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);

        return true;
    }

    protected static boolean addClient(String name, String surname, String phone, String idCard) {
        MongoCollection<Document> collection = database.getCollection("users");

        Document tempDocument = collection.find(eq("idCard", idCard)).first();  //check if there's no same type

        if (tempDocument == null) {
            Document doc = new Document();
            doc.append("name", name);
            doc.append("surname", surname);
            doc.append("email", "");
            doc.append("googleId", "");
            doc.append("phone", phone);
            doc.append("password", "");
            doc.append("idCard", idCard);
            collection.insertOne(doc);
            return true;
        }
        return false;
    }

    protected static void deleteClient(String _id) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first();

        if (tempDocument == null) {
            return;
        }

        try {
            collection.deleteOne(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }
    }

    protected static Client getClient(String idCard){
        MongoCollection<Document> collection = database.getCollection("users");
        Document tempDocument = collection.find(eq("idCard", idCard)).first();

        if (tempDocument != null) {
            Client client = new Client(tempDocument.get("name").toString(),
                    tempDocument.get("surname").toString(),
                    tempDocument.get("phone").toString(),
                    tempDocument.get("idCard").toString(),
                    tempDocument.get("_id").toString()
            );
        } else {
            return null;
        }

        return null;
    }

    protected static void updateReservations(String productId, String userId, String startDate, String finishDate, String price, String status, String oldProductId) {
        MongoCollection<Document> collection = database.getCollection("reservations");
        Document tempDocument = collection.find(eq("productId", oldProductId)).first();   //it tooks first product with same oldproduct Id which we override with new product id
        Document updatedDocument = new Document();
        updatedDocument.append("productId", productId); //new product id
        updatedDocument.append("userId", userId);
        updatedDocument.append("startDate", startDate);
        updatedDocument.append("finishDate", finishDate);
        updatedDocument.append("price", price);
        updatedDocument.append("status", status);

        UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);
        return;
    }

    protected static void deleteReservations(String fieldName, String filter){
        MongoCollection<Document> collection = database.getCollection("reservations");

        Bson tempDocument = eq(fieldName, filter);

        try {
            collection.deleteMany(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }
    }
}

