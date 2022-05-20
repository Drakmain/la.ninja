package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageEditor {
    private BufferedImage bufferedImage;

    public BufferedImageEditor(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImageEditor() {
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bi) {
        this.bufferedImage = bi;
    }

    public void getScaledInstance(int scale) {
        BufferedImage resized = new BufferedImage(this.getBufferedImage().getWidth() * scale, this.getBufferedImage().getHeight() * scale, BufferedImage.TYPE_INT_RGB);
        Image scaled = this.getBufferedImage().getScaledInstance(this.getBufferedImage().getWidth() * scale, this.getBufferedImage().getHeight() * scale, Image.SCALE_DEFAULT);
        resized.getGraphics().drawImage(scaled, 0, 0, null);
        this.setBufferedImage(resized);
    }

    public BufferedImage crop(Rectangle rectangle) {
        return this.bufferedImage.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void invertColors() {
        Color c;

        for (int i = 0; i < this.bufferedImage.getHeight(); i++) {
            for (int j = 0; j < this.bufferedImage.getWidth(); j++) {
                int p = this.bufferedImage.getRGB(j, i);
                c = new Color(p);
                if ((c.getRed() > 0 && c.getRed() < 75) && (c.getBlue() > 0 && c.getBlue() < 75) && (c.getGreen() > 0 && c.getGreen() < 75)) {
                    c = new Color(255, 255, 255);
                    this.bufferedImage.setRGB(j, i, c.getRGB());
                } else {
                    c = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
                    this.bufferedImage.setRGB(j, i, c.getRGB());
                }
            }
        }
    }
}
