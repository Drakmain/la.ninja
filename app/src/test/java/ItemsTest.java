import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ItemsTest {

    @Test
    void pull() {
        Items items = new Items("Enhancement_Material_Page1");

        ITesseract instance = new Tesseract();
        instance.setDatapath(".\\tessdata");

        BufferedImageEditor bufferedImageEditor1 = new BufferedImageEditor();
        BufferedImageEditor bufferedImageEditor2 = new BufferedImageEditor();
        try {
            bufferedImageEditor1.setBufferedImage(ImageIO.read(new File(".\\src\\test\\resources\\pullSample.png")));
            bufferedImageEditor2.setBufferedImage(ImageIO.read(new File(".\\src\\test\\resources\\pullSample.png")));
        } catch (IOException e) {
            System.err.println("Error IOException " + e.getMessage());
            throw new RuntimeException(e);
        }

        items.pull(bufferedImageEditor1.getBufferedImage(), bufferedImageEditor2.getBufferedImage(), instance, new Overlay());

        System.out.println(items);
    }
}