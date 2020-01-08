package de.uni_passau.fim.prog2.model;

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
    };

    /**
     * Gibt den Gegenspieler für einen Spieler zurück.
     *
     * @return      Entspricht dem gegnerischen Spieler.
     */
    abstract Player inverse();
}
