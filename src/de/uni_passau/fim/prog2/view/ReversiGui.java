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

class ReversiGui extends JFrame implements Observer {

    static final Color HUMAN_COLOR = Color.BLUE;

    static final Color MACHINE_COLOR = Color.RED;

    private JLabel humanScore;

    private JLabel machineScore;

    private JButton undo;

    private ReversiGui(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        final int defaultHeight = 600;
        final int defaultWidth = 600;

        setTitle("Reversi");
        setLayout(new BorderLayout());
        displayData.addObserver(this);

        Container contentPane = getContentPane();
        contentPane.add(new GameBoard(displayData), BorderLayout.CENTER);
        contentPane.add(createMenu(displayData), BorderLayout.SOUTH);

        setFocusable(true);
        addKeyShortCuts();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(defaultWidth, defaultHeight);
        setVisible(true);
    }

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

    private void updateScores(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        humanScore.setText(String.valueOf(displayData.getNumberOfHumanTiles()));
        machineScore.setText(String.valueOf(displayData.getNumberOfMachineTiles()));
    }

    private void updateUndo(DisplayData displayData) {
        assert displayData != null: "DisplayData cannot be null!";

        if (displayData.undoIsPossible()) {
            undo.setEnabled(true);
        } else {
            undo.setEnabled(false);
        }
    }

    private JPanel createMenu(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        final int numberOfMenuItems = 7;
        final int horizontalGap = 5;
        final int verticalGap = 5;

        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(1, numberOfMenuItems, horizontalGap, 0));
        menu.setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));

        humanScore = createScoreJLabel(ReversiGui.HUMAN_COLOR);
        menu.add(humanScore);
        menu.add(createJComboBox(displayData));
        addButtons(menu, displayData);
        machineScore = createScoreJLabel(ReversiGui.MACHINE_COLOR);
        menu.add(machineScore);

        return menu;
    }

    private void addButtons(JPanel menu, DisplayData displayData) {
        assert menu != null : "The menu to add cannot be null!";
        assert displayData != null : "DisplayData cannot be null!";

        JButton createNewGame = new JButton("<HTML><U>N</U>ew</HTML>");
        createNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.createNewBoard();
            }
        });
        menu.add(createNewGame);

        JButton switchPlayerOrder = new JButton("<HTML><U>S</U>witch</HTML>");
        switchPlayerOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.switchPlayerOrder();
            }
        });
        menu.add(switchPlayerOrder);

        undo = new JButton("<HTML><U>U</U>ndo</HTML>");
        undo.setEnabled(false);
        undo.addActionListener(new ActionListener() {
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
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        menu.add(quit);
    }

    private JComboBox createJComboBox(DisplayData displayData) {
        assert displayData != null : "DisplayData cannot be null!";

        final int defaultLevel = 3;
        Integer[] itemsOfJComboBox = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox jComboBox = new JComboBox<>(itemsOfJComboBox);
        jComboBox.setSelectedItem(defaultLevel);
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData.setLevel((int) jComboBox.getSelectedItem());
            }
        });
        return jComboBox;
    }

    private JLabel createScoreJLabel(Color fontColour) {
        assert fontColour != null : "The color for text cannot be null!";

        final Font scoreFont = new Font(null, Font.BOLD, 18);
        final int beginningNumberOfTiles = 2;

        JLabel jLabel = new JLabel(String.valueOf(beginningNumberOfTiles));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(fontColour);
        jLabel.setFont(scoreFont);
        return jLabel;
    }

    private void addKeyShortCuts() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DisplayData displayData = new DisplayData();
                ReversiGui reversiGui = new ReversiGui(displayData);
            }
        });
    }
}
