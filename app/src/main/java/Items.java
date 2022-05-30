import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bson.Document;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Items implements Iterable<Item> {

    private final Date date;
    private final String name;
    private final List<Item> list;

    public Items(String name) {
        this.name = name;
        this.date = new Date(System.currentTimeMillis());
        list = new ArrayList<>();
    }

    public void pull(BufferedImage screen1, BufferedImage screen2, ITesseract instance, Overlay overlay) {
        Rectangle cropRectangle = new Rectangle(909, 307, 133, 51);
        Rectangle t = new Rectangle(605, 309, 291, 46);
        BufferedImageEditor screenNumberBIE = new BufferedImageEditor(screen1);
        BufferedImageEditor screenStringBIE = new BufferedImageEditor(screen2);
        BufferedImageEditor cropBIE = new BufferedImageEditor();
        Item item;
        float f = 0;
        String temp;

        for (int i = 0; i < 10; i++) {
            item = new Item();

            for (int j = 0; j < 3; j++) {
                overlay.setItem("Item : " + i + j);

                cropBIE.setBufferedImage(screenNumberBIE.crop(cropRectangle));

                cropBIE.getScaledInstance(4);

                try {
                    temp = instance.doOCR(cropBIE.getBufferedImage());

                    if (temp.contains(",")) {
                        temp = temp.replace(",", "");
                    }

                    f = Float.parseFloat(temp);
                } catch (TesseractException e) {
                    Main.logError(overlay, "TesseractException : " + e.getMessage());
                    System.exit(4);
                } catch (NumberFormatException e) {
                    f = 0;
                    if (!e.getMessage().equals("empty String")) {
                        Main.logError(overlay, "NumberFormatException : " + this.name + "_" + i + j + " " + e.getMessage());
                        try {
                            Files.createDirectories(Paths.get("./Tesseract_NumberFormatException"));
                            ImageIO.write(cropBIE.getBufferedImage(), "png", new File("./Tesseract_NumberFormatException/" + this.name + "_" + i + j + "_NumberFormatException.png"));
                        } catch (IOException ee) {
                            Main.logError(overlay, "IOException : " + ee.getMessage());
                        }
                    }
                }

                switch (j) {
                    case 0 -> {
                        item.setAvgDay(f);
                        Item.recentPrice(cropRectangle);
                    }
                    case 1 -> {
                        item.setRecent((int) f);
                        Item.lowestPrice(cropRectangle);
                    }
                    case 2 -> {
                        item.setLowest((int) f);
                        Item.avgDayPrice(cropRectangle);
                    }
                }
            }

            cropBIE.setBufferedImage(screenStringBIE.crop(t));

            try {
                String[] oue = instance.doOCR(cropBIE.getBufferedImage()).split("\n");
                if (!oue[0].equals("")) {
                    item.setName(oue[0]);
                    this.add(item);
                }
            } catch (TesseractException e) {
                Main.logError(overlay, "TesseractException : " + e.getMessage());
                System.exit(4);
            }

            Item.nextItem(t);
            Item.nextItem(cropRectangle);
        }
    }

    public Document toDocument() {
        Document doc = new Document();

        for (Item i : this) {
            Document docItem = new Document();
            docItem.put("avgDay", i.getAvgDay());
            docItem.put("recent", i.getRecent());
            docItem.put("lowest", i.getLowest());
            doc.put(i.getName(), docItem);
        }

        doc.put("date", date);

        return doc;
    }

    public void add(Item item) {
        this.list.add(item);
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public List<Item> getList() {
        return list;
    }

    @Override
    public Iterator<Item> iterator() {
        return list.iterator();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Date : ").append(this.getDate());
        string.append("\n");

        string.append("Items : \n");
        for (Item i : this) {
            string.append(i).append("\n");
        }

        string.append("Size : ").append(this.getList().size());
        return string.toString();
    }
}
