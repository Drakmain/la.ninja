package com.company;

import java.awt.*;

public class Item {

    private String name;
    private float avgDay;
    private int recent;
    private int lowest;

    public Item() {
        this.name = null;
        this.avgDay = 0;
        this.recent = 0;
        this.lowest = 0;
    }

    public static void nextItem(Rectangle rectangle) {
        rectangle.setRect(rectangle.x, rectangle.y + 57, rectangle.width, rectangle.height);
    }

    public static void avgDayPrice(Rectangle rectangle) {
        rectangle.setRect(909, rectangle.y, 131, 51);
    }

    public static void recentPrice(Rectangle rectangle) {
        rectangle.setRect(1080, rectangle.y, 122, 51);
    }

    public static void lowestPrice(Rectangle rectangle) {
        rectangle.setRect(1241, rectangle.y, 122, 51);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAvgDay() {
        return avgDay;
    }

    public void setAvgDay(float avgDay) {
        this.avgDay = avgDay;
    }

    public float getLowest() {
        return lowest;
    }

    public void setLowest(int lowest) {
        this.lowest = lowest;
    }

    public float getRecent() {
        return recent;
    }

    public void setRecent(int recent) {
        this.recent = recent;
    }

    @Override
    public String toString() {
        return "Item{" + "name='" + name + '\'' + ", avgDay=" + avgDay + ", recent=" + recent + ", lowest=" + lowest + '}';
    }

}
