import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void nextItem() {
        Rectangle rectangle = new Rectangle(1, 1, 1, 1);
        Item.nextItem(rectangle);
        assertEquals(new Rectangle(1, 58, 1, 1), rectangle);
    }

    @Test
    void avgDayPrice() {
        Rectangle rectangle = new Rectangle(1, 1, 1, 1);
        Item.avgDayPrice(rectangle);
        assertEquals(new Rectangle(909, 1, 131, 51), rectangle);
    }

    @Test
    void recentPrice() {
        Rectangle rectangle = new Rectangle(1, 1, 1, 1);
        Item.recentPrice(rectangle);
        assertEquals(new Rectangle(1080, 1, 122, 51), rectangle);
    }

    @Test
    void lowestPrice() {
        Rectangle rectangle = new Rectangle(1, 1, 1, 1);
        Item.lowestPrice(rectangle);
        assertEquals(new Rectangle(1241, 1, 122, 51), rectangle);
    }
}