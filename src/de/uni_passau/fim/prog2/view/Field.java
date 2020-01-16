package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.Observer.Observable;
import de.uni_passau.fim.prog2.Observer.Observer;
import de.uni_passau.fim.prog2.model.Board;
import de.uni_passau.fim.prog2.model.DisplayData;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Entspricht der visuellen Darstellung eines Feldes, auf das ein Stein gesetzt
 * werden kann. Die Klasse implementiert das Interface {@code Observer}, da
 * diese von {@code DisplayData} bei neuen Spielbrettern geupdatet wird.
 *
 * @version 15.01.20
 * @author -----
 */
class Field extends JPanel implements Observer {

    /**
     * Entspricht der Hintergrundfarbe eines Feldes, die für alle Objekte
     * immer gleich ist.
     */
    private static final Color fieldColor = new Color(0, 180, 0);

    /**
     * Entspricht dem Abstand von der Feldlinie zum Steinrand.
     */
    private static final int borderOfStone = 10;

    /**
     * Entspricht der Zeile des Feldes auf dem Spielbrett, wobei nord-westlich
     * der Ursprung ist.
     */
    private int row;

    /**
     * Entspricht der Spalte des Feldes auf dem Spielbrett, wobei nord-westlich
     * der Ursprung ist.
     */
    private int col;

    /**
     * Entspricht dem Spieler, der auf diesem Feld einen Stein gelegt hat,
     * ansonsten {@code null}.
     */
    private Player playerOfStone;

    /**
     * Entspricht einem {@code MouseAdapter}, der das Ziehen des Menschen
     * durch einen Mausklick auf ein Feld ermöglicht.
     */
    private class FieldMouseAdapter extends MouseAdapter {

        /**
         * Entspricht der Spiellogik, auf die zugriffen werden muss, wenn ein
         * Stein gelegt wird.
         */
        private DisplayData displayData;

        /**
         * Kreiert einen MouseAdapter für das Feld des Spielbretts, der für
         * die Mausinteraktion zuständig ist.
         */
        private FieldMouseAdapter() {
            displayData = DisplayData.getInstance();
        }

        /**
         * Führt einen Zug des Menschen bei einem Mausklick aus, falls dieser
         * an der Reihe ist, das Spiel nicht vorbei ist und der Zug legal ist.
         * Die Maschine zieht sofort darauf. Es werden bei Ende des Spiels
         * oder Aussetzen eines Spieler Meldungen ausgegeben.
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
            if (displayData.move(row, col)) {
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
         * @param lastMovingPlayer  Entspricht dem zuletzt gezogenen Spieler.
         * @return                  Gibt {@code true} zurück, falls ein
         *                          Spieler aussetzen muss, ansonsten
         *                          {@code false}.
         * @see                     #showJOptionPane(String)
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
         * Überprüft, ob das Spiel vorbei ist und gibt darauf eine Meldung aus.
         *
         * @return      Gibt {@code true} zurück, falls das Spiel vorbei ist,
         *              ansonsten {@code false}.
         * @see         #showJOptionPane(String)
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
    }

    /**
     * Kreiert die visuelle Darstellung eines Feldes des Spielbretts, durch
     * die per Mausklick der Mensch ziehen kann.
     *
     * @param row           Entspricht der Zeile des Feldes.
     * @param col           Entspricht der Zeile des Feldes.
     */
    Field(int row, int col) {
        assert row > 0 && row <= Board.SIZE : "Row is illegal!";
        assert col > 0 && col <= Board.SIZE : "Col is illegal!";

        this.row = row;
        this.col = col;
        setBackground(fieldColor);
        addMouseListener(new FieldMouseAdapter());
        DisplayData.getInstance().addObserver(this);
    }

    /**
     * Updatet die visuelle Darstellung des Feldes.
     *
     * @param o     Entspricht der Spiellogik.
     * @see         DisplayData#getSlot(int, int)
     */
    @Override
    public void update(Observable o) {
        if (o != null) {
            Player playerOfStone = ((DisplayData) o).getSlot(row, col);

            if (this.playerOfStone != playerOfStone) {
                this.playerOfStone = playerOfStone;
                repaint();
            }
        } else {
            throw new IllegalArgumentException("Observable cannot be null!");
        }
    }

    void initialize(Player player) {
        playerOfStone = player;
        repaint();
    }

    /**
     * Stellt das Feld visuell dar und zeichnet ggf den Stein des Spielers,
     * der auf das Feld seinen Stein gesetzt hat.
     *
     * @param g     Entspricht der visuellen Darstellungskomponente.
     * @see         #drawFieldLines(Graphics2D)
     * @see         #drawStone(Graphics2D)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        drawFieldLines(graphics);
        if (playerOfStone != null) {
            drawStone(graphics);
        }
    }

    /**
     * Zeichnet die Feldlinien eines Feldes.
     *
     * @param graphics  Entspricht der visuellen Darstellungskomponente.
     */
    private void drawFieldLines(Graphics2D graphics) {
        int width = getWidth();
        int height = getHeight();

        graphics.drawLine(0, 0, width, 0);
        graphics.drawLine(0, 0, 0, height);
        graphics.drawLine(width, 0, width, height);
        graphics.drawLine(0, height, width, height);
    }

    /**
     * Zeichnet den Stein des Spielers, falls das Feld belegt ist.
     *
     * @param graphics  Entspricht der visuellen Darstellungskomponente.
     */
    private void drawStone(Graphics2D graphics) {
        assert playerOfStone != null : "Stone of Player cannot be null!";

        if (playerOfStone == Player.HUMAN) {
            graphics.setColor(ReversiGui.HUMAN_COLOR);
        } else {
            graphics.setColor(ReversiGui.MACHINE_COLOR);
        }
        graphics.fillOval(borderOfStone, borderOfStone,
                getWidth() - borderOfStone * 2,
                getHeight() - borderOfStone * 2);
    }

    /**
     * Gibt eine Meldung aus.
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
