package de.uni_passau.fim.prog2.model;

import de.uni_passau.fim.prog2.Observer.Observable;

import java.util.Stack;

/**
 * Entspricht dem Vermittler zwischen {@code Board} und der View mit dem
 * Controller. Die Klasse implementiert das Interface {@code Observable}
 * und benutzt das Design-Pattern Singleton, damit sowohl {@code ReversiGui}
 * und {@code Field} sich als {@code Observer} registrieren können, und der
 * gesamte Controller darauf zugreifen kann, ohne ständig das Objekt in
 * Methoden bzw Konstruktoren übergeben zu müssen.
 *
 * @version 15.01.20
 * @author -----
 */
public final class DisplayData extends Observable {

    private static DisplayData ourInstance = new DisplayData();

    /**
     * Entspricht allen Spielzügen seit Spielstart.
     */
    private Stack<Board> boards;

    /**
     * Kreiert den Vermittler für die Gui mit den standard Spieleinstellungen
     * für das erste Spiel.
     *
     * @see     #createNewStack(Player)
     */
    private DisplayData() {
        assert ourInstance == null : "DisplayData already exists!";

        boards = createNewStack(Player.HUMAN);
    }

    /**
     * Gibt die statische Instanz des Singleton Design-Pattern zurück, die
     * auch die einzige Spiellogik darstellt.
     *
     * @return      Entspricht dem einzigen Objekt von {@code DisplayData}.
     */
    public static DisplayData getInstance() {
        return ourInstance;
    }

    /**
     * Löscht das momentane Spiel und startet ein neues Spiel mit den gleichen
     * Spieleinstellungen und updatet die View.
     *
     * @see     #updateObserver()
     */
    public void createNewBoard() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        boards = createNewStack(boards.peek().getFirstPlayer());
        machineMove();
        updateObserver();
    }

    /**
     * Führt einen Zug des menschlichen Spielers aus, falls dieser legal ist,
     * das Spiel nicht vorbei ist und der Mensch an der Reihe ist. Falls
     * der Zug erfolgreich war, wird die View benachrichtigt.
     *
     * @param row                           Entspricht der Zeile in der der
     *                                      Stein gelegt werden soll.
     * @param col                           Entspricht der Spalte in der der
     *                                      Stein gelegt werden soll.
     * @return                              Gibt zurück, ob der Zug erfolgreich
     *                                      war.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code row}
     *                                      oder {@code col} negativ, {@code 0}
     *                                      oder größer als {@code Board.SIZE}
     *                                      ist.
     * @see                                 #isGameOver()
     * @see                                 #next()
     * @see                                 #updateObserver()
     * @see                                 Board#move(int, int)
     */
    public boolean move(int row, int col) {
        assert boards.peek() != null : "Illegal state of DisplayData";

        if (!isGameOver() && next() == Player.HUMAN) {
            Board move = boards.peek().move(row, col);
            if (move != null) {
                boards.push(move);
                updateObserver();
                return true;
            }
        }
        return false;
    }

    /**
     * Führt einen Zug der Maschine aus, falls diese an der Reihe ist und das
     * Spiel nicht vorbei ist und benachrichtigt die View.
     *
     * @see     #isGameOver()
     * @see     #next()
     * @see     #updateObserver()
     * @see     Board#machineMove()
     */
    public void machineMove() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        if (!isGameOver()) {
            if (next() == Player.MACHINE) {
                boards.push(boards.peek().machineMove());
                updateObserver();
            }
        }
    }

    /**
     * Gibt den Stein aus, wobei {@code null} für keinen Stein steht.
     *
     * @param row                           Entspricht der Zeile des Feldes.
     * @param col                           Entspricht der Spalte des Feldes.
     * @return                              Gibt den Spieler des Steines aus
     *                                      und bei keinem Stein wird
     *                                      {@code null} zurückgegeben.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code row}
     *                                      oder {@code col} negativ, {@code 0}
     *                                      oder größer als {@code Board.SIZE}
     *                                      ist.
     * @see                                 Board#getSlot(int, int)
     */
    public Player getSlot(int row, int col) {
        assert boards.peek() != null : "Illegal state of DisplayData";

        return boards.peek().getSlot(row, col);
    }

    /**
     * Setzt das Level, falls dieses positiv ist.
     *
     * @param level                         Entspricht dem neuen Level.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code level}
     *                                      negativ oder 0 ist.
     * @see                                 Board#setLevel(int)
     */
    public void setLevel(int level) {
        assert boards.peek() != null : "Illegal state of DisplayData";

        boards.peek().setLevel(level);
    }

    /**
     * Tauscht den Eröffner und startet ein neues Spiel und benachrichtigt die
     * View.
     */
    public void switchPlayerOrder() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        if (boards.peek().getFirstPlayer() == Player.HUMAN) {
            boards = createNewStack(Player.MACHINE);
            machineMove();
        } else {
            boards = createNewStack(Player.HUMAN);
        }
        updateObserver();
    }

    /**
     * Setzt einen Spielzug des Menschen zurück, falls dieser bereits gezogen
     * hat.
     *
     * @see         #undoIsPossible()
     */
    public void undo() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        boolean humanMovePopped = false;
        while (!humanMovePopped && undoIsPossible()) {
            boards.pop();
            updateObserver();
            if (next() == Player.HUMAN) {
                humanMovePopped = true;
            }
        }
    }

    /**
     * Gibt zurück, ob ein Undo möglich ist.
     *
     * @return      Gibt {@code true} zurück, falls ein Undo möglich ist,
     *              andernfalls {@code false}.
     */
    public boolean undoIsPossible() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        final int minimumMovesForUndo;
        if (boards.peek().getFirstPlayer() == Player.HUMAN) {
            minimumMovesForUndo = 2;
            return boards.size() >= minimumMovesForUndo;
        } else {
            minimumMovesForUndo = 3;
            return boards.size() >= minimumMovesForUndo;
        }
    }

    /**
     * Gibt die Anzahl an Steinen des Menschen zurück.
     *
     * @return      Entspricht der Anzahl der Steine.
     * @see         Board#getNumberOfHumanTiles()
     */
    public int getNumberOfHumanTiles() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        return boards.peek().getNumberOfHumanTiles();
    }

    /**
     * Gibt die Anzahl der Steinen der Maschine zurück.
     *
     * @return      Entspricht der Anzahl der Steine.
     * @see         Board#getNumberOfMachineTiles()
     */
    public int getNumberOfMachineTiles() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        return boards.peek().getNumberOfMachineTiles();
    }

    /**
     * Gibt zurück, ob das Spiel vorbei ist.
     *
     * @return      Gibt {@code true} zurück, falls das Spiel vorbei ist,
     *              andernfalls {@code false}.
     * @see         Board#gameOver()
     */
    public boolean isGameOver() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        return boards.peek().gameOver();
    }

    /**
     * Gibt den Gewinner zurück, falls das Spiel vorbei ist.
     *
     * @return                              Entspricht dem Gewinner.
     * @throws IllegalStateException()      Wird geworfen, falls das Spiel
     *                                      noch nicht vorbei ist.
     * @see                                 Board#getWinner()
     */
    public Player getWinner() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        return boards.peek().getWinner();
    }

    /**
     * Gibt den Spieler aus, der an der Reihe ist.
     *
     * @return      Entspricht dem Spieler, der an der Reihe ist.
     * @see         Board#next()
     */
    public Player next() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        return boards.peek().next();
    }

    /**
     * Kreiert einen neuen {@code Stack} mit einem neuen Spiel, wobei der
     * Eröffner gesetzt werden kann.
     *
     * @param firstPlayer       Entspricht dem Eröffner.
     * @return                  Gibt den neuen {@code Stack} zurück.
     */
    private Stack<Board> createNewStack(Player firstPlayer) {
        assert firstPlayer != null : "First player cannot be undefined!";

        Stack<Board> stack = new Stack<>();
        stack.add(new Reversi(firstPlayer));
        return stack;
    }

    /**
     * Setzt den Zustand auf verändert und updatet alle {@code Observer}.
     */
    private void updateObserver() {
        setChanged();
        notifyObserver();
    }
}
