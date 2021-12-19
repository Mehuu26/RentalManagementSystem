package com.api;

import okhttp3.*;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.util.Collections;

public abstract class InsertFindRequest {
    protected void insertRequest(String collection, JsonObject jsonFile) throws Exception{

        JsonBuilderFactory builderFactory = Json.createBuilderFactory(Collections.<String, Object>emptyMap()); //check if it is ok

        JsonObject connectionData = builderFactory.createObjectBuilder()
                .add("collection", collection)
                .add("database", "rental-data")
                .add("dataSource", "rental-system")
                .add("document", jsonFile).build();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, connectionData.toString());
        Request request = new Request.Builder()
                .url("https://data.mongodb-api.com/app/data-lasjp/endpoint/data/beta/action/insertOne") //findOne/insertOne
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "6VPv7kgAnpG0Ge0PpLdOXATONkGoz7lzri7rve7KbbQ5wsLxeDXnSp4WtBtDEhQG")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());


    }
}
