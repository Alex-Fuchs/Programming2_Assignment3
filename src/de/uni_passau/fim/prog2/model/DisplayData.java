package de.uni_passau.fim.prog2.model;

import de.uni_passau.fim.prog2.Observer.Observable;

import java.util.Stack;

/**
 * Entspricht dem Vermittler zwischen {@code Board} und Controller. Die Klasse
 * implementiert das Interface {@code Observable}, damit die View bei
 * Änderungen benachrichtigt wird. Zusätzlich wurde eine Undo Funktion
 * hinzugefügt, die den letzten Spielzug des Menschen rückgängig macht, falls
 * dieser bereits gezogen ist.
 *
 * @version 15.01.20
 * @author -----
 */
public final class DisplayData extends Observable {

    /**
     * Entspricht allen Spielzügen seit Spielstart.
     */
    private Stack<Board> boards;

    /**
     * Entspricht dem {@code Thread}, der den momentanen Maschinenzug berechnet,
     * wobei dieser während einem Zug des Menschen {@code null} ist.
     */
    private Thread machineThread;

    /**
     * Kreiert den Vermittler für die Gui mit den standard Spieleinstellungen
     * für das erste Spiel.
     *
     * @see     #createNewStack(Player)
     */
    public DisplayData() {
        boards = createNewStack(Player.HUMAN);
    }

    /**
     * Löscht das momentane Spiel, startet ein neues Spiel mit den gleichen
     * Spieleinstellungen und updatet die View, wobei auch momentane
     * Maschinenzüge abgebrochen werden.
     */
    public void createNewBoard() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        stopMachineThread();
        setChanged();
        boards = createNewStack(boards.peek().getFirstPlayer());
        notifyObserver(false);
        machineMove();
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
     * @see                                 Board#move(int, int)
     */
    public boolean move(int row, int col) {
        assert boards.peek() != null : "Illegal state of DisplayData";

        if (!isGameOver() && next() == Player.HUMAN) {
            Board move = boards.peek().move(row, col);
            if (move != null) {
                setChanged();
                boards.push(move);
                notifyObserver(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Führt die Maschinenzüge aus, bis das Spiel vorbei ist oder nun der
     * Mensch an der Reihe ist, wobei dies von {@code MaschineThread}
     * berechnet wird.
     *
     * @throws IllegalStateException    Wird geworfen, falls versucht wird,
     *                                  Maschinenzüge auszuführen, obwohl diese
     *                                  bereits momentan berechnet werden.
     * @see                             MachineThread
     */
    public void machineMove() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        if (machineThread == null) {
            machineThread = new MachineThread();
            machineThread.start();
        } else {
            throw new IllegalStateException("The machine is already moving!");
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
     * Setzt das Level, falls {@code level} positiv ist, wobei zu beachten ist,
     * dass das Level sich nach {@link #undo()} nicht zurücksetzt und alle
     * zukünftigen Spiele in dem selben Level eingestellt sind.
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
     * Tauscht den Eröffner, startet ein neues Spiel und benachrichtigt die
     * View, wobei dadurch auch momentane Maschinenzüge abgebrochen werden.
     *
     * @see         #stopMachineThread()
     * @see         #createNewStack(Player)
     */
    public void switchPlayerOrder() {
        assert boards.peek() != null : "Illegal state of DisplayData";

        stopMachineThread();
        setChanged();
        if (boards.peek().getFirstPlayer() == Player.HUMAN) {
            boards = createNewStack(Player.MACHINE);
            notifyObserver(false);
            machineMove();
        } else {
            boards = createNewStack(Player.HUMAN);
            notifyObserver(false);
        }
    }

    /**
     * Setzt einen Spielzug des Menschen zurück, falls dieser bereits gezogen
     * hat, wobei dadurch ggf der momentane Maschinenzug abgebrochen wird.
     *
     * @see         #stopMachineThread()
     * @see         #undoIsPossible()
     */
    public void undo() {
        assert boards.peek() != null : "Illegal state of DisplayData";
        assert undoIsPossible() : "Undo should only be then called!";

        boolean humanMovePopped = false;
        stopMachineThread();
        while (!humanMovePopped && undoIsPossible()) {
            setChanged();
            boards.pop();
            notifyObserver(false);
            if (next() == Player.HUMAN) {
                humanMovePopped = true;
            }
        }
    }

    /**
     * Gibt zurück, ob der letzte Zug des Menschen rückgängig machbar ist.
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
     * Gibt den Spieler zurück, der durch seinen Zug die momentane
     * Spielsituation kreiert hat, wobei anfangs dies {@code null} ist.
     *
     * @return      Entspricht dem Spieler, der zuletzt an der Reihe war,
     *              wobei dies bei Spielbeginn {@code null} ist.
     * @see         Board#next()
     */
    public Player lastPlayer() {
        if (boards.size() > 1) {
            Board top = boards.pop();
            Player lastPlayer = boards.peek().next();
            boards.push(top);
            return lastPlayer;
        } else {
            return null;
        }
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
     * Falls momentan ein Maschinenzug berechnet wird, wird diese Berechnung
     * abgebrochen und zum Ausgangszustand zurückgeführt.
     */
    @SuppressWarnings("deprecation")
    public void stopMachineThread() {
        if (machineThread != null) {
            machineThread.stop();
            machineThread = null;
            clearChanged();
        }
    }

    /**
     * Dieser {@code Thread} berechnet die Züge der Maschine.
     */
    private class MachineThread extends Thread {

        /**
         * Führt Maschinenzüge solange aus, bis das Spiel vorbei ist oder die
         * Maschine nicht mehr an der Reihe ist.
         *
         * @see         #isGameOver()
         * @see         #next()
         * @see         Board#machineMove()
         */
        @Override
        public void run() {
            while (!isGameOver() && next() == Player.MACHINE) {
                setChanged();
                boards.push(boards.peek().machineMove());
                notifyObserver(true);
            }
            machineThread = null;
        }
    }
}
