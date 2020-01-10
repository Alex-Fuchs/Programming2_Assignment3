package de.uni_passau.fim.prog2.io;

import de.uni_passau.fim.prog2.model.Board;
import de.uni_passau.fim.prog2.model.Player;
import de.uni_passau.fim.prog2.model.Reversi;

/**
 * {@code ShellToBoard} ist eine Utilityklasse, die den Output des Programms
 * steuert und ebenso das Interface {@code Board} und die Klasse {@code Shell}
 * verbindet. Parameter werden auf Richtigkeit geprüft. Befehle werden an das
 * {@code Board} Objekt weitergeleitet.
 *
 * @version 21.12.19
 * @author -----
 */
final class ShellToBoard {

    /**
     * Stellt das Objekt des Spiels dar und setzt das Level auf 3 und den
     * Eröffner auf den Menschen.
     */
    private static Board board = new Reversi(Player.HUMAN);

    private ShellToBoard() { }

    /**
     * Setzt das {@code Board} Objekt vollständig zurück, wobei die
     * Spieleinstellungen des alten Spiels erhalten bleiben.
     *
     * @see     Board#getFirstPlayer()
     * @see     Reversi
     */
    static void createNewBoard() {
        board = new Reversi(board.getFirstPlayer());
    }

    /**
     * Falls der Mensch ziehen kann, wird geprüft, ob der Zug legal ist und
     * bei Legalität wird der Zug ausgeführt. Gibt ebenfalls den Gewinner aus,
     * falls das Spiel vorbei ist. Gibt auch aus, ob die Maschine aussetzen
     * muss.
     *
     * @param tokens    Entspricht der Zeile und der Spalte, auf den ein Stein
     *                  gelegt werden soll.
     * @see             #checkParameters(String[])
     * @see             #printWinner()
     * @see             #checkMissTurn(Player)
     * @see             Board#gameOver()
     * @see             Board#next()
     * @see             Board#move(int, int)
     */
    static void move(String[] tokens) {
        if (!board.gameOver()) {
            if (board.next() == Player.HUMAN) {
                Integer[] parameters = checkParameters(tokens);
                if (parameters != null) {
                    if (parameters[0] <= Board.SIZE
                            && parameters[1] <= Board.SIZE) {
                        Board move = board.move(parameters[0], parameters[1]);

                        if (move != null) {
                            board = move;
                            if (board.gameOver()) {
                                printWinner();
                            } else {
                                checkMissTurn(Player.HUMAN);
                            }
                        } else {
                            printError("Invalid move at (" + parameters[0]
                                    + ", " + parameters[1] + ")!");
                        }
                    } else {
                        printError("At least one parameter is too big!");
                    }
                }
            } else {
                printError("Machine turn!");
            }
        } else {
            printError("The game is already over!");
        }
    }

    /**
     * Die Maschine zieht, falls sie ziehen kann und sie tut dies solange, wie
     * sie am Stück ziehen kann. Gibt ebenfalls den Gewinner aus, falls das
     * Spiel vorbei ist. Gibt auch aus, ob die Maschine aussetzen muss.
     *
     * @see             #printWinner()
     * @see             #checkMissTurn(Player)
     * @see             Board#gameOver()
     * @see             Board#next()
     * @see             Board#machineMove()
     */
    static void machineMove() {
        if (!board.gameOver()) {
            if (board.next() == Player.MACHINE) {
                board = board.machineMove();
                if (board.gameOver()) {
                    printWinner();
                } else {
                    checkMissTurn(Player.MACHINE);
                    machineMove();
                }
            }
        }
    }

    /**
     * Ändert das Level des momentanen Spiels. Das Level muss ein positiver
     * {@code Integer} sein.
     *
     * @param tokens     Entspricht der Liste der Parameter, hier ist jedoch
     *                   nur ein Parameter, das Level, notwendig.
     * @see              #checkParameters(String[])
     * @see              Board#setLevel(int)
     */
    static void setLevel(String[] tokens) {
        Integer[] parameter = checkParameters(tokens);
        if (parameter != null) {
            board.setLevel(parameter[0]);
        }
    }

    /**
     * Erstellt ein neues Spiel und tauscht den Eröffner, wobei die sonstigen
     * Spieleinstellungen, das Level, erhalten bleiben.
     *
     * @see     Board#getFirstPlayer()
     * @see     Reversi
     */
    static void switchPlayerOrder() {
        if (board.getFirstPlayer() == Player.HUMAN) {
            board = new Reversi(Player.MACHINE);
        } else {
            board = new Reversi(Player.HUMAN);
        }
    }

    /**
     * Gibt die kanonische Darstellung von {@code board} zurück, falls das
     * Spiel noch nicht vorbei ist.
     *
     * @see     Board#gameOver()
     * @see     Board#toString()
     */
    static void print() {
        System.out.println(board);
    }

    /**
     * Gibt alle möglichen Kommandos mit Beschreibung in der Konsole aus.
     */
    static void help() {
        String[] commands = {"**********",
                "Othello:",
                "h: prints all commands",
                "q: game quit",
                "n: creates a new game",
                "m <integer x> <integer y>: sets stone to row x, col y",
                "l <integer x>: sets the level to x",
                "s: switches the player order",
                "p: prints the current board",
                "The board is always square with the size: " + Board.SIZE,
                "The row and col is indexed with 1,...," + Board.SIZE,
                "the Level can be set to 1,...,8",
                "**********"};

        for (String tmp: commands) {
            System.out.println(tmp);
        }
    }

    /**
     * Gibt eine spezielle Fehlernachricht in der Konsole aus.
     *
     * @param message       Entspricht einer kurzen Beschreibung des Fehlers.
     */
    static void printError(String message) {
        final String errorMessage = "Error! ";
        System.out.println(errorMessage + message);
    }

    /**
     * Prüft, ob alle nötigen Parameter zu {@code Integer} konvertiert werden
     * können und positiv sind.
     *
     * @param tokens        Entspricht den übergebenen Parametern.
     * @return              Gibt null zurück, falls einer der Parameter nicht
     *                      den Anforderungen entspricht.
     *                      Gibt andernfalls die konvertierten Parameter
     *                      zurück.
     */
    private static Integer[] checkParameters(String[] tokens) {
        Integer[] parameters = new Integer[tokens.length];
        for (int i = 0; i < parameters.length; i++) {
            try {
                parameters[i] = Integer.parseInt(tokens[i]);
                if (parameters[i] <= 0) {
                    printError("At least one parameter is not positive!");
                    return null;
                }
            } catch (NumberFormatException e) {
                printError("At least one parameter is no integer or too big!");
                return null;
            }
        }
        return parameters;
    }

    /**
     * Prüft, ob das Spiel vorbei ist und falls wird der Gewinner ausgegeben.
     *
     * @see         Board#getWinner()
     */
    private static void printWinner() {
        assert board.gameOver() : "The game is not over!";

        Player winner = board.getWinner();
        if (winner == Player.HUMAN) {
            System.out.println("You have won!");
        } else if (winner == Player.MACHINE) {
            System.out.println("Machine has won.");
        } else {
            System.out.println("Tie game!");
        }
    }

    /**
     * Prüft, ob ein Spieler aussetzen muss.
     *
     * @param playerMoved       Entspricht dem Spieler, der den letzten Zug
     *                          gemacht hat.
     * @see                     Board#next()
     */
    private static void checkMissTurn(Player playerMoved) {
        if (board.next() == playerMoved) {
            if (playerMoved == Player.HUMAN) {
                System.out.println("The bot has to miss a turn");
            } else {
                System.out.println("Human has to miss a turn");
            }
        }
    }
}
