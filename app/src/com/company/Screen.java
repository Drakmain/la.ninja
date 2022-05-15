package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen {

    private final GraphicsEnvironment ge;
    private final GraphicsDevice[] gd;

    private final DisplayMode screenDM;

    private final Rectangle screenSize;

    public Screen() {
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getScreenDevices();
        screenDM = gd[0].getDisplayMode();
        screenSize = new Rectangle(0, 0, screenDM.getWidth(), screenDM.getHeight());
    }

    public GraphicsDevice getGraphicsDevice() {
        return this.gd[0];
    }

    public BufferedImage shot(Robot r) {
        return r.createScreenCapture(this.screenSize);
    }

}
