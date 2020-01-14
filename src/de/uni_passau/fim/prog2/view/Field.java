package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.GuiToModel;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Field extends JPanel {

    private static final Color fieldColor = new Color(0, 180, 0);

    private static final int borderOfStone = 10;

    private Color colorOfStone;

    Field(int row, int col, GameBoard parent) {
        setBackground(fieldColor);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (GuiToModel.move(row, col)) {
                    parent.update();
                    if (!checkGameOver() && !checkMissTurn(Player.HUMAN)) {
                        GuiToModel.machineMove();
                        parent.update();
                        while (!checkGameOver()
                                && checkMissTurn(Player.MACHINE)) {
                            GuiToModel.machineMove();
                            parent.update();
                        }
                    }
                } else {
                    showJOptionPane("Invalid Move or game over or machine turn!");
                }
            }
        });
    }

    void setStone(Color colorOfStone) {
        this.colorOfStone = colorOfStone;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        drawFieldLines(graphics);
        if (colorOfStone != null) {
            drawStone(graphics);
        }
    }

    private void drawFieldLines(Graphics2D graphics) {
        int width = getWidth();
        int height = getHeight();

        graphics.drawLine(0, 0, width, 0);
        graphics.drawLine(0, 0, 0, height);
        graphics.drawLine(width, 0, width, height);
        graphics.drawLine(0, height, width, height);
    }

    private void drawStone(Graphics2D graphics) {
        assert colorOfStone != null : "Player of stone cannot be undefined!";

        graphics.setColor(colorOfStone);
        graphics.fillOval(borderOfStone, borderOfStone,
                getWidth() - borderOfStone * 2,
                getHeight() - borderOfStone * 2);
    }

    private boolean checkMissTurn(Player lastMovingPlayer) {
        assert lastMovingPlayer != null : "Last player cannot be undefined!";

        if (GuiToModel.next() == lastMovingPlayer) {
            if (lastMovingPlayer == Player.HUMAN) {
                showJOptionPane("Machine has to miss a turn!");
            } else {
                showJOptionPane("Human has to miss a turn!");
            }
            return true;
        }
        return false;
    }

    private boolean checkGameOver() {
        if (GuiToModel.gameOver()) {
            Player winner = GuiToModel.getWinner();

            if (winner == Player.MACHINE) {
                showJOptionPane("The bot has won");
            } else if (winner == Player.HUMAN) {
                showJOptionPane("The human has Won!");
            } else {
                showJOptionPane("Tie Game");
            }
            return true;
        }
        return false;
    }

    private void showJOptionPane(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Message", JOptionPane.INFORMATION_MESSAGE);
    }
}
