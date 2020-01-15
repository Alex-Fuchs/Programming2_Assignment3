package de.uni_passau.fim.prog2.model;

import de.uni_passau.fim.prog2.Observer.Observable;
import java.util.Stack;

public class DisplayData extends Observable {

    private static Stack<Board> boards;

    public DisplayData() {
        boards = createNewStack(Player.HUMAN);
    }

    public void createNewBoard() {
        assert boards.peek() != null : "Illegal State";

        Board top = boards.peek();
        boards = createNewStack(top.getFirstPlayer());
        machineMove();
        setChanged();
    }

    public boolean move(int row, int col) {
        assert boards.peek() != null : "Illegal State";

        if (row > 0 && row <= Board.SIZE && col > 0 && col <= Board.SIZE) {
            if (!gameOver() || next() == Player.HUMAN) {
                Board move = boards.peek().move(row, col);
                if (move != null) {
                    boards.push(move);
                    setChanged();
                    return true;
                }
            }
            return false;
        } else {
            throw new IllegalArgumentException("Row or col illegal!");
        }
    }

    public void machineMove() {
        assert boards.peek() != null : "Illegal State";

        Board top = boards.peek();
        if (!top.gameOver()) {
            if (top.next() == Player.MACHINE) {
                Board move = top.machineMove();
                boards.push(move);
                setChanged();
            }
        }
    }

    public Player getSlot(int row, int col) {
        assert boards.peek() != null : "Illegal State";

        if (row > 0 && row <= Board.SIZE && col > 0 && col <= Board.SIZE) {
            Board top = boards.peek();
            return top.getSlot(row, col);
        } else {
            throw new IllegalArgumentException("Row or col illegal!");
        }
    }

    public void setLevel(int level) {
        assert boards.peek() != null : "Illegal State";

        if (level > 0) {
            Board top = boards.peek();
            top.setLevel(level);
        } else {
            throw new IllegalArgumentException("Level is negative or 0!");
        }
    }

    public void switchPlayerOrder() {
        assert boards.peek() != null : "Illegal State";

        Board top = boards.peek();
        if (top.getFirstPlayer() == Player.HUMAN) {
            boards = createNewStack(Player.MACHINE);
            machineMove();
        } else {
            boards = createNewStack(Player.HUMAN);
        }
        setChanged();
    }

    public void undo() {
        assert boards.peek() != null : "Illegal State";

        boolean humanMovePopped = false;
        while (!humanMovePopped && undoIsPossible()) {
            boards.pop();
            setChanged();
            if (next() == Player.HUMAN) {
                humanMovePopped = true;
            }
        }
    }

    public boolean undoIsPossible() {
        if (boards.peek().getFirstPlayer() == Player.HUMAN) {
            return boards.size() > 1;
        } else {
            return boards.size() > 2;
        }
    }

    public int getNumberOfHumanTiles() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().getNumberOfHumanTiles();
    }

    public int getNumberOfMachineTiles() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().getNumberOfMachineTiles();
    }

    public boolean gameOver() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().gameOver();
    }

    public Player getWinner() {
        assert boards.peek() != null: "Illegal State";

        if (gameOver()) {
            return boards.peek().getWinner();
        } else {
            throw new IllegalStateException("Game is not over!");
        }
    }

    public Player next() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().next();
    }

    private Stack<Board> createNewStack(Player firstPlayer) {
        assert firstPlayer != null : "First player cannot be undefined!";

        Stack<Board> stack = new Stack<>();
        stack.add(new Reversi(firstPlayer));
        return stack;
    }
}
