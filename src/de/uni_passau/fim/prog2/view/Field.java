package de.uni_passau.fim.prog2.view;

import javax.swing.*;
import java.awt.*;

class Field extends JPanel {

    private int row;

    private int col;

    Field(int row, int col) {
        this.row = row;
        this.col = col;
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        graphics.drawLine(0, 0, width, 0);
        graphics.drawLine(0, 0, 0, height);
        graphics.drawLine(width, 0, width, height);
        graphics.drawLine(0, height, width, height);
    }
}
