package com;

import com.api.Crypt;
import com.controllers.LogInController;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.*;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class Main extends Application {
    public static final Stage STAGE = new Stage();
    @Override
    public void start(Stage primaryStage) throws Exception{
        new LogInController();
        STAGE.show();



        //Parent root = FXMLLoader.load(getClass().getResource("/scenesFXML/logIn.fxml"));
        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 600, 400));
        //primaryStage.show();
    }

    public static void main(String[] args) throws Exception{
//        System.out.println("wyswietla");
//        String uri = "mongodb+srv://tester:test@rental-system.yjcmg.mongodb.net/rental-system?retryWrites=true&w=majority";
//        try (MongoClient mongoClient = MongoClients.create(uri)) {
//            System.out.println("stworzono polaczenie");
//
//            MongoDatabase database = mongoClient.getDatabase("rental-data");
//            System.out.println("jestem w database");
//
//            MongoCollection<Document> collection = database.getCollection("items");
//            System.out.println("jestem w kolekcji");
//
//            Document doc = collection.find(eq("type", "ski")).first();
//            System.out.println("znalazlem narte");
//
//            System.out.println(doc.toJson());
//        }
        Crypt.init();

        String test = "hello world!";

        String result = Crypt.encrypt("password", test);

        String decrypted = Crypt.decrypt("password", result);

        System.out.println(result);

        String encryptedValue= "D3c8cA29FF93Ab51A70651bDcA516F49E8RCvhihGLInGFv4JNEXQg2Aee684eD2F60F8523e2ecF87F8A832A";

        launch(args);
    }
}
