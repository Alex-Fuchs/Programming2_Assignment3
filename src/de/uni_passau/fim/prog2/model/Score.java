package de.uni_passau.fim.prog2.model;

/**
 * {@code Score} berechnet für eine Instanz der Klasse {@code Reversi} den
 * Score eines {@code Player}. Diese Bewertung dient als Entscheidungsgrundlage
 * für die Maschine. Die Klasse {@code Score} muss abgeändert werden, falls
 * die Größe des Spielbretts nicht 8 x 8 beträgt, da diese in der Klasse
 * hartkodiert wurde.
 *
 * @version 21.12.19
 * @author -----
 */
class Score {

    /**
     * Entspricht das zu bewertende Spielobjekt.
     */
    private Reversi reversi;

    /**
     * Entspricht den zu bewertenden Spieler.
     */
    private Player playerToAssess;

    /**
     * Entspricht der Bewertung der einzelnen Felder des Spielbretts.
     */
    private static final int[][] FIELD_SCORES = getFieldScores();

    /**
     * Kreiert ein Bewertungsobjekt für ein {@code Reversi} Objekt mit dem
     * zu bewertenden Spieler.
     *
     * @param reversi           Entspricht dem zu bewertenden Spielbrett.
     * @param playerToAssess    Entspricht dem zu bewertenden Spieler.
     */
    Score(Reversi reversi, Player playerToAssess) {
        assert reversi != null : "Reversi to assess cannot be undefined";
        assert playerToAssess != null : "Player to asses cannot be undefined";

        this.reversi = reversi;
        this.playerToAssess = playerToAssess;
    }

    /**
     * Berechnet den Score aus der Sicht des zu bewertenden Spielers
     * {@code playerToAssess}.
     *
     * @return          Gibt den Score des Spielbretts zurück.
     * @see             #calculateFieldScore()
     * @see             #calculateMobilityScore()
     * @see             #calculatePotencialScore()
     */
    double calculateScore() {
        assert reversi != null : "Reversi to assess cannot be undefined";
        assert playerToAssess != null : "Player to asses cannot be undefined";

        double score = 0;
        score += calculateFieldScore();
        score += calculateMobilityScore();
        score += calculatePotencialScore();
        return score;
    }

    /**
     * Berechnet den Score der Felder von {@code playerToAsses}, wobei auch
     * die Felder des Gegners mit einbezogen werden. Jedes Feld wird anders
     * bewertet. Diese Bewertungfunktion hat eine Größe von 8 x 8 hartkodiert
     * und kann somit andere Spielbrette nicht bewerten. Der Score wird mit
     * einer bestimmten Formel berechnet.
     *
     * @return              Gibt den Score der Felder zurück.
     */
    private double calculateFieldScore() {
        assert Board.SIZE == 8 : "Calculating is only for 8x8 possible";
        assert reversi != null : "Reversi to assess cannot be undefined";
        assert playerToAssess != null : "Player to asses cannot be undefined";

        int playerScore = 0;
        int enemyScore = 0;
        for (int i = 1; i <= Board.SIZE; i++) {
            for (int u = 1; u <= Board.SIZE; u++) {
                Player playerOfSlot = reversi.getSlot(i, u);

                if (playerOfSlot == playerToAssess) {
                    playerScore += FIELD_SCORES[i - 1][u - 1];
                } else if (playerOfSlot == playerToAssess.inverse()) {
                    enemyScore += FIELD_SCORES[i - 1][u - 1];
                }
            }
        }
        return playerScore - 1.5 * enemyScore;
    }

    /**
     * Berechnet den Score der Mobilität, den Score der momentan möglichen Züge
     * von {@code playerToAsses}, wobei auch die möglichen Züge des Gegners mit
     * einbezogen werden. Der Score wird mit einer bestimmten Formel berechnet
     * und wird im Laufe des Spiels immer unwichtiger.
     *
     * @return                      Gibt den Score der möglichen Züge zurück.
     * @see                         Reversi#numberOfLegalMoves(Player)
     */
    private double calculateMobilityScore() {
        assert reversi != null : "Reversi to assess cannot be undefined";
        assert playerToAssess != null : "Player to asses cannot be undefined";

        int numberOfFields = Board.SIZE * Board.SIZE;
        int numberOfTakenFields = reversi.getNumberOfHumanTiles()
                                + reversi.getNumberOfMachineTiles();
        int playerScore = reversi.numberOfLegalMoves(playerToAssess);
        int enemyScore = reversi.numberOfLegalMoves(playerToAssess.inverse());
        return (numberOfFields / (double) numberOfTakenFields)
                * (3.0 * playerScore - 4.0 * enemyScore);

    }

    /**
     * Berechnet den Score des Potenzials, den Score der in Zukunft möglichen
     * Züge von {@code playerToAssess}, wobei auch die zukünftigen, möglichen
     * Züge des Gegners mit einbezogen werden. Der Score wird mit einer
     * bestimmten Formel berechnet und wird im Laufe des Spiels immer
     * unwichtiger.
     *
     * @return                      Gibt den Score der zukünftig möglichen
     *                              Züge zurück.
     * @see                         #countWrappingFields(int, int)
     */
    private double calculatePotencialScore() {
        assert reversi != null : "Reversi to assess cannot be undefined";
        assert playerToAssess != null : "Player to asses cannot be undefined";

        int numberOfFields = Board.SIZE * Board.SIZE;
        int numberOfTakenFields = reversi.getNumberOfHumanTiles()
                                + reversi.getNumberOfMachineTiles();
        int playerScore = 0;
        int enemyScore = 0;
        for (int i = 1; i <= Board.SIZE; i++) {
            for (int u = 1; u <= Board.SIZE; u++) {
                Player playerOfSlot = reversi.getSlot(i, u);

                if (playerOfSlot == playerToAssess) {
                    enemyScore += countWrappingFields(i, u);
                } else if (playerOfSlot == playerToAssess.inverse()) {
                    playerScore += countWrappingFields(i, u);
                }
            }
        }
        return (numberOfFields / (2.0 * numberOfTakenFields))
                * (2.5 * playerScore - 3.0 * enemyScore);
    }

    /**
     * Zählt die Anzahl der leeren Felder, die an das Feld in der Zeile
     * {@code row} und in der Spalte {@code col} anliegen.
     *
     * @param row       Entspricht der Zeile des Feldes.
     * @param col       Entspricht der Spalte des Feldes.
     * @return          Gibt die Anzahl der anliegenden Felder zurück.
     */
    private int countWrappingFields(int row, int col) {
        assert row > 0 && row <= Board.SIZE : "Row is not positive or to big!";
        assert col > 0 && col <= Board.SIZE : "Col is not positive or to big!";
        assert reversi != null : "Reversi to assess cannot be undefined";

        int counter = 0;
        for (Direction direction: Direction.values()) {
            int rowToCount = row + direction.getY();
            int colToCount = col + direction.getX();
            if (rowToCount > 0 && rowToCount <= Board.SIZE && colToCount > 0
                    && colToCount <= Board.SIZE) {
                if (reversi.getSlot(rowToCount, colToCount) == null) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Gibt eine 8 x 8 Matrix zurück, wobei in jedem Feld der Score des
     * Spielbrettfeldes gespeichert wird. Falls das Spielfeld nicht 8 x 8
     * groß ist, muss eine neue Bewertungsmatrix gebildet werden.
     *
     * @return      Entspricht der Bewertungsmatrix, die für die
     *              Felderbewertung benötigt wird.
     */
    private static int[][] getFieldScores() {
        assert Board.SIZE == 8 : "Calculating is only for 8x8 possible";

        int[][] result = new int[Board.SIZE][Board.SIZE];

        result[0][0] = 9999;
        result[0][1] = 5;
        result[0][2] = 500;
        result[0][3] = 200;
        result[0][4] = 200;
        result[0][5] = 500;
        result[0][6] = 5;
        result[0][7] = 9999;

        result[1][0] = 5;
        result[1][1] = 1;
        result[1][2] = 50;
        result[1][3] = 150;
        result[1][4] = 150;
        result[1][5] = 50;
        result[1][6] = 1;
        result[1][7] = 5;

        result[2][0] = 500;
        result[2][1] = 50;
        result[2][2] = 250;
        result[2][3] = 100;
        result[2][4] = 100;
        result[2][5] = 250;
        result[2][6] = 50;
        result[2][7] = 500;

        result[3][0] = 200;
        result[3][1] = 150;
        result[3][2] = 100;
        result[3][3] = 50;
        result[3][4] = 50;
        result[3][5] = 100;
        result[3][6] = 150;
        result[3][7] = 200;

        result[4][0] = 200;
        result[4][1] = 150;
        result[4][2] = 100;
        result[4][3] = 50;
        result[4][4] = 50;
        result[4][5] = 100;
        result[4][6] = 150;
        result[4][7] = 200;

        result[5][0] = 500;
        result[5][1] = 50;
        result[5][2] = 250;
        result[5][3] = 100;
        result[5][4] = 100;
        result[5][5] = 250;
        result[5][6] = 50;
        result[5][7] = 500;

        result[6][0] = 5;
        result[6][1] = 1;
        result[6][2] = 50;
        result[6][3] = 150;
        result[6][4] = 150;
        result[6][5] = 50;
        result[6][6] = 1;
        result[6][7] = 5;

        result[7][0] = 9999;
        result[7][1] = 5;
        result[7][2] = 500;
        result[7][3] = 200;
        result[7][4] = 200;
        result[7][5] = 500;
        result[7][6] = 5;
        result[7][7] = 9999;

        return result;
    }
}
