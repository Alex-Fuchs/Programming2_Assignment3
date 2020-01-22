package de.uni_passau.fim.prog2.model;

import java.awt.*;

/**
 * {@code Reversi} kann als Mensch vs Maschine gespielt werden,
 * wobei diese Klasse die Spieler und deren Steine darstellt.
 *
 * @version 21.12.19
 * @author -----
 */
public enum Player {

    /**
     * Entspricht dem menschlichen Spieler.
     */
    HUMAN {
        /**
         * {@inheritDoc}
         */
        @Override
        Player inverse() {
            return MACHINE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Color getColorOfPlayer() {
            return Color.BLUE;
        }
    },
    /**
     * Entspricht der Maschine.
     */
    MACHINE {
        /**
         * {@inheritDoc}
         */
        @Override
        Player inverse() {
            return HUMAN;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Color getColorOfPlayer() {
            return Color.RED;
        }
    };

    /**
     * Gibt den Gegenspieler für einen Spieler zurück.
     *
     * @return      Entspricht dem gegnerischen Spieler.
     */
    abstract Player inverse();

    /**
     * Gibt die Farbe für einen Spieler zurück, wobei diese Methode für die
     * visuelle Darstellung benötigt wird.
     *
     * @return      Entspricht der Farbe des Spielers.
     */
    public abstract Color getColorOfPlayer();
}
