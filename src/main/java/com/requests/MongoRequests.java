package com.requests;

import com.api.Client;
import com.api.Crypt;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoRequests extends Crypt {
    static String uri = "mongodb+srv://tester:test@rental-system.yjcmg.mongodb.net/rental-system?retryWrites=true&w=majority";
    static MongoClient mongoClient = MongoClients.create(uri);
    static MongoDatabase database = mongoClient.getDatabase("rental-data");


    protected static void deleteEveryObject(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        //Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first();

        if (collection == null) {
            return;
        }

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                collection.deleteOne(cursor.next());
            }
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }
    }

    protected static void deleteObject(String collectionName, String _id) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first();

        try {
            collection.deleteOne(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }


    }

    protected static boolean checkObjectFileterExists(String collectionName, String fieldname, String filter) {  //true if object exists
        MongoCollection<Document> collection = database.getCollection(collectionName);
        if (collectionName.equals("employee")) {
            System.out.println("collection name equals employee");
            ArrayList<Document> employeeList = getCollection("employee");
            System.out.println(employeeList);

            for (int i = 0; i < employeeList.size(); i++) {
                if (Crypt.decrypt(Crypt.password, employeeList.get(i).get(fieldname).toString()).equals(filter)) {
                    return true;
                }
            }
            return false;
        } else {
            Document filterDocument = collection.find(eq(fieldname, filter)).first();
            if (filterDocument == null) {
                return false;
            } else return true;
        }
    }

    protected static boolean checkObjectDoubleFilterExists(String collectionName, String fieldname, String filter, String secondFieldName, String secondFilter) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document filterDocument = collection.find(and(eq(fieldname, filter), eq(secondFieldName, secondFilter))).first();
        if (filterDocument == null) {
            return false;
        } else return true;
    }

    protected static boolean checkObjectFilter(String collectionName, String fieldname, String filter, String oldFilter) {   //true if there is no similar object
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Document filterDocument = new Document();
        Document oldFilterDocument = new Document();

        System.out.println(collectionName + "-------------------------------------------------------------------------");
        if (collectionName.equals("employee")) { //decrypting employees
            System.out.println("collection name equals employee");
            ArrayList<Document> employeeList = getCollection("employee");
            System.out.println(employeeList);

            for (int i = 0; i < employeeList.size(); i++) {
                if (Crypt.decrypt(Crypt.password, employeeList.get(i).get(fieldname).toString()).equals(filter)) {
                    System.out.println("found same fieldname and filter");
                    filterDocument = employeeList.get(i);
                }
                if (Crypt.decrypt(Crypt.password, employeeList.get(i).get(fieldname).toString()).equals(oldFilter)) {
                    System.out.println("found same fieldname and oldFilter");
                    oldFilterDocument = employeeList.get(i);
                }
            }
        } else {
            filterDocument = collection.find(eq(fieldname, filter)).first();
            oldFilterDocument = collection.find(eq(fieldname, oldFilter)).first();
        }

        System.out.println("7777777777777777777777777777777777777777777777777");
        System.out.println("filter document" + filterDocument.isEmpty());
        System.out.println("check if filter document is true " + filterDocument.equals("{}"));
        System.out.println("old filter document " + oldFilterDocument);
        try {
            if (filterDocument == null || filterDocument.isEmpty()) return true;    //when theres no new object with id
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
                //System.out.println(tempList);
            }
        } finally {
            cursor.close();
        }
        return tempList;
    }

    protected static Document getObjectFilter(String collectionName, String fieldName, String filter) { //put "" value in fieldname and filter to get first document in collection
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document tempDocument = new Document();
        if (fieldName.isEmpty() && filter.isEmpty()) {
            tempDocument = collection.find().first();
        } else {
            tempDocument = collection.find(eq(fieldName, filter)).first();
        }
        return tempDocument;
    }

    protected static Document getObjectDoubleFilter(String collectionName, String fieldName, String filter, String secondFieldName, String secondFilter) { //put "" value in fieldname and filter to get first document in collection
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document tempDocument = new Document();
        if (fieldName.isEmpty() && filter.isEmpty()) {
            tempDocument = collection.find().first();
        } else {
            tempDocument = collection.find(and(eq(fieldName, filter), eq(secondFieldName, secondFilter))).first();
        }
        return tempDocument;
    }

    protected static Document getObjectFilterById(String collectionName, String fieldName, String filter) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document tempDocument = new Document();

        try {
            if (fieldName.isEmpty() && filter.isEmpty()) {    //put
                return null;
            } else {
                tempDocument = collection.find(eq(fieldName, new ObjectId(filter))).first();
                System.out.println(tempDocument);
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return tempDocument;
    }

    protected static boolean getEmployee(String login, String password) {
        ArrayList<Document> employeeList = getCollection("employee");
        System.out.println(employeeList);

        for (int i = 0; i < employeeList.size(); i++) {
            System.out.println(Crypt.decrypt(Crypt.password, employeeList.get(i).get("user").toString()));
            System.out.println(Crypt.decrypt(Crypt.password, employeeList.get(i).get("password").toString()));
            if (Crypt.decrypt(Crypt.password, employeeList.get(i).get("user").toString()).equals(login) && Crypt.decrypt(Crypt.password, employeeList.get(i).get("password").toString()).equals(password)) {
                return true;
            }
        }
        return false;

    }

    protected static boolean addEmployee(String user, String password, String name, String surname) {
        MongoCollection<Document> collection = database.getCollection("employee");

        Document insertEmployee = new Document();

        //get whole emplyee collection then decrypt every user and check if there is no similar one
        ArrayList<Document> employeeList = getCollection("employee");
        for (int i = 0; i < employeeList.size(); i++) {
            if (Crypt.decrypt(Crypt.password, employeeList.get(i).get("user").toString()).equals(user)) {
                System.out.println("found same fieldname and filter");
                return false;
            }
        }

        if (!user.isEmpty()) {
            user = Crypt.encrypt(Crypt.password, user);
            System.out.println("user:");
            System.out.println(user);
        }
        if (!password.isEmpty()) {
            password = Crypt.encrypt(Crypt.password, password);
            System.out.println("password:");
            System.out.println(password);
        }
        if (!name.isEmpty()) {
            name = Crypt.encrypt(Crypt.password, name);
            System.out.println("name:");
            System.out.println(name);
        }
        if (!surname.isEmpty()) {
            surname = Crypt.encrypt(Crypt.password, surname);
            System.out.println("surname:");
            System.out.println(surname);
        }

        insertEmployee.append("user", user);
        insertEmployee.append("password", password);
        insertEmployee.append("name", name);
        insertEmployee.append("surname", surname);

        collection.insertOne(insertEmployee);

        return true;

    }

    protected static void updateEmployee(String user, String password, String name, String surname, String oldUser) {
        MongoCollection<Document> collection = database.getCollection("employee");
        Document tempDocument = new Document();
        //Document tempDocument = collection.find(eq("user", oldUser)).first();

        //get whole emplyee collection then decrypt every user and check if there is similar one
        ArrayList<Document> employeeList = getCollection("employee");
        for (int i = 0; i < employeeList.size(); i++) {
            if (Crypt.decrypt(Crypt.password, employeeList.get(i).get("user").toString()).equals(oldUser)) {
                System.out.println("found same fieldname and filter");
                tempDocument = employeeList.get(i);
            }
        }
        if (!user.isEmpty()) {
            user = Crypt.encrypt(Crypt.password, user);
            System.out.println("user:");
            System.out.println(user);
        }
        if (!password.isEmpty()) {
            password = Crypt.encrypt(Crypt.password, password);
            System.out.println("password:");
            System.out.println(password);
        }
        if (!name.isEmpty()) {
            name = Crypt.encrypt(Crypt.password, name);
            System.out.println("name:");
            System.out.println(name);
        }
        if (!surname.isEmpty()) {
            surname = Crypt.encrypt(Crypt.password, surname);
            System.out.println("surname:");
            System.out.println(surname);
        }

        Document updatedDocument = new Document();
        updatedDocument.append("user", user);
        updatedDocument.append("password", password);
        updatedDocument.append("name", name);
        updatedDocument.append("surname", surname);

        if (tempDocument.equals(null)) {
            return;
        } else {
            UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);
            return;
        }
    }

    protected static void deleteEmployee(String user) {
        MongoCollection<Document> collection = database.getCollection("employee");

        Document tempDocument = new Document();

        ArrayList<Document> employeeList = getCollection("employee");
        for (int i = 0; i < employeeList.size(); i++) {
            if (Crypt.decrypt(Crypt.password, employeeList.get(i).get("user").toString()).equals(user)) {
                System.out.println("found user");
                tempDocument = employeeList.get(i);
            }
        }

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
        if (type == "" || producer == "" || model == "" || size == "" || productId == "") {
            return false;
        }

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

        if (!idCard.isEmpty()) {
            Document checkDocumentIdCard = new Document();

            ArrayList<Document> clientsList = MongoRequests.getCollection("users");
            for(int i = 0; i<clientsList.size(); i++){
                if(Crypt.decrypt(Crypt.password, clientsList.get(i).get("idCard").toString()).equals(idCard)){
                    checkDocumentIdCard = clientsList.get(i);
                    break;
                }
            }

            System.out.println("test check id card" + checkDocumentIdCard);
            //System.out.println("test object id " + checkDocumentIdCard.get("_id").equals(new ObjectId(_id)) + "id object= " + _id + " found object id= " + checkDocumentIdCard.get("_id"));
            if (checkDocumentIdCard == null || checkDocumentIdCard.isEmpty()) {
                System.out.println("check document ID Card is null");
            } else if (checkDocumentIdCard.get("_id").equals(new ObjectId(_id))) {//check if found phone number is the same object we are updating
                System.out.println("same id detected");
            } else {
                System.out.println("id Card number exists");
                return false;
            }
        }

        System.out.println(tempDocument);
        if (tempDocument == null) {
            System.out.println("there's no id value object");
            return false;
        }

        Crypt.init();
        name = encrypt(Crypt.password, name);
        surname = encrypt(Crypt.password, surname);
        phone = encrypt(Crypt.password, phone);
        idCard = encrypt(Crypt.password, idCard);

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

        Document tempDocument = new Document();

        ArrayList<Document> clientsList = MongoRequests.getCollection("users");
        for(int i = 0; i<clientsList.size(); i++){
            if(Crypt.decrypt(Crypt.password, clientsList.get(i).get("idCard").toString()).equals(idCard)){
                tempDocument = clientsList.get(i);
                break;
            }
        }



        System.out.println("add client mongo db ---------------------------------------");
        Crypt.init();
        if (!name.isEmpty()) {
            name = Crypt.encrypt(Crypt.password, name);
            System.out.println("name:");
            System.out.println(name);
        }
        if (!surname.isEmpty()) {
            surname = Crypt.encrypt(Crypt.password, surname);
            System.out.println("surname:");
            System.out.println(surname);
        }
        if (!phone.isEmpty()) {
            phone = Crypt.encrypt(Crypt.password, phone);
            System.out.println("phone:");
            System.out.println(phone);
        }
        if (!idCard.isEmpty()) {
            idCard = Crypt.encrypt(Crypt.password, idCard);
            System.out.println("idCard:");
            System.out.println(idCard);
        }


        if (tempDocument == null || tempDocument.isEmpty()) {
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

    protected static Client getClient(String idCard) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document tempDocument = collection.find(eq("idCard", idCard)).first();

        Crypt.init();

        if (tempDocument != null) {
            Client client = new Client(Crypt.decrypt(Crypt.password, tempDocument.get("name").toString()),
                    Crypt.decrypt(Crypt.password, tempDocument.get("surname").toString()),
                    Crypt.decrypt(Crypt.password, tempDocument.get("phone").toString()),
                    Crypt.decrypt(Crypt.password, tempDocument.get("idCard").toString()),
                    Crypt.decrypt(Crypt.password, tempDocument.get("_id").toString())
            );
            return client;
        } else {
            return null;
        }
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

    protected static void updateReservationById(String _id, String fieldname, String updatedValue) {
        MongoCollection<Document> collection = database.getCollection("reservations");

        if (_id.isEmpty() || fieldname.isEmpty() || updatedValue.isEmpty()) {
            return;
        } else {
            Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first();

            Bson updates = Updates.combine(
                    Updates.set(fieldname, updatedValue)
            );

            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = collection.updateOne(tempDocument, updates, options);
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
    }

    protected static void deleteReservations(String fieldName, String filter) {
        MongoCollection<Document> collection = database.getCollection("reservations");

        Bson tempDocument = eq(fieldName, filter);

        try {
            collection.deleteMany(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }
    }

    protected static void updateCompanyInfo(String _id, String phone, String email, String title, String close, String open, String address, String percentage) {
        MongoCollection<Document> collection = database.getCollection("company");

        Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first();

        Document updatedDocument = new Document();
        updatedDocument.append("phone", phone);
        updatedDocument.append("email", email);
        updatedDocument.append("title", title);
        updatedDocument.append("close", close);
        updatedDocument.append("open", open);
        updatedDocument.append("address", address);
        updatedDocument.append("percentage", percentage);

        UpdateResult updateResult = collection.replaceOne(tempDocument, updatedDocument);
    }

    protected static void addRental(String productId, String userId, String startDate, String status) {
        MongoCollection<Document> collection = database.getCollection("rentals");

        Document doc = new Document();
        doc.append("productId", productId);
        doc.append("userId", userId);
        doc.append("startDate", startDate);
        doc.append("finishDate", "");
        doc.append("price", "");
        doc.append("status", status);
        collection.insertOne(doc);
        return;
    }

    protected static void updateRentalProductId(String _id, String oldProductId, String newProductId) {
        MongoCollection<Document> collection = database.getCollection("rentals");

        if (_id.isEmpty() || oldProductId.isEmpty() || newProductId.isEmpty()) {
            return;
        } else {
            Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first();

            Bson updates = Updates.combine(
                    Updates.set("productId", newProductId)
            );

            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = collection.updateOne(tempDocument, updates, options);
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
    }

    protected static void updateRental(String _id, String finishDate, String status, String price) {
        MongoCollection<Document> collection = database.getCollection("rentals");

        if (_id.isEmpty() || finishDate.isEmpty() || status.isEmpty() || price.isEmpty()) {
            return;
        } else {
            Document tempDocument = collection.find(eq("_id", new ObjectId(_id))).first();

            Bson updates = Updates.combine(
                    Updates.set("finishDate", finishDate),
                    Updates.set("price", price),
                    Updates.set("status", status)
            );

            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = collection.updateOne(tempDocument, updates, options);
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
    }

    protected static void deleteRentals(String fieldName, String filter) {
        MongoCollection<Document> collection = database.getCollection("rentals");

        Bson tempDocument = eq(fieldName, filter);

        try {
            collection.deleteMany(tempDocument);
        } catch (MongoException e) {
            System.out.println("unable to delete object due to " + e + "error");
        }
    }
}


