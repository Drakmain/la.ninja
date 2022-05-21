import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class App {

    private final Overlay overlay;
    private final ITesseract instance;
    private final Screen screen;
    private final Robot robot;

    public App(Overlay overlay, Robot robot, Screen screen) {
        this.instance = new Tesseract();
        this.instance.setDatapath(".\\tessdata");

        this.screen = screen;
        this.robot = robot;
        this.overlay = overlay;
    }

    public static int rand(int min, int max) {
        return (int) (min + (Math.random() * (max - min)));
    }

    public Items pullSection(int x, int y, String name) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        Main.log(this.overlay, "Lost Ark " + name + " : Start Pulling Section");
        this.overlay.setSection("Section : " + name);

        Items items = new Items(new Date(currentTimeMillis()), name);
        int page;

        Main.log(this.overlay, "Lost Ark " + name + " : Open");
        this.robot.mouseMove(x, y);
        Thread.sleep(rand(100, 150));
        this.robot.mouseMove(x + 1, y - 1);
        Thread.sleep(rand(100, 150));
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(rand(100, 150));
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1000);

        Main.log(this.overlay, "Lost Ark " + name + " : All");
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
            Main.log(this.overlay, "Lost Ark " + items.getName() + " : Start Pulling Page " + i + 1);
            this.overlay.setPage("Page : " + (i + 1) + "/" + page);

            items.pull(this.screen.shot(this.robot), this.instance, this.overlay);

            this.robot.mouseMove(1147, 899);
            Thread.sleep(rand(100, 150));
            this.robot.mouseMove(1146, 898);
            Thread.sleep(rand(100, 150));
            this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(rand(100, 150));
            this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(1000);
        }

        Main.log(overlay, "Lost Ark " + name + " : Close");
        this.robot.mouseMove(x, y);
        Thread.sleep(rand(100, 150));
        this.robot.mouseMove(x - 1, y + 1);
        Thread.sleep(rand(100, 150));
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(rand(100, 150));
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        System.out.println(items);

        long totalTime = System.currentTimeMillis() - timeStart;
        Main.log(overlay, "Lost Ark " + name + " : End Pulling Section, took " + totalTime + "ms");

        return items;
    }

    public int getPagesNumber() {
        String[] output = new String[0];
        BufferedImageEditor screenBIE = new BufferedImageEditor();
        Rectangle rectangle = new Rectangle(1018, 886, 113, 26);

        screenBIE.setBufferedImage(this.screen.shot(this.robot));

        screenBIE.invertColors();

        screenBIE.setBufferedImage(screenBIE.crop(rectangle));

        screenBIE.getScaledInstance(4);

        try {
            output = this.instance.doOCR(screenBIE.getBufferedImage()).trim().split("/");
        } catch (TesseractException e) {
            Main.logError(overlay, "TesseractException : " + e.getMessage());
            System.exit(4);
        }
        try {
            return Integer.parseInt(output[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            Main.logError(overlay, "ArrayIndexOutOfBoundsException : " + e.getMessage());
            return 1;
        }
    }

    public void waitForText(String string, Rectangle rectangle) throws InterruptedException {
        String output = "";
        BufferedImageEditor screenBIE = new BufferedImageEditor();

        long timeStart = System.currentTimeMillis();
        while (!output.contains(string)) {

            screenBIE.setBufferedImage(this.screen.shot(this.robot));

            screenBIE.invertColors();

            screenBIE.setBufferedImage(screenBIE.crop(rectangle));

            try {
                output = this.instance.doOCR(screenBIE.getBufferedImage()).trim();
            } catch (TesseractException e) {
                Main.logError(overlay, "TesseractException : " + e.getMessage());
                System.exit(4);
            }

            System.out.println("Trying " + string + " : " + output);

            Thread.sleep(1000);
        }

        long totalTime = System.currentTimeMillis() - timeStart;
        System.out.println("Time taken " + totalTime + "ms");
    }

    public boolean checkForText(String string, Rectangle rectangle) {
        String output = "";
        BufferedImageEditor screenBIE = new BufferedImageEditor();

        screenBIE.setBufferedImage(this.screen.shot(this.robot));

        screenBIE.invertColors();

        screenBIE.setBufferedImage(screenBIE.crop(rectangle));

        try {
            output = this.instance.doOCR(screenBIE.getBufferedImage()).trim();
        } catch (TesseractException e) {
            Main.logError(overlay, "TesseractException : " + e.getMessage());
            System.exit(4);
        }

        System.out.println(output.contains(string));
        return output.contains(string);
    }
}
