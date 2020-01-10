package de.uni_passau.fim.prog2.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class Menu extends JPanel {

    Menu() {
        final int numberOfMenuItems = 7;
        final int horizontalGap = 5;
        final int verticalGap = 5;

        setLayout(new GridLayout(1, numberOfMenuItems, horizontalGap, 0));
        setBorder(new EmptyBorder(verticalGap, 0, verticalGap, 0));
        createMenuItems();
    }

    private void createMenuItems() {
        add(createScoreJLabel(Color.BLUE));

        Integer[] itemsOfJComboBox = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox jComboBox = new JComboBox<>(itemsOfJComboBox);
        jComboBox.setSelectedItem(3);
        add(jComboBox);

        JButton createNewGame = new JButton("<HTML><U>N</U>ew</HTML>");
        add(createNewGame);

        JButton switchPlayerOrder = new JButton("<HTML><U>S</U>witch</HTML>");
        add(switchPlayerOrder);

        JButton undo = new JButton("<HTML><U>U</U>ndo</HTML>");
        add(undo);

        JButton quit = new JButton("<HTML><U>Q</U>uit</HTML>");
        add(quit);

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

}
