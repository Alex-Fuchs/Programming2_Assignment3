package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.Observer.Observable;
import de.uni_passau.fim.prog2.Observer.Observer;
import de.uni_passau.fim.prog2.model.DisplayData;
import de.uni_passau.fim.prog2.model.Player;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
     * @param displayData   Entspricht der Spiellogik, die sowohl die
     *                      Controller als auch die Observer benötigen.
     * @see                 #createMenu(DisplayData)
     * @see                 #addWindowListener(DisplayData)
     * @see                 GameBoard
     */
    private ReversiGui(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        setLayout(new BorderLayout());
        Container contentPane = getContentPane();
        contentPane.add(new GameBoard(displayData), BorderLayout.CENTER);
        contentPane.add(createMenu(displayData), BorderLayout.SOUTH);

        displayData.addObserver(this);
        addWindowListener(displayData);

        setTitle("Reversi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    /**
     * Updatet die visuelle Darstellung der Scores und updatet die
     * Verfügbarkeit von {@code undo}.
     *
     * @param o                             Entspricht der Spiellogik, von
     *                                      der Informationen benötigt werden.
     * @param arg                           Entspricht zusätzlichen
     *                                      Informationen, die jedoch dieser
     *                                      {@code Observer} nicht benötigt.
     * @throws IllegalArgumentException     Wird geworfen, falls {@code o} kein
     *                                      Objekt von {@code DisplayData} ist.
     * @see                                 #updateScores(DisplayData)
     * @see                                 #updateUndo(DisplayData)
     */
    @Override
    public void update(Observable o, Object arg) {
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
     * @param displayData   Entspricht der Spiellogik, von der die aktuellen
     *                      Scores benötigt werden.
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
     * Updatet die Verfügbarkeit von {@code undo}.
     *
     * @param displayData   Entspricht der Spiellogik, von der die
     *                      Verfügbarkeit von Undo geprüft werden muss.
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
     * Kreiert die visuelle Darstellung des Menü und fügt die
     * Tastenkombinationen des Spiels hinzu.
     *
     * @param displayData   Entspricht der Spiellogik, die auf Interaktionen
     *                      im Menü reagieren muss.
     * @see                 #addShortCuts(JPanel, DisplayData)
     * @see                 #createScoreJLabel(Color)
     * @see                 #createLevelJComboBox(DisplayData)
     * @see                 #addButtons(JPanel, DisplayData)
     */
    private JPanel createMenu(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        final int numberOfMenuItems = 7;
        final int horizontalGap = 5;
        final int verticalBorder = 5;
        JPanel menu = new JPanel(
                new GridLayout(1, numberOfMenuItems, horizontalGap, 0));
        menu.setBorder(new EmptyBorder(verticalBorder, 0, verticalBorder, 0));
        addShortCuts(menu, displayData);

        humanScore = createScoreJLabel(Player.HUMAN.getColorOfPlayer());
        machineScore = createScoreJLabel(Player.MACHINE.getColorOfPlayer());
        updateScores(displayData);

        menu.add(humanScore);
        menu.add(createLevelJComboBox(displayData));
        addButtons(menu, displayData);
        menu.add(machineScore);
        return menu;
    }

    /**
     * Fügt die {@code JButtons} des Menüs hinzu.
     *
     * @param menu              Entspricht dem gesamten Menü.
     * @param displayData       Entspricht der Spiellogik, die auf die
     *                          Interaktion mit den {@code JButton} reagieren
     *                          muss.
     */
    private void addButtons(JPanel menu, DisplayData displayData) {
        assert menu != null : "The menu to add cannot be null!";
        assert displayData != null : "DisplayData cannot be null!";

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
     * @param displayData   Entspricht der Spiellogik, der das neue Level
     *                      mitgeteilt werden muss.
     * @return              Gibt die erzeugte {@code JComboBox} zurück.
     */
    private JComboBox createLevelJComboBox(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

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
     * Fügt die Tastenkombinationen des Spiels hinzu.
     *
     * @param menu              Die Tastenkombinationen werden beim Menü
     *                          gespeichert.
     * @param displayData       Entspricht der Spiellogik, auf die durch die
     *                          Operationen der Tastenkombinationen zugegriffen
     *                          werden muss.
     */
    private void addShortCuts(JPanel menu, DisplayData displayData) {
        assert menu != null : "The menu to add cannot be null!";
        assert displayData != null : "DisplayData cannot be null!";

        InputMap inputMap = menu.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = menu.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.ALT_DOWN_MASK), "new");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.ALT_DOWN_MASK), "switch");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U,
                InputEvent.ALT_DOWN_MASK), "undo");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.ALT_DOWN_MASK), "quit");

        actionMap.put("new", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.createNewBoard();
            }
        });
        actionMap.put("switch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.switchPlayerOrder();
            }
        });
        actionMap.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayData.undoIsPossible()) {
                    displayData.undo();
                }
            }
        });
        actionMap.put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Stellt sicher, dass beim Schließen der Gui auch die
     * Maschinenberechnungen beendet werden.
     *
     * @param displayData       Entspricht der Spiellogik, der mitgeteilt
     *                          werden muss, das das Spiel nun beendet wird.
     */
    private void addWindowListener(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                displayData.stopMachineThread();
            }
        });
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
