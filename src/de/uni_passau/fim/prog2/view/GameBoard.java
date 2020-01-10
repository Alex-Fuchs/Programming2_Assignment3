package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.Board;

import javax.swing.*;
import java.awt.*;

class GameBoard extends JPanel {

    GameBoard() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        gridBagLayout.columnWidths = new int[] {0, 0, 0};
        gridBagLayout.rowHeights = new int[] {0, 0, 0};
        gridBagLayout.columnWeights = new double[] {0.0, 1.0, 1.0E-4};
        gridBagLayout.rowWeights = new double[] {0.0, 1.0, 1.0E-4};

        addHorizontalIndexes();
        addVerticalIndexes();
        addFields();
    }

    private void addFields() {
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(Board.SIZE, Board.SIZE));

        for (int i = 0; i < Board.SIZE; i++) {
            for (int u = 0; u < Board.SIZE; u++) {
                fields.add(new Field());
            }
        }

        int[] parameters = {1, 1, 0, 0, 0, 8};
        add(fields, createGridBagConstraints(parameters));
    }

    private void addVerticalIndexes() {
        JPanel verticalIndexes = new JPanel();
        verticalIndexes.setLayout(new GridLayout(Board.SIZE, 1));

        for (int i = 1; i <= Board.SIZE; i++) {
            verticalIndexes.add(createIndexJLabel(String.valueOf(i)));
        }

        int[] parameters = {0, 1, 0, 7, 0, 5};
        add(verticalIndexes, createGridBagConstraints(parameters));
    }

    private void addHorizontalIndexes() {
        JPanel horizontalIndexes = new JPanel();
        horizontalIndexes.setLayout(new GridLayout(1, Board.SIZE));

        for (int i = 1; i <= Board.SIZE; i++) {
            horizontalIndexes.add(createIndexJLabel(String.valueOf(i)));
        }

        int[] parameters = {1, 0, 3, 0, 3, 8};
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
}
