package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.Observer.Observable;
import de.uni_passau.fim.prog2.Observer.Observer;
import de.uni_passau.fim.prog2.model.DisplayData;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
 * Entspricht der visuellen Darstellung von Reversi inkl Menü. Das Menü
 * beinhaltet {@code JButtons}, um ein neues Spiel zu beginnen, den Eröffner zu
 * wechseln, einen Spielzug rückgängig zu machen und für die Levelauswahl
 * eine {@code JComboBox}. Es ist ebenfalls möglich das Menü mit
 * Tastenkombinationen zu benutzen.
 *
 * @version 15.01.20
 * @author -----
 */
public class ReversiGui extends JFrame implements Observer {

    /**
     * Farbe des menschlichen Spielers, wobei diese für die Steine als auch
     * für die Scoreanzeige verwendet wird.
     */
    static final Color HUMAN_COLOR = Color.BLUE;

    /**
     * Farbe des Maschine, wobei diese für die Steine als auch für die
     * Scoreanzeige verwendet wird.
     */
    static final Color MACHINE_COLOR = Color.RED;

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
     * @see                 #createMenu()
     * @see                 #addKeyShortCuts()
     * @see                 GameBoard
     */
    private ReversiGui() {
        final int defaultHeight = 600;
        final int defaultWidth = 600;
        setTitle("Reversi");
        setLayout(new BorderLayout());
        Container contentPane = getContentPane();
        contentPane.add(new GameBoard(), BorderLayout.CENTER);
        contentPane.add(createMenu(), BorderLayout.SOUTH);
        addKeyShortCuts();

        setFocusable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(defaultWidth, defaultHeight);
        setVisible(true);
    }

    /**
     * Updatet die visuelle Darstellung der Scores und updatet die
     * Verfügbarkeit von {@code undo}.
     *
     * @param o     Entspricht der Spiellogik.
     * @see         #updateScores(DisplayData)
     * @see         #updateUndo(DisplayData)
     */
    @Override
    public void update(Observable o) {
        if (o != null) {
            DisplayData displayData = (DisplayData) o;
            updateUndo(displayData);
            updateScores(displayData);
        } else {
            throw new IllegalArgumentException("Observable cannot be null!");
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
        assert displayData != null : "DisplayData cannot be null!";

        humanScore.setText(String.valueOf(displayData.getNumberOfHumanTiles()));
        machineScore.setText(String.valueOf(displayData.getNumberOfMachineTiles()));
    }

    /**
     * Update die Verfügbarkeit von {@code undo}.
     *
     * @param displayData   Entspricht der Spiellogik.
     * @see                 DisplayData#undoIsPossible()
     */
    private void updateUndo(DisplayData displayData) {
        assert displayData != null: "DisplayData cannot be null!";

        if (displayData.undoIsPossible()) {
            undo.setEnabled(true);
        } else {
            undo.setEnabled(false);
        }
    }

    /**
     * Kreiert die visuelle Darstellung des Spiels.
     *
     * @see                 #createScoreJLabel(Color)
     * @see                 #createJComboBox()
     * @see                 #addButtons(JPanel)
     */
    private JPanel createMenu() {
        final int numberOfMenuItems = 7;
        final int horizontalGap = 5;
        final int verticalBorder = 5;
        JPanel menu = new JPanel(new GridLayout(1, numberOfMenuItems,
                horizontalGap, 0));
        menu.setBorder(new EmptyBorder(verticalBorder, 0, verticalBorder, 0));

        humanScore = createScoreJLabel(ReversiGui.HUMAN_COLOR);
        machineScore = createScoreJLabel(ReversiGui.MACHINE_COLOR);
        DisplayData displayData = DisplayData.getInstance();
        updateScores(displayData);
        displayData.addObserver(this);

        menu.add(humanScore);
        menu.add(createJComboBox());
        addButtons(menu);
        menu.add(machineScore);
        return menu;
    }

    /**
     * Fügt die {@code JButtons} des Menüs hinzu.
     *
     * @param menu              Entspricht dem Menü.
     */
    private void addButtons(JPanel menu) {
        assert menu != null : "The menu to add cannot be null!";

        JButton createNewGame = new JButton("<HTML><U>N</U>ew</HTML>");
        createNewGame.addActionListener(new ActionListener() {

            /**
             * Entspricht der Spiellogik, auf die zugriffen werden muss,
             * falls ein neues Spiel gestartet wird.
             */
            private DisplayData displayData = DisplayData.getInstance();

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
             * Entspricht der Spiellogik, auf die zugriffen werden muss,
             * falls der Eröffner gewechselt und ein neues Spiel gestartet
             * wird.
             */
            private DisplayData displayData = DisplayData.getInstance();

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
             * Entspricht der Spiellogik, auf die zugriffen werden muss,
             * falls ein Spielzug zurückgesetzt wird.
             */
            private DisplayData displayData = DisplayData.getInstance();

            /**
             * Benachrichtigt die Spiellogik einen Zug des menschlichen
             * Spieler rückgängig zu machen, falls dieser bereits gezogen ist.
             *
             * @param e     Entspricht dem auslösenden Klick.
             * @see         DisplayData#createNewBoard()
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayData.undoIsPossible()) {
                    displayData.undo();
                } else {
                    throw new IllegalStateException("Undo should be disabled!");
                }

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
     * @return              Gibt die erzeugte {@code JComboBox} zurück.
     */
    private JComboBox createJComboBox() {
        final int defaultLevel = 3;
        Integer[] itemsOfJComboBox = {1, 2, 3, 4, 5, 6, 7, 8};
        JComboBox jComboBox = new JComboBox<>(itemsOfJComboBox);
        jComboBox.setSelectedItem(defaultLevel);
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int level = (int) jComboBox.getSelectedItem();
                DisplayData.getInstance().setLevel(level);
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

        final Font scoreFont = new Font(null, Font.BOLD, 18);
        JLabel jLabel = new JLabel();
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(fontColour);
        jLabel.setFont(scoreFont);
        return jLabel;
    }

    /**
     * Fügt die Tastatureingabe zu dem {@code JFrame} hinzu.
     */
    private void addKeyShortCuts() {
        addKeyListener(new KeyAdapter() {

            /**
             * Entspricht der Spiellogik, auf die zugegriffen werden muss, um
             * die Operationen auszuführen.
             */
            DisplayData displayData = DisplayData.getInstance();

            /**
             * Speichert, ob momentan die ALT-Taste gedrückt ist.
             */
            boolean altIsPressed;

            /**
             * Wenn eine Taste gedrückt wird, wird überprüft, ob ebenfalls
             * die ALT-Taste gedrückt wird und darauf wird die jeweilige
             * Operation ausgeführt.
             *
             * @param e     Entspricht der auslösenden Taste.
             */
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_ALT) {
                    altIsPressed = true;
                } else if (altIsPressed) {
                    switch (keyCode) {
                    case KeyEvent.VK_N:
                        displayData.createNewBoard();
                        break;
                    case KeyEvent.VK_S:
                        displayData.switchPlayerOrder();
                        break;
                    case KeyEvent.VK_U:
                        if (displayData.undoIsPossible()) {
                            displayData.undo();
                        }
                        break;
                    case KeyEvent.VK_Q:
                        dispose();
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    altIsPressed = false;
                }
            }
        });
    }

    /**
     * Startmethode von dem Programm.
     *
     * @param args  Übergabeparameter des Programms.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReversiGui();
            }
        });
    }
}
