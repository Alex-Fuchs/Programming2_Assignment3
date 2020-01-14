package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.GuiToModel;
import de.uni_passau.fim.prog2.model.Board;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.*;
import java.awt.*;

class GameBoard extends JPanel {

    private static final Color human = Color.BLUE;

    private static final Color machine = Color.RED;

    private ReversiGui parent;

    private Field[][] fields;

    GameBoard(ReversiGui parent) {
        if (parent != null) {
            this.parent = parent;
            GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWeights = new double[]{0.0, 1.0};
            gridBagLayout.rowWeights = new double[]{0.0, 1.0};
            setLayout(gridBagLayout);

            addHorizontalIndexes();
            addVerticalIndexes();
            addFields();
        } else {
            throw new IllegalArgumentException("Parent cannot be undefined!");
        }
    }

    void updateGameBoard() {
        Player[][] visualisation = GuiToModel.getVisualisation();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int u = 0; u < Board.SIZE; u++) {
                Player playerOfSlot = visualisation[i][u];

                if (playerOfSlot == Player.HUMAN) {
                    fields[i][u].setStone(human);
                } else if (playerOfSlot == Player.MACHINE) {
                    fields[i][u].setStone(machine);
                } else {
                    fields[i][u].setStone(null);
                }
            }
        }
    }

    void updateMenu() {
        if (parent != null) {
            Menu menu = parent.getMenu();
            menu.updateScores();
            menu.updateUndo();
        } else {
            throw new IllegalStateException("Cannot be done at building Gui");
        }
    }

    private void addFields() {
        final int rightBorder = 8;
        fields = new Field[Board.SIZE][Board.SIZE];
        JPanel board = new JPanel();

        board.setLayout(new GridLayout(Board.SIZE, Board.SIZE));
        for (int i = 0; i < Board.SIZE; i++) {
            for (int u = 0; u < Board.SIZE; u++) {
                fields[i][u] = new Field(i + 1, u + 1, this);
                board.add(fields[i][u]);
            }
        }

        initializeFields();
        int[] parameters = {1, 1, 0, 0, 0, rightBorder};
        add(board, createGridBagConstraints(parameters));
    }

    private void addVerticalIndexes() {
        final int rightBorder = 5;
        final int leftBorder = 7;

        JPanel verticalIndexes = new JPanel();
        verticalIndexes.setLayout(new GridLayout(Board.SIZE, 1));
        for (int i = 1; i <= Board.SIZE; i++) {
            verticalIndexes.add(createIndexJLabel(String.valueOf(i)));
        }

        int[] parameters = {0, 1, 0, leftBorder, 0, rightBorder};
        add(verticalIndexes, createGridBagConstraints(parameters));
    }

    private void addHorizontalIndexes() {
        final int rightBorder = 8;
        final int topBorder = 3;
        final int bottomBorder = 3;

        JPanel horizontalIndexes = new JPanel();
        horizontalIndexes.setLayout(new GridLayout(1, Board.SIZE));
        for (int i = 1; i <= Board.SIZE; i++) {
            horizontalIndexes.add(createIndexJLabel(String.valueOf(i)));
        }

        int[] parameters = {1, 0, topBorder, 0, bottomBorder, rightBorder};
        add(horizontalIndexes, createGridBagConstraints(parameters));
    }

    private JLabel createIndexJLabel(String text) {
        assert text != null : "Text cannot be undefined!";
        final Font scoreFont = new Font(null, Font.BOLD, 12);

        JLabel jLabel = new JLabel(text);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setFont(scoreFont);
        return jLabel;
    }

    private GridBagConstraints createGridBagConstraints(int[] parameters) {
        assert parameters.length == 6 : "Too less or too much parameter!";

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = parameters[0];
        gridBagConstraints.gridy = parameters[1];
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(parameters[2], parameters[3],
                parameters[4], parameters[5]);
        return gridBagConstraints;
    }

    private void initializeFields() {
        int median = Board.SIZE / 2 - 1;

        fields[median][median].setStone(machine);
        fields[median][median + 1].setStone(human);
        fields[median + 1][median].setStone(human);
        fields[median + 1][median + 1].setStone(machine);
    }
}
