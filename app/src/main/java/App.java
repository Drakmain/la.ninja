import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bson.Document;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class App {

    private final Overlay overlay;
    private final ITesseract instance;
    private final Screen screen;
    private final Robot robot;
    private final Map<String, Integer> marketSectionNameMap;

    public App(Overlay overlay, Robot robot, Screen screen) {
        this.instance = new Tesseract();
        this.instance.setDatapath(".\\tessdata");

        marketSectionNameMap = new HashMap<>();
        marketSectionNameMap.put("Skin", 0);
        marketSectionNameMap.put("Engraving_Recipe", 0);
        marketSectionNameMap.put("Enhancement_Material", 1);
        marketSectionNameMap.put("Combat_Supplies", 0);
        marketSectionNameMap.put("Cooking", 0);
        marketSectionNameMap.put("Trader", 0);
        marketSectionNameMap.put("Adventurer_Tome", 0);
        marketSectionNameMap.put("Sailing", 0);
        marketSectionNameMap.put("Pets", 0);
        marketSectionNameMap.put("Mount", 0);
        marketSectionNameMap.put("Gem_Chest", 0);

        this.screen = screen;
        this.robot = robot;
        this.overlay = overlay;
    }

    public static int rand(int min, int max) {
        return (int) (min + (Math.random() * (max - min)));
    }

    public static void deamon(Overlay overlay) throws InterruptedException {
        boolean exe = true;

        overlay.log("Starting la.ninja deamon");

        java.util.List<Items> sectionItemsList = new ArrayList<>();

        Screen screen = new Screen();
        Robot robot = null;

        try {
            robot = new Robot(screen.getGraphicsDevice());
        } catch (AWTException e) {
            overlay.logError("AWTException : " + e.getMessage());
            System.exit(2);
        }

        App app = new App(overlay, robot, screen);

        /////////////////////////////
        // Launching GeForceNow

        String command = System.getenv("LOCALAPPDATA") + "\\NVIDIA Corporation\\GeForceNOW\\CEF\\GeForceNOWStreamer.exe\" --url-route=\"#?cmsId=102074111&launchSource=External&shortName=lost_ark_na_eu_steam&parentGameId=";
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            overlay.logError("IOException : " + e.getMessage());
            System.exit(3);
        }

        // Launching GeForceNow
        /////////////////////////////

        overlay.log("waitForText Lost Ark Server Mokoko");
        app.waitForText("Mokoko", new Rectangle(793, 586, 58, 22));

        overlay.log("Lost Ark Enter");
        robot.keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_ENTER);

        overlay.log("waitForText Lost Ark Launch");
        app.waitForText("Launch", new Rectangle(819, 1000, 66, 33));

        overlay.log("Lost Ark Launch");
        robot.keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_ENTER);

        overlay.log("waitForText Lost Ark In Game Comabt");
        app.waitForText("Combat", new Rectangle(32, 1051, 55, 16));

        Thread.sleep(3000);

        overlay.log("Lost Ark Escape Pop Up");
        do {
            robot.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(200);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(1000);
        } while (!app.checkForText("Game Menu", new Rectangle(1024, 282, 117, 19)));

        robot.keyPress(KeyEvent.VK_ESCAPE);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_ESCAPE);

        Thread.sleep(1000);

        overlay.log("Lost Ark Alt Y");
        robot.mouseMove(843, 1010);
        robot.keyPress(KeyEvent.VK_ALT);
        Thread.sleep(200);
        robot.keyPress(KeyEvent.VK_Y);
        Thread.sleep(100);
        robot.keyRelease(KeyEvent.VK_Y);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_ALT);

        Thread.sleep(1000);

        while (exe) {

            /////////////////////////////
            // Pulling Market

            for (Map.Entry<String, Integer> section : app.marketSectionNameMap.entrySet()) {
                if (section.getValue() == 1) {
                    sectionItemsList.add(app.pullSection(386, 441, section.getKey()));

                    Thread.sleep(1000);
                }
            }

            // Pulling Market
            /////////////////////////////

            /////////////////////////////
            // Connection to MongoDB + Insert into Market Database

            //String password = URLEncoder.encode(, StandardCharsets.UTF_8);

            ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
            MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
            MongoClient mongoClient = MongoClients.create(settings);
            MongoDatabase marketDatabase = mongoClient.getDatabase("Market");

            for (Items items : sectionItemsList) {
                MongoCollection<Document> collection = marketDatabase.getCollection(items.getName());

                collection.insertOne(items.toDocument());
            }

            mongoClient.close();

            // Connection to MongoDB + Insert into Market Database
            /////////////////////////////

            overlay.setSection("Section : ");
            overlay.setItem("Item : ");
            overlay.setPage("Page : ");

            // Waiting for 5 hour for next pull
            try {
                Thread.sleep(3600000 * 5);
            } catch (InterruptedException e) {
                overlay.logError("InterruptedException : " + e.getMessage());
            }
        }
    }

    public static void debug(Overlay overlay, String arg) throws InterruptedException {
        long timeStart = System.currentTimeMillis();

        overlay.log("Starting la.ninja debug");

        List<Items> sectionItemsList = new ArrayList<>();

        Screen screen = new Screen();
        Robot robot = null;

        try {
            robot = new Robot(screen.getGraphicsDevice());
        } catch (AWTException e) {
            overlay.logError("AWTException : " + e.getMessage());
            System.exit(2);
        }

        App app = new App(overlay, robot, screen);

        /////////////////////////////
        // Launching GeForceNow

        String command = System.getenv("LOCALAPPDATA") + "\\NVIDIA Corporation\\GeForceNOW\\CEF\\GeForceNOWStreamer.exe\" --url-route=\"#?cmsId=102074111&launchSource=External&shortName=lost_ark_na_eu_steam&parentGameId=";
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            overlay.logError("IOException : " + e.getMessage());
            System.exit(3);
        }

        // Launching GeForceNow
        /////////////////////////////

        if (!arg.equals("skip")) {
            overlay.log("waitForText Lost Ark Server Mokoko");
            app.waitForText("Mokoko", new Rectangle(793, 586, 58, 22));

            overlay.log("Lost Ark Enter");
            robot.keyPress(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            robot.keyRelease(KeyEvent.VK_ENTER);

            overlay.log("waitForText Lost Ark Launch");
            app.waitForText("Launch", new Rectangle(819, 1000, 66, 33));

            overlay.log("Lost Ark Launch");
            robot.keyPress(KeyEvent.VK_ENTER);
            Thread.sleep(200);
            robot.keyRelease(KeyEvent.VK_ENTER);

            overlay.log("waitForText Lost Ark In Game Comabt");
            app.waitForText("Combat", new Rectangle(32, 1051, 55, 16));

            Thread.sleep(3000);

            overlay.log("Lost Ark Escape Pop Up");
            do {
                robot.keyPress(KeyEvent.VK_ESCAPE);
                Thread.sleep(200);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
                Thread.sleep(1000);
            } while (!app.checkForText("Game Menu", new Rectangle(1024, 282, 117, 19)));

            robot.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(200);
            robot.keyRelease(KeyEvent.VK_ESCAPE);

            Thread.sleep(1000);
        } else {
            Thread.sleep(3000);

            if (app.checkForText("Market", new Rectangle(924, 102, 72, 22))) {
                overlay.log("Lost Ark Alt Y");
                robot.mouseMove(843, 1010);
                robot.keyPress(KeyEvent.VK_ALT);
                Thread.sleep(200);
                robot.keyPress(KeyEvent.VK_Y);
                Thread.sleep(100);
                robot.keyRelease(KeyEvent.VK_Y);
                Thread.sleep(200);
                robot.keyRelease(KeyEvent.VK_ALT);
            }
        }

        robot.mouseMove(843, 1010);
        robot.keyPress(KeyEvent.VK_ALT);
        Thread.sleep(200);
        robot.keyPress(KeyEvent.VK_Y);
        Thread.sleep(100);
        robot.keyRelease(KeyEvent.VK_Y);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_ALT);

        Thread.sleep(2000);

        /////////////////////////////
        // Pulling Market

        for (Map.Entry<String, Integer> section : app.marketSectionNameMap.entrySet()) {
            if (section.getValue() == 1) {
                sectionItemsList.add(app.pullSection(386, 441, section.getKey()));

                Thread.sleep(1000);
            }
        }

        // Pulling Market
        /////////////////////////////

        /////////////////////////////
        // Connection to MongoDB + Insert into Market Database

        //String password = URLEncoder.encode(, StandardCharsets.UTF_8);

        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase marketDatabase = mongoClient.getDatabase("Market");

        for (Items items : sectionItemsList) {
            MongoCollection<Document> collection = marketDatabase.getCollection(items.getName());

            collection.insertOne(items.toDocument());
        }

        MongoCollection<Document> nameList = marketDatabase.getCollection("Name_List");

        for (Items items : sectionItemsList) {
            FindIterable<Document> findIterable = nameList.find(eq("section", items.getName()));
            Document d = findIterable.first();
            for (Item item : items) {
                if (!d.containsKey(item.getName())) {
                    d.put(String.valueOf(d.size() - 3 + 1), item.getName());
                }
            }
            nameList.replaceOne(eq("section", items.getName()), d);
        }

        mongoClient.close();

        // Connection to MongoDB + Insert into Market Database
        /////////////////////////////

        overlay.setSection("Section : ");
        overlay.setItem("Item : ");
        overlay.setPage("Page : ");

        synchronized (overlay) {
            overlay.notify();
        }

        long totalTime = System.currentTimeMillis() - timeStart;
        overlay.log("End debug, took " + totalTime + "ms");
    }

    public Items pullSection(int x, int y, String name) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        overlay.log("Lost Ark " + name + " : Start Pulling Section");
        this.overlay.setSection("Section : " + name);

        Items items = new Items(name);
        int page;

        overlay.log("Lost Ark " + name + " : Open");
        this.robot.mouseMove(x, y);
        Thread.sleep(rand(100, 150));
        this.robot.mouseMove(x + 1, y - 1);
        Thread.sleep(rand(100, 150));
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(rand(100, 150));
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1000);

        overlay.log("Lost Ark " + name + " : All");
        this.robot.mouseMove(x, y + 39);
        Thread.sleep(rand(100, 150));
        this.robot.mouseMove(x + 1, y + 40);
        Thread.sleep(rand(100, 150));
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(rand(100, 150));
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1500);

        page = this.getPagesNumber();

        for (int i = 0; i < page; i++) {
            overlay.log("Lost Ark " + items.getName() + " : Start Pulling Page " + i + 1);
            this.overlay.setPage("Page : " + (i + 1) + "/" + page);

            items.pull(this.screen.shot(this.robot), this.screen.shot(this.robot), this.instance, this.overlay);

            this.robot.mouseMove(1147, 899);
            Thread.sleep(rand(100, 150));
            this.robot.mouseMove(1146, 898);
            Thread.sleep(rand(100, 150));
            this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(rand(100, 150));
            this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(1000);
        }

        overlay.log("Lost Ark " + name + " : Close");
        this.robot.mouseMove(x, y);
        Thread.sleep(rand(100, 150));
        this.robot.mouseMove(x - 1, y + 1);
        Thread.sleep(rand(100, 150));
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(rand(100, 150));
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        System.out.println(items);

        long totalTime = System.currentTimeMillis() - timeStart;
        overlay.log("Lost Ark " + name + " : End Pulling Section, took " + totalTime + "ms");

        return items;
    }

    public int getPagesNumber() {
        String[] output = new String[0];
        BufferedImageEditor screenBIE = new BufferedImageEditor();
        Rectangle rectangle = new Rectangle(1018, 886, 113, 26);

        screenBIE.setBufferedImage(this.screen.shot(this.robot));

        screenBIE.invertColorsNumber();

        screenBIE.setBufferedImage(screenBIE.crop(rectangle));

        screenBIE.getScaledInstance(4);

        try {
            output = this.instance.doOCR(screenBIE.getBufferedImage()).trim().split("/");
        } catch (TesseractException e) {
            overlay.logError("TesseractException : " + e.getMessage());
            System.exit(4);
        }
        try {
            return Integer.parseInt(output[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            overlay.logError("ArrayIndexOutOfBoundsException : " + e.getMessage());
            return 1;
        }
    }

    public void waitForText(String string, Rectangle rectangle) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        while (!checkForText(string, rectangle)) {
            Thread.sleep(1000);
        }

        long totalTime = System.currentTimeMillis() - timeStart;
        overlay.log("checkForText " + string + " : Time taken " + totalTime + "ms");
    }

    public boolean checkForText(String string, Rectangle rectangle) {
        String output = "";
        BufferedImageEditor screenBIE = new BufferedImageEditor();

        screenBIE.setBufferedImage(this.screen.shot(this.robot));

        screenBIE.invertColorsString();

        screenBIE.setBufferedImage(screenBIE.crop(rectangle));

        try {
            output = this.instance.doOCR(screenBIE.getBufferedImage()).trim();
        } catch (TesseractException e) {
            overlay.logError("TesseractException : " + e.getMessage());
            System.exit(4);
        }

        overlay.log("checkForText " + string + " : " + output);

        return output.contains(string);
    }
}
