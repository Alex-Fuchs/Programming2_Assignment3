package de.uni_passau.fim.prog2.view;

import javax.swing.*;
import java.awt.*;

class UserInterface {

    public static void main(String[] args) {
        /*try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        JFrame frame = new JFrame("Reversi");
        frame.setLayout(new BorderLayout());

        Container contentPane = frame.getContentPane();
        contentPane.add(new GameBoard(), BorderLayout.CENTER);
        contentPane.add(new Menu(), BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}
