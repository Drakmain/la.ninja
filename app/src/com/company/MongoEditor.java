package com.company;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MongoEditor {

    private final MongoClient mongoClient;

    private final MongoDatabase marketDatabase;

    private final List<String> sectionName;

    public MongoEditor() {
        String[] env = new String[0];

        try {
            File file = new File("./app/.env");
            BufferedReader br = new BufferedReader(new FileReader(file));
            env = br.readLine().split("=");
        } catch (IOException e) {
            System.err.println("Error : IOException" + e.getMessage());
            System.exit(1);
        }

        String password = URLEncoder.encode(env[1], StandardCharsets.UTF_8);

        ConnectionString connectionString = new ConnectionString("mongodb+srv://Drakmain:" + password + "@economy.qqflq.mongodb.net");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
        mongoClient = MongoClients.create(settings);
        marketDatabase = mongoClient.getDatabase("Market");

        sectionName = new ArrayList<>();
        sectionName.add("Skin");
        sectionName.add("Engraving_Recipe");
        sectionName.add("Enhancement_Material");
        sectionName.add("Combat_Supplies");
        sectionName.add("Cooking");
        sectionName.add("Trader");
        sectionName.add("Adventurer_Tome");
        sectionName.add("Sailing");
        sectionName.add("Pets");
        sectionName.add("Mount");
        sectionName.add("Gem_Chest");
    }

    public static void main(String[] args) throws IOException {

        MongoEditor mongoEditor = new MongoEditor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean exe = true;

        while (exe) {
            System.out.print("""
                    MongoEditor App :\s
                    1) Create all collection default for Market database.
                    2) Delete all collection for Market database.
                    3) Reset all collection for Market database.
                    0) Exit App.
                    Pick a number :\s""");

            String input = reader.readLine();

            switch (input) {
                case "0" -> exe = false;
                case "1" -> {
                    mongoEditor.create();
                    System.out.println("Create done\n");
                }
                case "2" -> {
                    mongoEditor.delete();
                    System.out.println("Delete done\n");
                }
                case "3" -> {
                    mongoEditor.delete();
                    System.out.println();
                    mongoEditor.create();
                    System.out.println("Reset done\n");
                }
                default -> System.out.println("Number not available");
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

        mongoEditor.close();

        System.exit(0);
    }

    private void close() {
        mongoClient.close();
    }

    public void delete() {
        for (String name : sectionName) {
            System.out.println("Dropping Collection : " + name);
            MongoCollection<Document> collection = marketDatabase.getCollection(name);
            collection.drop();
        }
    }

    public void create() {
        for (String name : sectionName) {
            try {
                marketDatabase.createCollection(name);
                System.out.println("Creating Collection : " + name);
            } catch (MongoCommandException e) {
                System.err.println("Error : MongoCommandException " + e.getMessage());
            }
        }
    }

}
