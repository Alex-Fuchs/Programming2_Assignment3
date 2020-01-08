package de.uni_passau.fim.prog2.model;

/**
 * {@code IllegalMoveException} ist eine definierte {@code RuntimeException},
 * die geworfen wird, falls versucht wird, einen Zug auszuführen, obwohl das
 * Spiel vorbei ist oder ein Spieler nicht an der Reihe ist.
 *
 * @version 21.12.19
 * @author -----
 */
class IllegalMoveException extends RuntimeException {

    /**
     * Vewendet lediglich den Super Konstruktor zur Instanziierung der
     * {@code IllegalMoveException}.
     */
    IllegalMoveException() {
        super();
    }

    /**
     * Verwendet lediglich den Super Konstruktor mit einer Nachricht, um
     * die {@code IllegalMoveException} zu instanziieren.
     *
     * @param message       Entspricht der Fehlernachricht.
     */
    IllegalMoveException(String message) {
        super(message);
    }
}
