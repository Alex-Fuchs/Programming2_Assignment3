package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.observer.Observable;
import de.uni_passau.fim.prog2.observer.Observer;
import de.uni_passau.fim.prog2.model.DisplayData;
import de.uni_passau.fim.prog2.model.Board;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Implementiert die visuelle Darstellung des Spielbretts inkl aller Indexe an
 * den Spielbrettseiten und ermöglicht Mausinteraktionen mit dem Spielbrett.
 * Die Klasse implementiert das Interface {@code Observer}, um von der
 * Spiellogik bei einer neuen Spielsituation bzw auszugebenden Meldungen
 * benachrichtigt werden zu können.
 *
 * @version 15.01.20
 * @author -----
 */
class GameBoard extends JPanel implements Observer {

    /**
     * Entspricht den Feldern des Spielbretts.
     */
    private Field[][] fields;

    /**
     * Kreiert ein Spielfeld mit allen Feldern und Indexen an den Seitenrändern.
     * Das Spielfeld ermöglicht eine Interaktion mit der Maus, um als Mensch
     * zu ziehen.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @see                 #addHorizontalIndexes()
     * @see                 #addVerticalIndexes()
     * @see                 #addFields(DisplayData)
     */
    GameBoard(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[]{0.0, 1.0};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0};
        setLayout(gridBagLayout);

        fields = new Field[Board.SIZE][Board.SIZE];
        displayData.addObserver(this);
        addHorizontalIndexes();
        addVerticalIndexes();
        addFields(displayData);
    }

    /**
     * Updatet die visuelle Darstellung des Spielfeldes und gibt nur Meldungen
     * aus, ob ein Spieler aussetzen muss oder das Spiel vorbei ist, falls
     * die Maschine oder der Mensch gezogen ist bzw {@code arg} {@code true}
     * ist, da andernfalls bei {@link DisplayData#undo()} nochmal eine Meldung
     * ausgegeben wird, dass ein Spieler aussetzen muss.
     *
     * @param o                             Entspricht der Spiellogik, von der
     *                                      Informationen benötigt werden.
     * @param arg                           Entspricht einem Flag, ob ein
     *                                      Spieler gezogen ist. Wird für die
     *                                      Ausgabe von Meldungen benötigt.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code o} kein
     *                                      Objekt von {@code DisplayData} oder
     *                                      {@code arg} kein {@code Boolean}
     *                                      ist.
     * @see                                 #updateGameField(DisplayData)
     * @see                                 #checkGameOver(DisplayData)
     * @see                                 #checkMissTurn(DisplayData)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof DisplayData && arg instanceof Boolean) {
            DisplayData displayData = (DisplayData) o;
            updateGameField(displayData);
            if (((boolean) arg) && !checkGameOver(displayData)) {
                checkMissTurn(displayData);
            }
        } else {
            throw new IllegalArgumentException("Observable or arg is illegal!");
        }
    }

    /**
     * Erzeugt alle Spielfelder und fügt diese dem Spielbrett hinzu.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @see                 #createGridBagConstraints(int[])
     * @see                 #initializeFields()
     * @see                 FieldMouseAdapter
     */
    private void addFields(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        final int rightBorder = 8;
        JPanel gameBoard = new JPanel(new GridLayout(Board.SIZE, Board.SIZE));
        MouseAdapter fieldMouseAdapter = new FieldMouseAdapter(displayData);
        for (int row = 1; row <= Board.SIZE; row++) {
            for (int col = 1; col <= Board.SIZE; col++) {
                fields[row - 1][col - 1]
                        = new Field(row, col, fieldMouseAdapter);
                gameBoard.add(fields[row - 1][col - 1]);
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
     * Kreiert {@code JLabel} für die Indexanzeige.
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
     * Initialisiert das Spielfeld, indem die Steine der Anfangsspielsituation
     * visuell dargestellt werden.
     *
     * @see         #setPlayerOfField(int, int, Player)
     */
    private void initializeFields() {
        int median = Board.SIZE / 2;
        setPlayerOfField(median, median, Player.MACHINE);
        setPlayerOfField(median, median + 1, Player.HUMAN);
        setPlayerOfField(median + 1, median, Player.HUMAN);
        setPlayerOfField(median + 1, median + 1, Player.MACHINE);
    }

    /**
     * Updatet die visuelle Darstellung des Spielbretts.
     *
     * @param displayData       Entspricht der Spiellogik, von der
     *                          Informationen benötigt werden.
     * @see                     #setPlayerOfField(int, int, Player)
     * @see                     DisplayData#getSlot(int, int)
     */
    private void updateGameField(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        for (int row = 1; row <= Board.SIZE; row++) {
            for (int col = 1; col <= Board.SIZE; col++) {
                Player playerOfSlot = displayData.getSlot(row, col);
                setPlayerOfField(row, col, playerOfSlot);
            }
        }
    }

    /**
     * Teilt dem Feld mit der Position ({@code row}, {@code col}) mit, dass
     * nun {@code playerOfField} seinen Stein auf das Feld gesetzt hat,
     * bzw bei {@code null} nun kein Stein mehr auf dem Feld liegt.
     *
     * @param row               Entspricht der Zeile des Feldes.
     * @param col               Entspricht der Spalte des Feldes.
     * @param playerOfField     Entspricht dem Spieler, der auf das Feld seinen
     *                          Stein gesetzt hat, wobei {@code null} für
     *                          keinen Stein steht.
     * @see                     Field#setColorOfStone(Color)
     */
    private void setPlayerOfField(int row, int col, Player playerOfField) {
        assert row > 0 && row <= Board.SIZE : "The row is illegal!";
        assert col > 0 && col <= Board.SIZE : "The col is illegal!";

        if (playerOfField != null) {
            fields[row - 1][col - 1]
                    .setColorOfStone(playerOfField.getColorOfPlayer());
        } else {
            fields[row - 1][col - 1].setColorOfStone(null);
        }
    }

    /**
     * Überprüft, ob das Spiel vorbei ist und gibt darauf eine Meldung aus.
     *
     * @param displayData   Entspricht der Spiellogik, von der Informationen
     *                      benötigt werden.
     * @return              Gibt {@code true} zurück, falls das Spiel vorbei
     *                      ist, ansonsten {@code false}.
     * @see                 #showJOptionPane(String, String, int)
     * @see                 DisplayData#isGameOver()
     * @see                 DisplayData#getWinner()
     */
    private boolean checkGameOver(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        if (displayData.isGameOver()) {
            Player winner = displayData.getWinner();
            String message;
            if (winner == Player.MACHINE) {
                message = "The bot has won";
            } else if (winner == Player.HUMAN) {
                message = "The human has won!";
            } else {
                message = "Tie game";
            }
            showJOptionPane(message, "Game over!",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * Überprüft, ob ein Spieler aussetzen muss und gibt darauf eine
     * Meldung aus.
     *
     * @param displayData   Entspricht der Spiellogik, von der Informationen
     *                      benötigt werden.
     * @see                 #showJOptionPane(String, String, int)
     * @see                 DisplayData#lastPlayer()
     * @see                 DisplayData#next()
     */
    private void checkMissTurn(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        Player lastPlayer = displayData.lastPlayer();
        if (displayData.next() == lastPlayer) {
            String message;
            if (lastPlayer == Player.HUMAN) {
                message = "Machine has to miss a turn!";
            } else {
                message = "Human has to miss a turn!";
            }
            showJOptionPane(message, "Miss Turn!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Gibt eine Meldung aus, wobei die Nachricht, der Titel und der Typ der
     * Nachricht angegeben werden kann.
     *
     * @param message       Entspricht dem Text, der in der Meldung ausgegeben
     *                      werden soll.
     * @param title         Entspricht dem Titel der Meldung.
     * @param type          Entspricht dem Typen der Meldung.
     */
    private void showJOptionPane(String message, String title, int type) {
        assert message != null : "Message cannot be null!";
        assert title != null : "The title cannot be null!";

        JOptionPane.showMessageDialog(null, message, title, type);
    }

    /**
     * Entspricht einem {@code MouseAdapter}, der das Ziehen des Menschen
     * durch einen Mausklick auf ein Feld ermöglicht, wobei lediglich ein
     * Objekt für alle Felder verwendet wird.
     */
    private final class FieldMouseAdapter extends MouseAdapter {

        /**
         * Entspricht der Spiellogik, auf die zugriffen werden muss, wenn ein
         * Stein gelegt wird.
         */
        private DisplayData displayData;

        /**
         * Kreiert einen MouseAdapter für das Feld des Spielbretts, der für
         * die Mausinteraktion zuständig ist.
         *
         * @param displayData   Entspricht der Spiellogik, auf die bei einem
         *                      Mausklick zugegriffen werden muss.
         */
        private FieldMouseAdapter(DisplayData displayData) {
            assert displayData != null : "DisplayData cannot be null!";

            this.displayData = displayData;
        }

        /**
         * Führt einen Zug des Menschen bei einem Mausklick aus, falls dieser
         * an der Reihe ist, das Spiel nicht vorbei ist und der Zug legal ist.
         * Die Maschine zieht sofort darauf solange sie kann.
         *
         * @param e     Entspricht dem Event des Mausklicks.
         * @see         DisplayData#move(int, int)
         * @see         DisplayData#machineMove()
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (!displayData.isGameOver()) {
                if (displayData.next() == Player.HUMAN
                        && !displayData.hasChanged()) {
                    Field field = (Field) e.getComponent();
                    if (displayData.move(field.getRow(), field.getCol())) {
                        displayData.machineMove();
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                } else {
                    showJOptionPane("Machine needs more time for Moving!",
                            "Not your Turn!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                showJOptionPane("Please start a new game, the game is over!",
                        "Game already over!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
