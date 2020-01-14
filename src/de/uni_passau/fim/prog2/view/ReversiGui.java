package de.uni_passau.fim.prog2.view;

import javax.swing.*;
import java.awt.*;

class ReversiGui extends JFrame {

    private Menu menu;

    private GameBoard gameBoard;

    private ReversiGui() {
        final int defaultHeight = 600;
        final int defaultWidth = 600;

        setTitle("Reversi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(defaultWidth, defaultHeight);
        addItems();
        setVisible(true);
    }

    Menu getMenu() {
        return menu;
    }

    GameBoard getGameBoard() {
        return gameBoard;
    }

    private void addItems() {
        setLayout(new BorderLayout());
        Container contentPane = getContentPane();

        gameBoard = new GameBoard(this);
        menu = new Menu(this);

        contentPane.add(gameBoard, BorderLayout.CENTER);
        contentPane.add(menu, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ReversiGui reversiGui = new ReversiGui();
            }
        });
    }
}
