package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.GuiToModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

class Menu extends JPanel {

    private ReversiGui parent;

    private JLabel humanScore;

    private JLabel machineScore;

    private JButton undo;

    Menu(ReversiGui parent) {
        assert parent != null : "Parent cannot be undefined!";

        final int numberOfMenuItems = 7;
        final int horizontalGap = 5;
        final int verticalGap = 5;

        this.parent = parent;
        setFocusable(true);
        setLayout(new GridLayout(1, numberOfMenuItems, horizontalGap, 0));
        setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
        addMenuItems();
        addKeyShortCuts();
    }

    void updateMenu() {
        updateUndo();
        updateScores();
    }

    private void updateScores() {
        humanScore.setText(String.valueOf(GuiToModel.getNumberOfHumanTiles()));
        machineScore.setText(String.valueOf(GuiToModel.getNumberOfMachineTiles()));
    }

    private void updateUndo() {
        if (GuiToModel.undoIsPossible()) {
            undo.setEnabled(true);
        } else {
            undo.setEnabled(false);
        }
    }

    private void addMenuItems() {
        humanScore = createScoreJLabel(ReversiGui.HUMAN_COLOR);
        add(humanScore);
        addComboBox();
        addButtons();
        machineScore = createScoreJLabel(ReversiGui.MACHINE_COLOR);
        add(machineScore);
    }

    private JLabel createScoreJLabel(Color fontColour) {
        final Font scoreFont = new Font(null, Font.BOLD, 18);
        final int beginningNumberOfTiles = 2;

        JLabel jLabel = new JLabel(String.valueOf(beginningNumberOfTiles));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(fontColour);
        jLabel.setFont(scoreFont);
        return jLabel;
    }

    private void addComboBox() {
        final int defaultLevel = 3;

        Integer[] itemsOfJComboBox = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox jComboBox = new JComboBox<>(itemsOfJComboBox);
        jComboBox.setSelectedItem(defaultLevel);
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiToModel.setLevel((int) jComboBox.getSelectedItem());
            }
        });
        add(jComboBox);
    }

    private void addButtons() {
        JButton createNewGame = new JButton("<HTML><U>N</U>ew</HTML>");
        createNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiToModel.createNewBoard();
                updateMenu();
                parent.getGameBoard().updateGameBoard();
            }
        });
        add(createNewGame);

        JButton switchPlayerOrder = new JButton("<HTML><U>S</U>witch</HTML>");
        switchPlayerOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiToModel.switchPlayerOrder();
                updateMenu();
                parent.getGameBoard().updateGameBoard();
            }
        });
        add(switchPlayerOrder);

        undo = new JButton("<HTML><U>U</U>ndo</HTML>");
        undo.setEnabled(false);
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiToModel.undoIsPossible()) {
                    GuiToModel.undo();
                    updateMenu();
                    parent.getGameBoard().updateGameBoard();
                } else {
                    throw new IllegalStateException("Undo should be disabled!");
                }

            }
        });
        add(undo);

        JButton quit = new JButton("<HTML><U>Q</U>uit</HTML>");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.dispose();
            }
        });
        add(quit);
    }

    private void addKeyShortCuts() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.isAltDown()) {
                    switch (e.getKeyChar()) {
                        case 'n':
                            GuiToModel.createNewBoard();
                            updateMenu();
                            parent.getGameBoard().updateGameBoard();
                            break;
                        case 's':
                            GuiToModel.switchPlayerOrder();
                            updateMenu();
                            parent.getGameBoard().updateGameBoard();
                            break;
                        case 'u':
                            if (GuiToModel.undoIsPossible()) {
                                GuiToModel.undo();
                                updateMenu();
                                parent.getGameBoard().updateGameBoard();
                            }
                            break;
                        case 'q':
                            parent.dispose();
                            break;
                    }

                }
            }
        });
    }
}
