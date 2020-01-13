package de.uni_passau.fim.prog2.view;

import de.uni_passau.fim.prog2.model.GuiToModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

class Menu extends JPanel {

    Menu() {
        final int numberOfMenuItems = 7;
        final int horizontalGap = 5;
        final int verticalGap = 5;

        setLayout(new GridLayout(1, numberOfMenuItems, horizontalGap, 0));
        setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
        addMenuItems();
    }

    private void addMenuItems() {
        add(createScoreJLabel(Color.BLUE));
        addComboBox();
        addButtons();
        add(createScoreJLabel(Color.RED));
    }

    private JLabel createScoreJLabel(Color fontColour) {
        final Font scoreFont = new Font(null, Font.BOLD, 18);

        JLabel jLabel = new JLabel("2");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(fontColour);
        jLabel.setFont(scoreFont);
        return jLabel;
    }

    private void addComboBox() {
        Integer[] itemsOfJComboBox = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox jComboBox = new JComboBox<>(itemsOfJComboBox);
        jComboBox.setSelectedItem(3);
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
                //Neu zeichen
            }
        });
        add(createNewGame);

        JButton switchPlayerOrder = new JButton("<HTML><U>S</U>witch</HTML>");
        switchPlayerOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiToModel.switchPlayerOrder();
                //Neu zeichnen
            }
        });
        add(switchPlayerOrder);

        JButton undo = new JButton("<HTML><U>U</U>ndo</HTML>");
        undo.setEnabled(false);
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo
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
