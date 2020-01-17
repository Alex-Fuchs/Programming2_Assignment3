package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.Observer.Observable;
import de.uni_passau.fim.prog2.Observer.Observer;
import de.uni_passau.fim.prog2.model.Board;
import de.uni_passau.fim.prog2.model.DisplayData;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

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
     * Kreiert die visuelle Darstellung eines Feldes des Spielbretts, durch
     * die der Mensch per Mausklick ziehen kann.
     *
     * @param row               Entspricht der Zeile des Feldes.
     * @param col               Entspricht der Zeile des Feldes.
     * @param mouseAdapter      Ermöglicht das Zeihen durch Mausklick.
     */
    Field(int row, int col, MouseAdapter mouseAdapter) {
        assert row > 0 && row <= Board.SIZE : "Row is illegal!";
        assert col > 0 && col <= Board.SIZE : "Col is illegal!";
        assert mouseAdapter != null : "MouseAdapter cannot be null!";

        this.row = row;
        this.col = col;
        addMouseListener(mouseAdapter);
        setBackground(fieldColor);
        DisplayData displayData = DisplayData.getInstance();
        playerOfStone = displayData.getSlot(row, col);
        displayData.addObserver(this);
    }

    /**
     * Updatet die visuelle Darstellung des Feldes und somit des Steines, falls
     * das Feld nicht leer ist.
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

    /**
     * Gibt die Zeile des Feldes zurück.
     *
     * @return      Entspricht der Zeile zwischen {@code 1} und
     *              {@code Board.SIZE}.
     */
    int getRow() {
        return row;
    }

    /**
     * Gibt die Spalte des Feldes zurück.
     *
     * @return      Entspricht der Spalte zwischen {@code 1} und
     *              {@code Board.SIZE}.
     */
    int getCol() {
        return col;
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
}
