package battleshipsdeluxe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class GUI extends JFrame{
    JPanel field = new JPanel();
    JLabel[][] squares;
    JPanel infoPane = new JPanel();
    JLabel infoLabel1 = new JLabel("");
    JLabel infoLabel2 = new JLabel("");
    JLabel infoLabel3 = new JLabel("");
    
    int fieldSize;
    int playerNumber;

    public void setWindowBasics(int fieldSize, int playerNumber) {
        this.playerNumber = playerNumber;
        this.fieldSize = fieldSize;
        setLayout(new BorderLayout());
        field.setLayout(new GridLayout(fieldSize, fieldSize));
        field.setPreferredSize(new Dimension(500, 500));
        setSquares();
        setField();
        infoLabel1.setHorizontalAlignment(SwingConstants.LEFT);
        infoLabel1.setBackground(Color.BLACK);
        infoLabel1.setOpaque(true);
        infoLabel1.setForeground(Color.GREEN);
        infoLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel2.setVerticalAlignment(SwingConstants.CENTER);
        infoLabel2.setBackground(Color.BLACK);
        infoLabel2.setOpaque(true);
        infoLabel2.setForeground(Color.GREEN);
        infoLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
        infoLabel3.setBackground(Color.BLACK);
        infoLabel3.setOpaque(true);
        infoLabel3.setForeground(Color.GREEN);
        infoPane.setLayout(new GridLayout(1, 3));
        infoPane.setPreferredSize(new Dimension(500, 30));
        infoPane.setBackground(Color.BLACK);
        infoPane.setOpaque(true);
        infoPane.add(infoLabel1);
        infoPane.add(infoLabel2);
        infoPane.add(infoLabel3);
        add(field, BorderLayout.CENTER);
        add(infoPane, BorderLayout.SOUTH);
        setBackground(Color.BLACK);
        pack();
        setTitle("Battleships");
        setResizable(false);
        if(playerNumber == 1)
            setLocation(100, 100);
        if(playerNumber == 2)
            setLocation(700, 100);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setSquares(){
        squares = new JLabel[fieldSize][fieldSize];
        for (JLabel[] square : squares) {
            for (int j = 0; j < square.length; j++) {
                square[j] = new JLabel();
                square[j].setBackground(Color.BLACK);
                square[j].setOpaque(true);
                square[j].setForeground(Color.GREEN);
                square[j].setHorizontalAlignment(SwingConstants.CENTER);
                square[j].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
            }
        }
    }
    
    public void setField(){
        field.removeAll();
        for (JLabel[] square : squares) {
            for (JLabel jLabel : square) {
                field.add(jLabel);
            }
        }
        revalidate();
        repaint();
    }
}