package de.uni_passau.fim.prog2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Tree} berechnet den besten Zug für einen Spieler, wobei für einzelne
 * Spielfelder die Scorewerte von {@code Score} benutzt werden. Zur
 * Berechnung wird ein Baum an möglichen Zügen einer max Tiefe generiert.
 * Diese Tiefe ist von dem Level abhängig. Aus diesem Baum wird dann der
 * beste Zug berechnet.
 *
 * @version 21.12.19
 * @author -----
 */
class Tree {

    /**
     * Entspricht der Wurzel des Baumes.
     */
    private Reversi root;

    /**
     * Entspricht den Kinderbäumen der Wurzel.
     */
    private List<Tree> children;

    /**
     * Kreiert den gesamten Baum des Ausgangsspielfeldes, wobei durch den Baum
     * alle möglichen Züge simuliert werden. Das Spiel darf somit nicht vorbei
     * sein.
     *
     * @param root              Entspricht dem Ausgangsspielfeldes.
     * @param level             Entspricht der Schwierigkeitsstufe nach der der
     *                          beste Zug berechnet wird.
     * @see                     #buildTree(int)
     */
    Tree(Reversi root, int level) {
        assert root != null : "The beginning game cannot be undefined!";
        assert level > 0 : "Level cannot be negative or 0!";
        assert !root.gameOver() : "The game cannot be already over!";

        this.root = root;
        buildTree(level);
    }

    /**
     * Kreiert einen Teilbaum des gesamten Baums, wobei die Wurzel ein
     * möglicher Zug darstellt.
     *
     * @param root                  Entspricht einer möglichen Spielsituation.
     */
    private Tree(Reversi root) {
        assert root != null : "The game to assess cannot be undefined!";

        this.root = root;
    }

    /**
     * Baut den gesamten Baum der möglichen Züge von der Ausgangsspielsituation
     * auf, wobei von jeder Spielsituation die nächst Möglichen berechnet
     * werden. Die max Tiefe des Baumes ist dabei durch das Level gegeben.
     *
     * @param level         Entspricht der Schwierigkeitsstufe und somit
     *                      der max Tiefe des Baumes.
     * @see                 Reversi#moveForNextPlayer(int, int)
     */
    private void buildTree(int level) {
        children = new ArrayList<>();
        if (!root.gameOver() && level > 0) {
            for (int i = 1; i <= Board.SIZE; i++) {
                for (int u = 1; u <= Board.SIZE; u++) {
                    Reversi moveOfNextPlayer = root.moveForNextPlayer(i, u);

                    if (moveOfNextPlayer != null) {
                        Tree tree = new Tree(moveOfNextPlayer);
                        children.add(tree);
                        tree.buildTree(level - 1);
                    }
                }
            }
        }
    }

    /**
     * Berechnet auf Basis der nächst möglichen Züge den besten Zug für den
     * nächst ziehenden Spieler.
     *
     * @return          Entspricht dem besten Zug, ausgeführt auf einem Klon.
     * @see             #calculateScore(Player)
     */
    Reversi calculateBestMove() {
        assert !root.gameOver() : "The game must not be already over!";
        assert children.size() > 0 : "The game must have children because"
                + " the game is not over yet!";
        assert root.next() != null : "The next player cannot be undefined!";

        Tree firstChild = children.get(0);
        Reversi bestMove = firstChild.root;
        double bestScoreOfChildren = firstChild.calculateScore(root.next());
        for (int i = 1; i < children.size(); i++) {
            Tree child = children.get(i);
            double scoreOfChild = child.calculateScore(root.next());

            if (scoreOfChild > bestScoreOfChildren) {
                bestScoreOfChildren = scoreOfChild;
                bestMove = child.root;
            }
        }
        return bestMove;
    }


    /**
     * Gibt den Score eines Kindes zurück. Falls dieser Kinder besitzt, werden
     * diese ebenfalls in den Score involviert.
     *
     * @param playerToAssess    Entspricht dem zu bewertenden Spieler.
     * @return                  Gibt den Score zurück.
     * @see                     #calculateScoreOfChildren(Player)
     */
    private double calculateScore(Player playerToAssess) {
        assert playerToAssess != null : "The player to asses cannot"
                + " be undefined!";

        Score score = new Score(root, playerToAssess);
        double scoreOfRoot = score.calculateScore();
        if (children.size() > 0) {
            scoreOfRoot += calculateScoreOfChildren(playerToAssess);
        }
        return scoreOfRoot;
    }


    /**
     * Gibt den Score der Kinder zurück, wobei dieser bei einem gegnerischen
     * Zug dem Minimum und bei einem eigenen Zug dem Maximum entspricht.
     *
     * @param playerToAssess    Entspricht dem bewertenden Spieler.
     * @return                  Gibt den Score zurück.
     * @see                     #calculateScore(Player)
     */
    private double calculateScoreOfChildren(Player playerToAssess) {
        assert children.size() > 0 : "There must be at least 1 child!";
        assert playerToAssess != null : "The player to asses cannot"
                + " be undefined!";

        double scoreOfChildren = children.get(0).calculateScore(playerToAssess);
        for (int i = 1; i < children.size(); i++) {
            double scoreOfChild
                    = children.get(i).calculateScore(playerToAssess);

            if (root.next() == playerToAssess) {
                if (scoreOfChildren < scoreOfChild) {
                    scoreOfChildren = scoreOfChild;
                }
            } else {
                if (scoreOfChildren > scoreOfChild) {
                    scoreOfChildren = scoreOfChild;
                }
            }
        }
        return scoreOfChildren;
    }

}
