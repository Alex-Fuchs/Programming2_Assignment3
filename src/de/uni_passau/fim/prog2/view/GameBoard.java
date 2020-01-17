package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.Board;
import de.uni_passau.fim.prog2.model.DisplayData;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Entspricht der visuellen Darstellung des Spielbretts inkl aller Indexe an
 * den Spielbrettseiten.
 *
 * @version 15.01.20
 * @author -----
 */
class GameBoard extends JPanel {

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
     * @see                 #createGridBagConstraints(int[])
     */
    private void addFields() {
        final int rightBorder = 8;
        JPanel gameBoard = new JPanel(new GridLayout(Board.SIZE, Board.SIZE));
        MouseAdapter mouseAdapter = createMouseAdapter();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int u = 0; u < Board.SIZE; u++) {
                gameBoard.add(new Field(i + 1, u + 1, mouseAdapter));
            }
        }

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

    /**
     * Kreiert einen {@code MouseAdapter}, der für das Ziehen per Mausklick
     * zuständig ist. Der {@code MouseAdapter} wurde nach {@code GameBoard}
     * ausgelagert, da so lediglich eine Instanz für alle Spielfelder
     * erstellt werden muss und diese wird den Feldern im Konstruktor
     * hinzugefügt.
     *
     * @return      Entspricht einem {@code MouseAdapter} für alle
     *              {@code Fields}.
     */
    private MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {

            /**
             * Entspricht der Spiellogik, auf die zugriffen werden muss, wenn
             * ein Stein gelegt wird.
             */
            private DisplayData displayData = DisplayData.getInstance();

            /**
             * Führt einen Zug des Menschen bei einem Mausklick aus, falls
             * dieser an der Reihe ist, das Spiel nicht vorbei ist und der Zug
             * legal ist. Die Maschine zieht sofort darauf. Es werden bei Ende
             * des Spiels oder Aussetzen eines Spieler Meldungen ausgegeben.
             *
             * @param e     Entspricht dem Event des Mausklicks.
             * @see         #checkGameOver()
             * @see         #checkMissTurn(Player)
             * @see         DisplayData#move(int, int)
             * @see         DisplayData#machineMove()
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Field field = (Field) e.getComponent();
                if (displayData.move(field.getRow(), field.getCol())) {
                    if (!checkGameOver() && !checkMissTurn(Player.HUMAN)) {
                        displayData.machineMove();
                        while (!checkGameOver()
                                && checkMissTurn(Player.MACHINE)) {
                            displayData.machineMove();
                        }
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            /**
             * Überprüft, ob ein Spieler aussetzen muss und gibt darauf eine
             * Meldung aus.
             *
             * @param lastMovingPlayer  Entspricht dem zuletzt gezogenen
             *                          Spieler.
             * @return                  Gibt {@code true} zurück, falls ein
             *                          Spieler aussetzen muss, ansonsten
             *                          {@code false}.
             * @see                     GameBoard#showJOptionPane(String)
             * @see                     DisplayData#next()
             */
            private boolean checkMissTurn(Player lastMovingPlayer) {
                assert lastMovingPlayer != null : "Last player cannot be null!";

                if (displayData.next() == lastMovingPlayer) {
                    if (lastMovingPlayer == Player.HUMAN) {
                        showJOptionPane("Machine has to miss a turn!");
                    } else {
                        showJOptionPane("Human has to miss a turn!");
                    }
                    return true;
                }
                return false;
            }

            /**
             * Überprüft, ob das Spiel vorbei ist und gibt darauf eine Meldung
             * aus.
             *
             * @return      Gibt {@code true} zurück, falls das Spiel vorbei
             *              ist, ansonsten {@code false}.
             * @see         GameBoard#showJOptionPane(String)
             * @see         DisplayData#isGameOver()
             * @see         DisplayData#getWinner()
             */
            private boolean checkGameOver() {
                if (displayData.isGameOver()) {
                    Player winner = displayData.getWinner();

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
        };
    }

    /**
     * Gibt eine Informationsmeldung aus.
     *
     * @param message       Entspricht dem Text, der in der Meldung ausgegeben
     *                      werden soll.
     */
    private void showJOptionPane(String message) {
        assert message != null : "Message cannot be null!";

        JOptionPane.showMessageDialog(null, message,
                "Information Message", JOptionPane.INFORMATION_MESSAGE);
    }
}
