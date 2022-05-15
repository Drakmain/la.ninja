package com.company;

import javax.swing.*;

public class Overlay extends JFrame {

    private final JPanel panel;
    private final JLabel state;
    private final JLabel section;
    private final JLabel item;
    private final JLabel page;
    private final JLabel error;

    public Overlay() {
        super("la.ninja");

        panel = new JPanel();

        state = new JLabel("State : Start\n");
        section = new JLabel("Section : \n");
        item = new JLabel("Item : \n");
        page = new JLabel("Page : \n");
        error = new JLabel("Error : \n");

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(state);
        panel.add(section);
        panel.add(item);
        panel.add(page);
        panel.add(error);

        this.add(panel);
        this.setAlwaysOnTop(true);
        this.setLocation(0, 0);
        this.setSize(500, 125);
        this.setVisible(true);
    }

    public void setState(String label) {
        this.state.setText(label);
    }

    public void setSection(String label) {
        this.section.setText(label);
    }

    public void setItem(String label) {
        this.item.setText(label);
    }

    public void setPage(String label) {
        this.page.setText(label);
    }

    public void setError(String label) {
        this.error.setText(label);
    }
}
