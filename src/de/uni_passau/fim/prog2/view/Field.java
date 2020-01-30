package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.Board;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.MouseAdapter;

/**
 * Implementiert die visuelle Darstellung eines Feldes, auf das ein Stein
 * gesetzt werden kann.
 *
 * @version 25.01.20
 * @author -----
 */
class Field extends JPanel {

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
     * Entspricht der Farbe des Spielsteines, wobei diese auch {@code null}
     * sein kann, falls kein Stein auf dem Feld liegt.
     */
    private Color colorOfStone;

    /**
     * Kreiert die visuelle Darstellung eines Feldes des Spielbretts, durch
     * die der Mensch per Mausklick ziehen kann.
     *
     * @param row               Entspricht der Zeile des Feldes.
     * @param col               Entspricht der Zeile des Feldes.
     * @param mouseAdapter      Ermöglicht das Ziehen durch Mausklick.
     */
    Field(int row, int col, MouseAdapter mouseAdapter) {
        assert row > 0 && row <= Board.SIZE : "Row is illegal!";
        assert col > 0 && col <= Board.SIZE : "Col is illegal!";
        assert mouseAdapter != null : "MouseAdapter cannot be null!";

        final Color fieldColor = new Color(0, 180, 0);
        this.row = row;
        this.col = col;
        setBackground(fieldColor);
        addMouseListener(mouseAdapter);
    }

    /**
     * Falls {@code colorOfStone} {@code null} entspricht, liegt nun kein Stein
     * mehr auf dem Feld, andernfalls wird die Farbe des Steines dem Spieler
     * entsprechend gesetzt. Falls sich etwas geändert hat, wird das Feld
     * inkl Stein neu gezeichnet.
     *
     * @param colorOfStone      Entspricht der Farbe des Steines, falls dieser
     *                          vorhanden ist, ansonsten {@code null}.
     * @see                     #repaint()
     */
    void setColorOfStone(Color colorOfStone) {
        if (this.colorOfStone != colorOfStone) {
            this.colorOfStone = colorOfStone;
            repaint();
        }
    }

    /**
     * Gibt die Zeile des Feldes zurück.
     *
     * @return      Entspricht einer Zeile zwischen {@code 1} und
     *              {@code Board.SIZE}.
     */
    int getRow() {
        return row;
    }

    /**
     * Gibt die Spalte des Feldes zurück.
     *
     * @return      Entspricht einer Spalte zwischen {@code 1} und
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
        if (colorOfStone != null) {
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
        assert colorOfStone != null : "Color of Stone cannot be null!";

        final int distanceToBorder = 7;
        graphics.setColor(colorOfStone);
        graphics.fillOval(distanceToBorder, distanceToBorder,
                getWidth() - distanceToBorder * 2,
                getHeight() - distanceToBorder * 2);
    }
}
