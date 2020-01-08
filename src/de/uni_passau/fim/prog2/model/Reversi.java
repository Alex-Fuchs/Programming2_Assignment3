package de.uni_passau.fim.prog2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code Reversi} implementiert ein strategisches Brettspiel
 * gennant Reversi bzw Othello. Das Spielbrett besteht aus
 * {@code Board.SIZE} x {@code Board.SIZE} Feldern. Auf diese Felder werden
 * Steine gelegt. Das Spiel sieht einen Menschen vs Maschine vor. Die Züge
 * des Bots werden aufwändig ermittelt. Das Spiel implementiert {@code Board},
 * mit dessen Funktionalität das Spiel gespielt werden kann.
 *
 * @version 21.12.19
 * @author -----
 */
public class Reversi implements Board {

    /**
     * Entspricht dem Spielfeld.
     */
    private Player[][] gameBoard;

    /**
     * Entspricht der momentanen Schwierigkeitsstufe der Maschine.
     */
    private static int level = 3;

    /**
     * Entspricht dem Spieler, der das Spiel eröffnet hat.
     */
    private Player firstPlayer;

    /**
     * Entspricht dem Spieler, der nun an der Reihe ist.
     */
    private Player nextPlayer;

    /**
     * Entspricht {@code true}, falls sowohl der Mensch als auch die Maschine
     * nicht mehr ziehen konnten, was äquivalent dazu ist, dass das Spiel
     * vorbei ist. Entspricht andernfalls {@code false}.
     */
    private boolean gameOver;

    /**
     * Entspricht der Anzahl an Steinen des menschlichen Spielers auf dem Feld.
     */
    private int numberOfHumanTiles = 2;

    /**
     * Entspricht der Anzahl an Steinen der Maschine auf dem Feld.
     */
    private int numberOfMachineTiles = 2;

    /**
     * Erstellt ein Spiel, wobei der Eröffner gesetzt werden kann und die
     * Schwierigkeitsstufe des alten Spiels erhalten bleibt bzw bei dem ersten
     * Spiel gleich {@code 3} ist.
     *
     * @param  firstPlayer                  Entspricht dem Eröffner des Spiels.
     * @throws IllegalArgumentException     Wird geworfen, falls der Eröffner
     *                                      {@code null} ist.
     * @see                                 #setInitialPosition()
     */
    public Reversi(Player firstPlayer) {
        if (firstPlayer != null) {
            gameBoard = new Player[Board.SIZE][Board.SIZE];
            this.firstPlayer = firstPlayer;
            nextPlayer = firstPlayer;
            setInitialPosition();
        } else {
            throw new IllegalArgumentException("FirstPlayer is undefined!");
        }
    }

    /**
     * Gibt den Spieler zurück, der das Spiel eröffnet hat.
     *
     * @return      Entspricht dem Eröffner.
     */
    @Override
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * Gibt den Spieler zurück, der nun an der Reihe ist, also nach den
     * Spielregeln an der Reihe ist und ziehen kann.
     *
     * @return      Entspricht dem nun ziehenden Spieler.
     */
    @Override
    public Player next() {
        return nextPlayer;
    }

    /**
     * Führt einen Zug des Menschen aus, falls der Mensch ziehen kann
     * und dieser an der Reihe ist. Der Zug wird auf einem Klon durchgeführt,
     * falls dieser legal ist, also den Spielregeln entspricht.
     *
     * @param row                           Entspricht der Zeile auf der der
     *                                      Stein gelegt werden soll.
     * @param col                           Entspricht der Spalte auf der der
     *                                      Stein gelegt werden soll.
     * @return                              Falls der Zug legal ist, wird der
     *                                      Zug auf einem Klon durchgeführt
     *                                      und dieser zurückgegeben,
     *                                      ansonsten wird {@code null}
     *                                      zurückgegeben.
     * @throws IllegalMoveException         Wird geworfen, falls das Spiel
     *                                      vorbei ist oder der Mensch nicht
     *                                      an der Reihe ist.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code row}
     *                                      oder {@code col} nicht positiv oder
     *                                      zu groß sind.
     * @see                                 #gameOver()
     * @see                                 #next()
     * @see                                 #moveForNextPlayer(int, int)
     * @see                                 IllegalMoveException
     */
    @Override
    public Reversi move(int row, int col) {
        if (row > 0 && row <= Board.SIZE && col > 0 && col <= Board.SIZE) {
            if (!gameOver()) {
                if (next() == Player.HUMAN) {
                    return moveForNextPlayer(row, col);
                } else {
                    throw new IllegalMoveException("Machine Turn!");
                }
            } else {
                throw new IllegalMoveException("Game is already over!");
            }
        } else {
            throw new IllegalArgumentException("Row or col is negative"
                    + " or too big!");
        }
    }

    /**
     * Berechnet den besten Zug für die Maschine und führt diesen auf einem
     * Klon aus, falls die Maschine an der Reihe ist und das Spiel nicht
     * vorbei ist.
     *
     * @return                          Entspricht dem Klon, auf dem der Zug
     *                                  ausgeführt wurde.
     * @throws IllegalMoveException     Wird geworfen, falls das Spiel bereits
     *                                  vorbei ist oder die Maschine nicht an
     *                                  der Reihe ist.
     * @see                             #gameOver()
     * @see                             #next()
     * @see                             IllegalMoveException
     * @see                             Tree
     */
    @Override
    public Reversi machineMove() {
        if (!gameOver()) {
            if (next() == Player.MACHINE) {
                Tree tree = new Tree(this, Reversi.level);
                return tree.calculateBestMove();
            } else {
                throw new IllegalMoveException("Human Turn!");
            }
        } else {
            throw new IllegalMoveException("Game is already over!");
        }
    }

    /**
     * Setzt die Schwierigkeitsstufe auf einen neuen Wert, der positiv sein
     * muss. Das Level kann beliebig schwierig gesetzt werden, wobei die
     * Rechenzeit sehr lang dauern kann. Ein maximales Level ist trotzdem
     * gegeben, da theoretisch nur eine begrenzte Anzahl an Zügen möglich sind.
     *
     * @param level                         Entspricht dem neuen Level der
     *                                      Maschine und muss positiv sein.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code level}
     *                                      nicht positiv ist.
     */
    @Override
    public void setLevel(int level) {
        if (level > 0) {
            Reversi.level = level;
        } else {
            throw new IllegalArgumentException("Level is negative or 0!");
        }
    }

    /**
     * Gibt zurück, ob das Spiel bereits vorbei ist, also keiner der Spieler
     * mehr ziehen kann.
     *
     * @return      Entspricht {@code true}, falls das Spiel vorbei ist,
     *              andernfalls {@code false}.
     */
    @Override
    public boolean gameOver() {
        return gameOver;
    }

    /**
     * Gibt zurück, wer der Gewinner des Spiels ist und somit mehr Steine auf
     * dem Spielfeld hat. Dies ist nur möglich, falls das Spiel bereits vorbei
     * ist. Ein Unentschieden ist ebenso möglich.
     *
     * @return                          Entspricht dem Gewinner des Spiels.
     *                                  Falls {@code null} zurückgegeben wird,
     *                                  ist das Spiel unentschieden.
     * @throws IllegalStateException    Wird geworfen, falls das Spiel noch
     *                                  nicht vorbei ist.
     * @see                             #gameOver()
     * @see                             #getNumberOfHumanTiles()
     * @see                             #getNumberOfMachineTiles()
     */
    @Override
    public Player getWinner() {
        if (gameOver()) {
            if (numberOfHumanTiles > numberOfMachineTiles) {
                return Player.HUMAN;
            } else if (numberOfHumanTiles < numberOfMachineTiles) {
                return Player.MACHINE;
            } else {
                return null;
            }
        } else {
            throw new IllegalStateException("Game is not over!");
        }
    }

    /**
     * Gibt die Anzahl der Steine des Menschen auf dem Spielfeld zurück.
     *
     * @return      Entspricht der Anzahl der Steine.
     */
    @Override
    public int getNumberOfHumanTiles() {
        return numberOfHumanTiles;
    }

    /**
     * Gibt die Anzahl der Steine der Maschine auf dem Spielfeld zurück.
     *
     * @return      Entspricht der Anzahl der Steine.
     */
    @Override
    public int getNumberOfMachineTiles() {
        return numberOfMachineTiles;
    }

    /**
     * Gibt den Inhalt des Felds zurück, wobei {@code null} für ein leeres
     * Feld steht.
     *
     * @param row                       Entspricht der Zeile des Spielfelds.
     * @param col                       Entspricht der Spalte des Spielfelds.
     * @return                          Gibt den Spieler des Steines auf dem
     *                                  Feld zurück. Falls {@code null}
     *                                  zurückgegeben wird, ist das Feld leer.
     * @throws IllegalArgumentException Wird geworfen, falls {@code row} oder
     *                                  {@code col} nicht positiv oder zu groß
     *                                  sind.
     */
    @Override
    public Player getSlot(int row, int col) {
        if (row > 0 && col > 0 && row <= Board.SIZE && col <= Board.SIZE) {
            return gameBoard[row - 1][col - 1];
        } else {
            throw new IllegalArgumentException("Row or col is negative"
                + " or too big!");
        }
    }

    /**
     * Klont das gesamte Spielobjekt tief und gibt den Klon darauf zurück.
     *
     * @return                          Entspricht dem Klon des Objekts.
     * @throws IllegalStateException    Wird geworfen, falls {@code Reversi}
     *                                  unvorhergesehen nicht klonbar ist.
     */
    @Override
    public Reversi clone() {
        Reversi copy;
        try {
            copy = (Reversi) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Reversi has to be cloneable!");
        }
        Player[][] gameCopy = gameBoard.clone();
        for (int i = 0; i < gameCopy.length; i++) {
            gameCopy[i] = gameCopy[i].clone();
        }
        copy.gameBoard = gameCopy;
        return copy;
    }

    /**
     * Gibt die kanonische Darstellung des Spielfelds zurück. Dabei steht "."
     * für ein leeres Feld, "O" für die Maschine, "X" für den Menschen.
     *
     * @return          Entspricht der Darstellung des Spielfelds.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= gameBoard.length; i++) {
            for (int u = 1; u <= gameBoard[i - 1].length; u++) {
                Player playerOfSlot = getSlot(i, u);

                if (playerOfSlot == null) {
                    stringBuilder.append('.');
                } else if (playerOfSlot == Player.HUMAN) {
                    stringBuilder.append('X');
                } else {
                    stringBuilder.append('O');
                }

                if (u != gameBoard[i - 1].length) {
                    stringBuilder.append(' ');
                }
            }
            if (i != gameBoard.length) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Führt für den nächsten Spieler einen Zug auf einem Klon aus, falls
     * dieser legal ist.
     *
     * @param row                           Entspricht der Zeile auf der der
     *                                      Stein gelegt werden soll.
     * @param col                           Entspricht der Spalte auf der der
     *                                      Stein gelegt werden soll.
     * @return                              Falls der Zug legal ist, wird ein
     *                                      Klon zurückgegeben, auf dem der
     *                                      Zug ausgeführt wurde, andernfalls
     *                                      wird {@code null} zurückgegeben.
     * @see                                 #next()
     * @see                                 #checkLegalityOfMove(int, int,
     *                                      Player)
     * @see                                 #executeMove(int, int, List)
     *
     */
    Reversi moveForNextPlayer(int row, int col) {
        assert row > 0 && row <= Board.SIZE : "Row is not positive or to big!";
        assert col > 0 && col <= Board.SIZE : "Col is not positive or to big!";
        assert next() != null : "Next player cannot be undefined!";

        List<Direction> legalDirections = checkLegalityOfMove(row, col, next());
        if (legalDirections.size() > 0) {
            return executeMove(row, col, legalDirections);
        } else {
            return null;
        }
    }

    /**
     * Prüft, wie viele legale Züge für einen Spieler möglich sind. Falls
     * kein Zug möglich ist, muss ein Spieler aussetzen.
     *
     * @param player            Entspricht dem Spieler, für den die Anzahl der
     *                          legalen Züge berechnet wird.
     * @return                  Es wird die Anzahl an legalen, möglichen Zügen
     *                          zurückgegeben.
     * @see                     #checkLegalityOfMove(int, int, Player)
     */
    int numberOfLegalMoves(Player player) {
        assert player != null : "Player cannot be undefined!";

        int counter = 0;
        for (int i = 1; i <= gameBoard.length; i++) {
            for (int u = 1; u <= gameBoard[i - 1].length; u++) {
                if (checkLegalityOfMove(i, u, player).size() > 0) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Prüft, ob ein Zug legal ist, also mindestens ein gegnerischer Stein
     * umgedreht wird und die gegnerischen Steine zwischen zwei eigenen
     * Steinen liegen. Zudem dürfen keine leeren Felder zwischen den eigenen
     * Steinen liegen. Zur Ausführung werden alle Richtungen zurückgegeben,
     * die legal sind.
     *
     * @param row               Entspricht der Zeile auf der der Stein gelegt
     *                          werden soll.
     * @param col               Entspricht der Spalte auf der der Stein gelegt
     *                          werden soll.
     * @param player            Entspricht dem Spieler, für den geprüft werden
     *                          soll, ob der Zug legal ist.
     * @return                  Falls ein Zug legal sind, werden alle legalen
     *                          Richtungen zurückgegeben, andernfalls wird
     *                          keine Richtung zurückgegeben.
     */
    private List<Direction> checkLegalityOfMove(int row, int col,
                                                 Player player) {
        assert row > 0 && row <= Board.SIZE : "Row is not positive or to big!";
        assert col > 0 && col <= Board.SIZE : "Col is not positive or to big!";
        assert player != null : "Player cannot be undefined!";

        List<Direction> legalDirections = new ArrayList<>();
        if (getSlot(row, col) == null) {
            for (Direction direction : Direction.values()) {
                int rowToCheck = row + direction.getY();
                int colToCheck = col + direction.getX();
                int counter = 0;
                boolean isLegal = false;
                boolean endLoop = false;

                while (!endLoop && rowToCheck <= Board.SIZE && rowToCheck > 0
                        && colToCheck <= Board.SIZE && colToCheck > 0) {
                    Player playerOfSlot = getSlot(rowToCheck, colToCheck);

                    if (playerOfSlot == player.inverse()) {
                        counter++;
                    } else if (playerOfSlot == player) {
                        if (counter > 0) {
                            isLegal = true;
                        }
                        endLoop = true;
                    } else {
                        endLoop = true;
                    }
                    rowToCheck += direction.getY();
                    colToCheck += direction.getX();
                }

                if (isLegal) {
                    legalDirections.add(direction);
                }
            }
        }
        return legalDirections;
    }

    /**
     * Führt einen bereits vorher auf Legalität geprüften Zug auf einem Klon
     * aus, wobei der Zug für {@code nextPlayer} ausgeführt wird. Außerdem
     * wird der nächste Spieler berechnet. Falls vorher der Zug nicht auf
     * Legalität geprüft wurde, können nicht mögliche Spielzustände
     * eintreten!
     *
     * @param row               Entspricht der Zeile auf der der Stein gelegt
     *                          werden soll.
     * @param col               Entspricht der Spalte auf der der Stein gelegt
     *                          werden soll.
     * @param directions        Entsprechen den Richtungen, in denen Steine
     *                          umgedreht werden müssen.
     * @return                  Gibt einen Klon zurück, auf dem der Zug
     *                          ausgeführt wurde.
     * @see                     #setNextPlayer()
     */
    private Reversi executeMove(int row, int col, List<Direction> directions) {
        assert row > 0 && row <= Board.SIZE : "Row is not positive or to big!";
        assert col > 0 && col <= Board.SIZE : "Col is not positive or to big!";
        assert directions.size() > 0 : "The move is not legal!";
        assert nextPlayer != null : "Next player cannot be undefined!";

        Reversi copy = clone();
        copy.gameBoard[row - 1][col - 1] = nextPlayer;
        if (nextPlayer == Player.HUMAN) {
            copy.numberOfHumanTiles++;
        } else {
            copy.numberOfMachineTiles++;
        }

        for (Direction direction: directions) {
            boolean endLoop = false;
            int rowToInverse = row + direction.getY();
            int colToInverse = col + direction.getX();

            while (!endLoop && rowToInverse <= Board.SIZE && rowToInverse > 0
                    && colToInverse <= Board.SIZE && colToInverse > 0) {
                Player playerOfSlot = getSlot(rowToInverse, colToInverse);

                if (playerOfSlot == nextPlayer.inverse()) {
                    copy.gameBoard[rowToInverse - 1][colToInverse - 1]
                            = nextPlayer;
                    if (nextPlayer == Player.HUMAN) {
                        copy.numberOfHumanTiles++;
                        copy.numberOfMachineTiles--;
                    } else {
                        copy.numberOfMachineTiles++;
                        copy.numberOfHumanTiles--;
                    }
                } else {
                    endLoop = true;
                }
                rowToInverse += direction.getY();
                colToInverse += direction.getX();
            }
        }
        copy.setNextPlayer();
        return copy;
    }

    /**
     * Setzt nach einem Zug den nächsten Spieler, wobei beachtet werden muss,
     * dass es ebenfalls vorkommen kann, das ein bzw beide Spieler aussetzen
     * müssen. Falls beide aussetzen müssen, ist das Spiel vorbei.
     *
     * @see                         #numberOfLegalMoves(Player)
     */
    private void setNextPlayer() {
        assert nextPlayer != null : "Old player cannot be undefined!";

        nextPlayer = nextPlayer.inverse();
        if (numberOfLegalMoves(nextPlayer) == 0) {
            if (numberOfLegalMoves(nextPlayer.inverse()) == 0) {
                gameOver = true;
            } else {
                nextPlayer = nextPlayer.inverse();
            }
        }
    }

    /**
     * Setzt die Anfangsposition des Spielfelds in Abhängigkeit der Größe des
     * Spielfelds.
     */
    private void setInitialPosition() {
        assert firstPlayer != null : "First player cannot be undefined!";

        int median = Board.SIZE / 2 - 1;
        gameBoard[median][median] = firstPlayer.inverse();
        gameBoard[median + 1][median] = firstPlayer;
        gameBoard[median][median + 1] = firstPlayer;
        gameBoard[median + 1][median + 1] = firstPlayer.inverse();
    }
}
