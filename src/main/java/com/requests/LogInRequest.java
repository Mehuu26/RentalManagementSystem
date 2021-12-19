package com.requests;

import com.mongodb.client.MongoDatabase;
import okhttp3.*;

public class LogInRequest extends MongoRequests{

    public void LogInDataCheck(String login, String password)throws Exception{
        getCollection("items", "test");
        getCollection("items", "h");
        getCollection("items", "q");
        getCollection("items", "test");



        //DataCheck(login, password);
    }

    private void DataCheck(String login, String password)throws Exception{
        String temp = "{\n    \"collection\":\"employee\",\n    " +
                "\"database\":\"rental-data\",\n    " +
                "\"dataSource\":\"rental-system\",\n    " +
                "\"filter\": {\"user\": \""+login+"\",\n" +
                "\"password\": \""+password+"\"}\n\n}";

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, temp);
        Request request = new Request.Builder()
                .url("https://data.mongodb-api.com/app/data-lasjp/endpoint/data/beta/action/findOne") //findOne/insertOne
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "6VPv7kgAnpG0Ge0PpLdOXATONkGoz7lzri7rve7KbbQ5wsLxeDXnSp4WtBtDEhQG")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }




}
