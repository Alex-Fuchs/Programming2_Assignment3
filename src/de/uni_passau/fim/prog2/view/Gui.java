package de.uni_passau.fim.prog2.view;

import javax.swing.*;
import java.awt.*;

class Gui extends JFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Gui gui = new Gui();
            }
        });
    }

    private Gui() {
        final int defaultHeight = 600;
        final int defaultWidth = 600;

        setTitle("Reversi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(defaultWidth, defaultHeight);
        addItems();
        setVisible(true);
    }

    private void addItems() {
        setLayout(new BorderLayout());
        Container contentPane = getContentPane();
        contentPane.add(new GameBoard(), BorderLayout.CENTER);
        contentPane.add(new Menu(), BorderLayout.SOUTH);
    }
}
