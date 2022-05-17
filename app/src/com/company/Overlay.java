package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class Overlay extends JFrame {

    private final JLabel state;
    private final JLabel section;
    private final JLabel item;
    private final JLabel page;
    private final JLabel error;

    public Overlay() {
        super("la.ninja App Overlay");

        AtomicReference<Thread> mainThread = new AtomicReference<>();
        AtomicReference<String> context = new AtomicReference<>("Deamon");

        Box menuBox = Box.createVerticalBox();

        Box infoBox = Box.createVerticalBox();
        infoBox.setVisible(false);

        /////////////////////////////
        // Main Box

        Box mainBox = Box.createHorizontalBox();

        Border emptyBorder = BorderFactory.createEmptyBorder(0, 3, 0, 3);
        mainBox.setBorder(emptyBorder);

        mainBox.add(menuBox);

        mainBox.add(Box.createRigidArea(new Dimension(3, 0)));

        mainBox.add(infoBox);

        // Main Box
        /////////////////////////////

        /////////////////////////////
        // Menu Box

        String[] options = {"Daemon", "Debug All", "Debug Skip"};

        JComboBox<String> optionsComboBox = new JComboBox<>(options);
        optionsComboBox.setSelectedIndex(0);

        JButton startButton = new JButton("Start");

        JButton stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        startButton.addActionListener((event) -> {
            this.setSize(600, 135);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            optionsComboBox.setEnabled(false);
            infoBox.setVisible(true);

            Thread t = switch (context.get()) {
                case "Debug All" -> new Thread(() -> {
                    try {
                        Main.debug(this, "");
                    } catch (InterruptedException e) {
                        System.err.println("Error InterruptedException : " + e.getMessage());
                    }
                });
                case "Debug Skip" -> new Thread(() -> {
                    try {
                        Main.debug(this, "skip");
                    } catch (InterruptedException e) {
                        Main.logError(this, "InterruptedException : " + e.getMessage());
                    }
                });
                case "Deamon" -> new Thread(() -> Main.deamon(this));
                default -> null;
            };

            mainThread.set(t);
            mainThread.get().start();

            Thread con = new Thread(() -> {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                this.setSize(165, 135);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                optionsComboBox.setEnabled(true);
                infoBox.setVisible(false);
            });

            con.start();
        });

        stopButton.addActionListener((event) -> {
            this.setSize(165, 135);
            mainThread.get().interrupt();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            optionsComboBox.setEnabled(true);
            infoBox.setVisible(false);
        });

        optionsComboBox.addActionListener((event) -> context.set((String) optionsComboBox.getSelectedItem()));

        menuBox.add(optionsComboBox);
        menuBox.add(Box.createRigidArea(new Dimension(0, 5)));
        menuBox.add(startButton);
        menuBox.add(Box.createRigidArea(new Dimension(0, 5)));
        menuBox.add(stopButton);

        // Menu Box
        /////////////////////////////

        /////////////////////////////
        //  Info Box

        state = new JLabel("State : \n");
        section = new JLabel("Section : \n");
        item = new JLabel("Item : \n");
        page = new JLabel("Page : \n");
        error = new JLabel("Error : \n");

        infoBox.add(state);
        infoBox.add(section);
        infoBox.add(item);
        infoBox.add(page);
        infoBox.add(error);

        // Info Box
        /////////////////////////////

        /////////////////////////////
        // Overlay Settings

        this.add(mainBox);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setLocation(0, 0);
        this.setSize(165, 135);
        this.setVisible(true);

        // Overlay Settings
        /////////////////////////////

        /////////////////////////////
        // Size Settings

        optionsComboBox.setMaximumSize(new Dimension(165, 30));
        optionsComboBox.setSize(new Dimension(165, 30));

        startButton.setMaximumSize(new Dimension(65, 30));
        startButton.setSize(new Dimension(65, 30));

        stopButton.setMaximumSize(new Dimension(65, 30));
        stopButton.setSize(new Dimension(65, 30));

        // Size Settings
        /////////////////////////////
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
