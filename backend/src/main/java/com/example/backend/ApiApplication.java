package com.example.backend;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import org.bson.Document;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.stream.StreamSupport;

@ApplicationPath("/api")
public class ApiApplication extends Application {

    public static MongoCollection<Document> connect(String databaseName, String collectionName) throws IllegalArgumentException {
        String password = System.getenv("MANGODB_PASSWORD");

        ConnectionString connectionString = new ConnectionString("mongodb+srv://Drakmain:" + password + "@economy.qqflq.mongodb.net");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase marketDatabase = mongoClient.getDatabase(databaseName);

        MongoIterable<String> mongoIterable = marketDatabase.listCollectionNames();

        boolean answer = StreamSupport.stream(mongoIterable.spliterator(), false)
                .anyMatch(n -> n.equals(collectionName));

        if (answer) {
            return marketDatabase.getCollection(collectionName);
        } else {
            throw new IllegalArgumentException("Collection " + collectionName + " does not exist in " + databaseName + " Database");
        }
    }
}