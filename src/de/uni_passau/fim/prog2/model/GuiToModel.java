package de.uni_passau.fim.prog2.model;

import java.util.Stack;

public class GuiToModel {

    private static Stack<Board> boards = createNewStack(Player.HUMAN);

    private GuiToModel() { }

    public static void createNewBoard() {
        assert boards.peek() != null : "Illegal State";

        Board top = boards.peek();
        boards = createNewStack(top.getFirstPlayer());
        machineMove();
    }

    public static boolean move(int row, int col) {
        assert boards.peek() != null : "Illegal State";

        if (row > 0 && row <= Board.SIZE && col > 0 && col <= Board.SIZE) {
            Board top = boards.peek();
            if (!top.gameOver() || top.next() == Player.HUMAN) {
                Board move = top.move(row, col);
                if (move != null) {
                    boards.push(move);
                    return true;
                }
            }
            return false;
        } else {
            throw new IllegalArgumentException("Row or col illegal!");
        }
    }

    public static void machineMove() {
        assert boards.peek() != null : "Illegal State";

        Board top = boards.peek();
        if (!top.gameOver()) {
            if (top.next() == Player.MACHINE) {
                Board move = top.machineMove();
                boards.push(move);
            }
        }
    }

    public static Player[][] getVisualisation() {
        assert boards.peek() != null : "Illegal State";

        Player[][] boardVisualisation = new Player[Board.SIZE][Board.SIZE];
        Board top = boards.peek();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int u = 0; u < Board.SIZE; u++) {
                boardVisualisation[i][u] = top.getSlot(i + 1, u + 1);
            }
        }
        return boardVisualisation;
    }

    public static void setLevel(int level) {
        assert boards.peek() != null : "Illegal State";

        if (level > 0) {
            Board top = boards.peek();
            top.setLevel(level);
        } else {
            throw new IllegalArgumentException("Level is negative or 0!");
        }
    }

    public static void switchPlayerOrder() {
        assert boards.peek() != null : "Illegal State";

        Board top = boards.peek();
        if (top.getFirstPlayer() == Player.HUMAN) {
            boards = createNewStack(Player.MACHINE);
            machineMove();
        } else {
            boards = createNewStack(Player.HUMAN);
        }
    }

    public static void undo() {
        assert boards.peek() != null : "Illegal State";

        boolean humanMovePopped = false;
        while (!humanMovePopped && undoIsPossible()) {
            boards.pop();
            if (next() == Player.HUMAN) {
                humanMovePopped = true;
            }
        }
    }

    public static boolean undoIsPossible() {
        if (boards.peek().getFirstPlayer() == Player.HUMAN) {
            return boards.size() > 1;
        } else {
            return boards.size() > 2;
        }
    }

    public static int getNumberOfHumanTiles() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().getNumberOfHumanTiles();
    }

    public static int getNumberOfMachineTiles() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().getNumberOfMachineTiles();
    }

    public static boolean gameOver() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().gameOver();
    }

    public static Player getWinner() {
        assert boards.peek() != null: "Illegal State";

        if (gameOver()) {
            return boards.peek().getWinner();
        } else {
            throw new IllegalStateException("Game is not over!");
        }
    }

    public static Player next() {
        assert boards.peek() != null : "Illegal State";

        return boards.peek().next();
    }

    private static Stack<Board> createNewStack(Player firstPlayer) {
        assert firstPlayer != null : "First player cannot be undefined!";

        Stack<Board> stack = new Stack<>();
        stack.add(new Reversi(firstPlayer));
        return stack;
    }
}
