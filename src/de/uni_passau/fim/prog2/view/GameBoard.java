package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.Board;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;

/**
 * Entspricht der visuellen Darstellung des Spielbretts inkl aller Indexe an
 * den Spielbrettseiten.
 *
 * @version 15.01.20
 * @author -----
 */
class GameBoard extends JPanel {

    /**
     * Entspricht den Feldern des Spielfelds.
     */
    private Field[][] fields;

    /**
     * Kreiert ein Spielfeld mit allen Feldern und Indexen an den Seitenrändern.
     * Das Spielfeld ermöglicht eine Interaktion mit der Maus für die Züge des
     * Menschen.
     *
     * @see                 #addHorizontalIndexes()
     * @see                 #addVerticalIndexes()
     * @see                 #addFields()
     */
    GameBoard() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[]{0.0, 1.0};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0};
        setLayout(gridBagLayout);

        addHorizontalIndexes();
        addVerticalIndexes();
        addFields();
    }

    /**
     * Erzeugt alle Spielfelder und fügt diese dem Spielbrett hinzu.
     *
     * @see                 #initializeFields()
     * @see                 #createGridBagConstraints(int[])
     */
    private void addFields() {
        final int rightBorder = 8;
        fields = new Field[Board.SIZE][Board.SIZE];
        JPanel gameBoard = new JPanel(new GridLayout(Board.SIZE, Board.SIZE));
        for (int i = 0; i < Board.SIZE; i++) {
            for (int u = 0; u < Board.SIZE; u++) {
                fields[i][u] = new Field(i + 1, u + 1);
                gameBoard.add(fields[i][u]);
            }
        }
        initializeFields();

        int[] parametersForGridBag = {1, 1, 0, 0, 0, rightBorder};
        add(gameBoard, createGridBagConstraints(parametersForGridBag));
    }

    /**
     * Fügt dem Spielbrett die vertikalen Indexe hinzu.
     *
     * @see         #createGridBagConstraints(int[])
     */
    private void addVerticalIndexes() {
        final int rightBorder = 5;
        final int leftBorder = 7;
        JPanel verticalIndexes = new JPanel(new GridLayout(Board.SIZE, 1));
        for (int i = 1; i <= Board.SIZE; i++) {
            verticalIndexes.add(createIndexJLabel(String.valueOf(i)));
        }

        int[] parametersForGridBag = {0, 1, 0, leftBorder, 0, rightBorder};
        add(verticalIndexes, createGridBagConstraints(parametersForGridBag));
    }

    /**
     * Fügt dem Spielbrett die horizontalen Indexe hinzu.
     *
     * @see         #createGridBagConstraints(int[])
     */
    private void addHorizontalIndexes() {
        final int rightBorder = 8;
        final int topBorder = 3;
        final int bottomBorder = 3;
        JPanel horizontalIndexes = new JPanel(new GridLayout(1, Board.SIZE));
        for (int i = 1; i <= Board.SIZE; i++) {
            horizontalIndexes.add(createIndexJLabel(String.valueOf(i)));
        }

        int[] parametersForGridBag
                = {1, 0, topBorder, 0, bottomBorder, rightBorder};
        add(horizontalIndexes, createGridBagConstraints(parametersForGridBag));
    }

    /**
     * Kreiert {@code JLabel} die Indexanzeige.
     *
     * @param text      Entspricht dem Text.
     * @return          Gibt das erzeugte {@code JLabel} zurück.
     */
    private JLabel createIndexJLabel(String text) {
        assert text != null : "Text cannot be null!";

        final Font scoreFont = new Font(null, Font.BOLD, 12);
        JLabel jLabel = new JLabel(text);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setFont(scoreFont);
        return jLabel;
    }

    /**
     * Kreiert ein {@code GridBagConstraints} nach den übergebenen Parametern
     * für das Einfügen von Komponenten im {@code GridBagLayout}.
     *
     * @param parameters    Entspricht den Parametern zur Erzeugung des
     *                      {@code GridBagConstraints}.
     * @return              Gibt das erzeugte {@code GridBagConstraint} zurück.
     */
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

        fields[median][median].initialize(Player.MACHINE);
        fields[median][median + 1].initialize(Player.HUMAN);
        fields[median + 1][median].initialize(Player.HUMAN);
        fields[median + 1][median + 1].initialize(Player.MACHINE);
    }
}
