package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.Observer.Observable;
import de.uni_passau.fim.prog2.Observer.Observer;
import de.uni_passau.fim.prog2.model.DisplayData;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * Implementiert die visuelle Darstellung von Reversi inkl Menü. Das Menü
 * beinhaltet {@code JButtons}, um ein neues Spiel zu beginnen, den Eröffner zu
 * wechseln, einen Spielzug rückgängig zu machen und für die Levelauswahl
 * eine {@code JComboBox}. Es ist ebenfalls möglich das Menü mit
 * Tastenkombinationen zu benutzen. Die Klasse implementiert das Interface
 * {@code Observer}, da diese von {@code DisplayData} geupdatet wird.
 *
 * @version 15.01.20
 * @author -----
 */
public final class ReversiGui extends JFrame implements Observer {

    /**
     * Entspricht der visuellen Anzeige des menschlichen Scores.
     */
    private JLabel humanScore;

    /**
     * Entspricht der visuellen Anzeige des Maschinenscores.
     */
    private JLabel machineScore;

    /**
     * Entspricht einem Button, der einen Spielzug des Menschen rückgängig
     * machen kann.
     */
    private JButton undo;

    /**
     * Kreiert die visuelle Darstellung des Spiels.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @see                 #createMenu(DisplayData)
     * @see                 GameBoard
     * @see                 ShortCutKeyAdapter
     */
    private ReversiGui(DisplayData displayData) {
        setTitle("Reversi");
        setLayout(new BorderLayout());
        Container contentPane = getContentPane();
        contentPane.add(new GameBoard(displayData), BorderLayout.CENTER);
        contentPane.add(createMenu(displayData), BorderLayout.SOUTH);
        addKeyListener(new ShortCutKeyAdapter(displayData));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setFocusable(true);
        setVisible(true);
    }

    /**
     * Updatet die visuelle Darstellung der Scores und updatet die
     * Verfügbarkeit von {@code undo}.
     *
     * @param o                             Entspricht der Spiellogik.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code o}
     *                                      {@code null} ist, oder kein Objekt
     *                                      von {@code DisplayData}.
     * @see                                 #updateScores(DisplayData)
     * @see                                 #updateUndo(DisplayData)
     */
    @Override
    public void update(Observable o) {
        if (o instanceof DisplayData) {
            DisplayData displayData = (DisplayData) o;
            updateUndo(displayData);
            updateScores(displayData);
        } else {
            throw new IllegalArgumentException("Observable is illegal!");
        }
    }

    /**
     * Update die visuelle Darstellung der Scores.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @see                 DisplayData#getNumberOfHumanTiles()
     * @see                 DisplayData#getNumberOfMachineTiles()
     */
    private void updateScores(DisplayData displayData) {
        assert displayData != null : "Information is needed from displayData!";

        humanScore.setText(
                String.valueOf(displayData.getNumberOfHumanTiles()));
        machineScore.setText(
                String.valueOf(displayData.getNumberOfMachineTiles()));
    }

    /**
     * Update die Verfügbarkeit von {@code undo}.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @see                 DisplayData#undoIsPossible()
     */
    private void updateUndo(DisplayData displayData) {
        assert displayData != null : "Information is needed from displayData!";

        if (displayData.undoIsPossible()) {
            undo.setEnabled(true);
        } else {
            undo.setEnabled(false);
        }
    }

    /**
     * Kreiert die visuelle Darstellung des Menü.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @see                 #createScoreJLabel(Color)
     * @see                 #createJComboBox(DisplayData)
     * @see                 #addButtons(JPanel, DisplayData)
     */
    private JPanel createMenu(DisplayData displayData) {
        final int numberOfMenuItems = 7;
        final int horizontalGap = 5;
        final int verticalBorder = 5;
        JPanel menu = new JPanel(new GridLayout(1, numberOfMenuItems,
                horizontalGap, 0));
        menu.setBorder(new EmptyBorder(verticalBorder, 0, verticalBorder, 0));

        humanScore = createScoreJLabel(Player.HUMAN.getColorOfPlayer());
        machineScore = createScoreJLabel(Player.MACHINE.getColorOfPlayer());
        updateScores(displayData);
        displayData.addObserver(this);

        menu.add(humanScore);
        menu.add(createJComboBox(displayData));
        addButtons(menu, displayData);
        menu.add(machineScore);
        return menu;
    }

    /**
     * Fügt die {@code JButtons} des Menüs hinzu.
     *
     * @param menu              Entspricht dem gesamten Menü.
     * @param displayData       Entspricht der Spiellogik.
     */
    private void addButtons(JPanel menu, DisplayData displayData) {
        assert menu != null : "The menu to add cannot be null!";

        JButton createNewGame = new JButton("<HTML><U>N</U>ew</HTML>");
        createNewGame.addActionListener(new ActionListener() {

            /**
             * Benachrichtigt die Spiellogik ein neues Spiel zu eröffnen.
             *
             * @param e     Entspricht dem auslösenden Klick.
             * @see         DisplayData#createNewBoard()
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.createNewBoard();
            }
        });
        menu.add(createNewGame);

        JButton switchPlayerOrder = new JButton("<HTML><U>S</U>witch</HTML>");
        switchPlayerOrder.addActionListener(new ActionListener() {

            /**
             * Benachrichtigt die Spiellogik den Eröffner zu wechseln und
             * ein neues Spiel zu starten.
             *
             * @param e     Entspricht dem auslösenden Klick.
             * @see         DisplayData#switchPlayerOrder()
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.switchPlayerOrder();
            }
        });
        menu.add(switchPlayerOrder);

        undo = new JButton("<HTML><U>U</U>ndo</HTML>");
        undo.setEnabled(false);
        undo.addActionListener(new ActionListener() {

            /**
             * Benachrichtigt die Spiellogik einen Zug des menschlichen
             * Spieler rückgängig zu machen, falls dieser bereits gezogen ist.
             *
             * @param e     Entspricht dem auslösenden Klick.
             * @see         DisplayData#undoIsPossible()
             * @see         DisplayData#undo()
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                assert displayData.undoIsPossible() : "Undo has to be disabled";

                displayData.undo();
            }
        });
        menu.add(undo);

        JButton quit = new JButton("<HTML><U>Q</U>uit</HTML>");
        quit.addActionListener(new ActionListener() {

            /**
             * Beendet das Spiel.
             *
             * @param e     Entspricht dem auslösenden Klick.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        menu.add(quit);
    }

    /**
     * Kreiert die Levelauswahl als {@code JComboBox}.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @return              Gibt die erzeugte {@code JComboBox} zurück.
     */
    private JComboBox createJComboBox(DisplayData displayData) {
        final int defaultLevel = 3;
        Integer[] itemsOfJComboBox = {1, 2, 3, 4, 5, 6, 7, 8};
        JComboBox jComboBox = new JComboBox<>(itemsOfJComboBox);
        jComboBox.setSelectedItem(defaultLevel);
        jComboBox.addActionListener(new ActionListener() {

            /**
             * Setzt das Level auf einen positiven Wert.
             *
             * @param e      Entspricht dem auslösenden Klick.
             * @see          DisplayData#setLevel(int)
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.setLevel((int) jComboBox.getSelectedItem());
            }
        });
        return jComboBox;
    }

    /**
     * Kreiert die {@code JLabel} für die Scoreanzeige.
     *
     * @param fontColour    Entspricht der Farbe der Schrift, die für den
     *                      Menschen und die Maschine verschieden sind.
     * @return              Gibt das erzeugte {@code JLabel} zurück.
     */
    private JLabel createScoreJLabel(Color fontColour) {
        assert fontColour != null : "The color for text cannot be null!";

        final Font scoreFont = new Font(null, Font.BOLD, 20);
        JLabel jLabel = new JLabel();
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(fontColour);
        jLabel.setFont(scoreFont);
        return jLabel;
    }

    /**
     * Ermöglicht die Eingabe der Tastaturkombinationen, wobei eine Verzögerung
     * von 0,5 Sekunden unterstützt wird, um eine angenehmere Benutzung zu
     * gewährleisten.
     */
    private class ShortCutKeyAdapter extends KeyAdapter {

        /**
         * Speichert, ob momentan die ALT-Taste gedrückt ist.
         */
        private boolean altIsPressed;

        /**
         * Speichert, ob momentan die n-Taste gedrückt ist.
         */
        private boolean nIsPressed;

        /**
         * Speichert, ob momentan die s-Taste gedrückt ist.
         */
        private boolean sIsPressed;

        /**
         * Speichert, ob momentan die u-Taste gedrückt ist.
         */
        private boolean uIsPressed;

        /**
         * Speichert, ob momentan die q-Taste gedrückt ist.
         */
        private boolean qIsPressed;

        /**
         * Entspricht der Systemzeit der letzten Nutzung einer
         * Tastenkombination.
         */
        private long lastUsage;

        /**
         * Entspricht der minimalen Pausenzeit der Tastenkombinationen,
         * um eine komfortable Bedienung zu garantieren.
         */
        private long minimumDelay = 500_000_000;

        /**
         * Die Spiellogik wird für die Operationen der Tastenkombinationen
         * benötigt.
         */
        private DisplayData displayData;

        /**
         * Erstellt den KeyAdapter, der die Tastenkombinationen des Spiels
         * ermöglicht.
         *
         * @param displayData       Die Spiellogik wird für die Folgen der
         *                          Tastenkombinationen benötigt.
         */
        private ShortCutKeyAdapter(DisplayData displayData) {
            this.displayData = displayData;
        }

        /**
         * Wenn eine Taste gedrückt wird, wird überprüft, ob ebenfalls
         * die ALT-Taste gedrückt wird und darauf wird die jeweilige
         * Operation ausgeführt. Es wird ebenfalls gespeichert, ob eine
         * Taste momentan gedrückt wird.
         *
         * @param e     Entspricht der auslösenden Taste.
         * @see         #setButtonPressed(int, boolean)
         * @see         #checkBothButtonPressed()
         */
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            setButtonPressed(e.getKeyCode(), true);
            checkBothButtonPressed();
        }

        /**
         * Wird benötigt, um zu speichern, ob eine der Tasten noch gedrückt
         * wird.
         *
         * @param e     Entspricht der auslösenden Taste.
         * @see         #setButtonPressed(int, boolean)
         */
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            setButtonPressed(e.getKeyCode(), false);
        }

        /**
         * Prüft, ob die Verzögerung vorbei ist und ob jew einer der Tasten mit
         * der Alt-Taste gedrückt wird und führt dann die Operation aus.
         */
        private void checkBothButtonPressed() {
            if (System.nanoTime() - lastUsage > minimumDelay &&
                    altIsPressed) {
                if (nIsPressed) {
                    displayData.createNewBoard();
                    lastUsage = System.nanoTime();
                } else if (sIsPressed) {
                    displayData.switchPlayerOrder();
                    lastUsage = System.nanoTime();
                } else if (uIsPressed && displayData.undoIsPossible()) {
                    displayData.undo();
                    lastUsage = System.nanoTime();
                } else if (qIsPressed) {
                    dispose();
                }
            }
        }

        /**
         * Setzt, ob die jew Taste noch gedrückt wird oder nicht.
         *
         * @param keyCode       Entspricht der Taste.
         * @param pressed       Entspricht einem Flag, ob die Taste noch
         *                      gedrückt wird.
         */
        private void setButtonPressed(int keyCode, boolean pressed) {
            switch (keyCode) {
                case KeyEvent.VK_N:
                    nIsPressed = pressed;
                    break;
                case KeyEvent.VK_S:
                    sIsPressed = pressed;
                    break;
                case KeyEvent.VK_U:
                    uIsPressed = pressed;
                    break;
                case KeyEvent.VK_Q:
                    qIsPressed = pressed;
                    break;
                case KeyEvent.VK_ALT:
                    altIsPressed = pressed;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Startmethode von dem Programm.
     *
     * @param args  Übergabeparameter des Programms.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReversiGui(new DisplayData());
            }
        });
    }
}
