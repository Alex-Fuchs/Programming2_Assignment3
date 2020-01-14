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

        setLayout(new GridLayout(1, numberOfMenuItems, horizontalGap, 0));
        setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
        addMenuItems();
    }

    void updateScores() {
        humanScore.setText(String.valueOf(GuiToModel.getNumberOfHumanTiles()));
        machineScore.setText(String.valueOf(GuiToModel.getNumberOfMachineTiles()));
    }

    void updateUndo() {
        if (GuiToModel.undoIsPossible()) {
            undo.setEnabled(true);
        }
    }

    private void addMenuItems() {
        humanScore = createScoreJLabel(Color.BLUE);
        add(humanScore);

        addComboBox();
        addButtons();

        machineScore = createScoreJLabel(Color.RED);
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
                parent.getGameBoard().updateGameBoard();
                updateScores();
            }
        });
        add(createNewGame);

        JButton switchPlayerOrder = new JButton("<HTML><U>S</U>witch</HTML>");
        switchPlayerOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiToModel.switchPlayerOrder();
                parent.getGameBoard().updateGameBoard();
                updateScores();
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
                    if (!GuiToModel.undoIsPossible()) {
                        undo.setEnabled(false);
                    }
                    parent.getGameBoard().updateGameBoard();
                    updateScores();
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
                Frame gui = (Frame) quit.getTopLevelAncestor();
                gui.dispose();
            }
        });
        add(quit);
    }
}
