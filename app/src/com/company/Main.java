package com.company;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// exit code :
// 1 : No .env file (IOException)
// 2 : AWTException
// 3 : IOException
// 4 : TesseractException
// 5 : Miss reading of page number (ArrayIndexOutOfBoundsException)

public class Main {

    public static void main(String[] args) throws InterruptedException {
        long timeStart = System.currentTimeMillis();

        Overlay overlay = new Overlay();

        Main.log(overlay, "Start main");

        Screen screen = new Screen();
        Robot robot = null;

        try {
            robot = new Robot(screen.getGraphicsDevice());
        } catch (AWTException e) {
            Main.logError(overlay, "AWTException : " + e.getMessage());
            System.exit(2);
        }

        App app = new App(overlay, robot, screen);

        /////////////////////////////
        // Launching GeForceNow

        String command = System.getenv("LOCALAPPDATA") + "\\NVIDIA Corporation\\GeForceNOW\\CEF\\GeForceNOWStreamer.exe\"  --url-route=\"#?cmsId=102074111&launchSource=External&shortName=lost_ark_na_eu_steam&parentGameId=";
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            Main.logError(overlay, "IOException : " + e.getMessage());
            System.exit(3);
        }

        // Launching GeForceNow
        /////////////////////////////

        if (!args[0].equals("skip")) {
            Main.log(overlay, "waitForText Lost Ark Server Mokoko");
            app.waitForText("Mokoko", new Rectangle(793, 586, 58, 22));

            Thread.sleep(1000);

            Main.log(overlay, "Lost Ark Enter");
            robot.mouseMove(943, 920);
            Thread.sleep(150);
            robot.mouseMove(945, 922);
            Thread.sleep(150);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(150);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(10000);

            Main.log(overlay, "Lost Ark Launch");
            robot.mouseMove(843, 1010);
            Thread.sleep(150);
            robot.mouseMove(845, 1012);
            Thread.sleep(150);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(150);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(20000);

            Main.log(overlay, "waitForText Lost Ark In Game Comabt");
            app.waitForText("Combat", new Rectangle(32, 1051, 55, 16));
        }

        Thread.sleep(2000);

        /////////////////////////////
        // Pulling market

        List<Items> sectionItemsList = new ArrayList<>();

        Map<String, Integer> sectionNameMap = new HashMap<>();
        sectionNameMap.put("Skin", 0);
        sectionNameMap.put("Engraving_Recipe", 0);
        sectionNameMap.put("Enhancement_Material", 1);
        sectionNameMap.put("Combat_Supplies", 0);
        sectionNameMap.put("Cooking", 0);
        sectionNameMap.put("Trader", 0);
        sectionNameMap.put("Adventurer_Tome", 0);
        sectionNameMap.put("Sailing", 0);
        sectionNameMap.put("Pets", 0);
        sectionNameMap.put("Mount", 0);
        sectionNameMap.put("Gem_Chest", 0);

        Main.log(overlay, "Lost Ark Alt Y");
        robot.mouseMove(843, 1010);
        robot.keyPress(KeyEvent.VK_ALT);
        Thread.sleep(200);
        robot.keyPress(KeyEvent.VK_Y);
        Thread.sleep(100);
        robot.keyRelease(KeyEvent.VK_Y);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_ALT);

        Thread.sleep(2000);

        for (Map.Entry<String, Integer> section : sectionNameMap.entrySet()) {
            if (section.getValue() == 1) {
                sectionItemsList.add(app.pullSection(386, 441, section.getKey()));

                Thread.sleep(1000);
            }
        }

        // Pulling market
        /////////////////////////////

        /////////////////////////////
        // Connection MongoDB

        String[] env = new String[0];

        try {
            File file = new File("./app/.env");
            BufferedReader br = new BufferedReader(new FileReader(file));
            env = br.readLine().split("=");
        } catch (IOException e) {
            Main.logError(overlay, "IOException : " + e.getMessage());
            System.exit(1);
        }

        String password = URLEncoder.encode(env[1], StandardCharsets.UTF_8);

        ConnectionString connectionString = new ConnectionString("mongodb+srv://Drakmain:" + password + "@economy.qqflq.mongodb.net");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase marketDatabase = mongoClient.getDatabase("Market");

        for (Items items : sectionItemsList) {
            MongoCollection<Document> collection = marketDatabase.getCollection(items.getName());

            collection.insertOne(items.toDocument());
        }

        mongoClient.close();

        // Connection MongoDB
        /////////////////////////////

        long totalTime = System.currentTimeMillis() - timeStart;
        Main.log(overlay, "End main, took " + totalTime + "ms");

        overlay.dispose();

        System.exit(0);
    }

    public static void log(Overlay overlay, String message) {
        overlay.setState("State : " + message);
        System.out.println(message);
    }

    public static void logError(Overlay overlay, String message) {
        overlay.setError("Error " + message);
        System.err.println("Error " + message);
    }

}