package battleshipsdeluxe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    JLabel infoLabel4 = new JLabel("");
    JPanel topPanel = new JPanel();
    JLabel cancelButton = new JLabel("Exit Game");
    JPanel rightPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    
    int fieldSize;
    int playerNumber;
    
    public GUI(int fieldSize){
        this.fieldSize = fieldSize;
        squares = new JLabel[fieldSize][fieldSize];
    }

    public void setWindowBasics(int playerNumber) {
        this.playerNumber = playerNumber;
        field.removeAll();
        infoPane.removeAll();
        setLayout(new BorderLayout());
        field.setLayout(new GridLayout(fieldSize, fieldSize));
        field.setPreferredSize(new Dimension(500, 500));
        field.setBackground(Color.BLACK);
        setSquares();
        setField();
        
        infoLabel1.setHorizontalAlignment(SwingConstants.LEFT);
        infoLabel1.setBackground(Color.BLACK);
        infoLabel1.setOpaque(true);
        infoLabel1.setForeground(Color.GREEN);
        
        infoLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel2.setBackground(Color.BLACK);
        infoLabel2.setOpaque(true);
        infoLabel2.setForeground(Color.GREEN);
        infoLabel2.setText("");
        
        infoLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
        infoLabel3.setBackground(Color.BLACK);
        infoLabel3.setOpaque(true);
        infoLabel3.setForeground(Color.GREEN);
        infoLabel3.setText("");
        
        infoLabel4.setHorizontalAlignment(SwingConstants.LEFT);
        infoLabel4.setBackground(Color.BLACK);
        infoLabel4.setOpaque(true);
        infoLabel4.setForeground(Color.GREEN);
        
        infoPane.setLayout(new GridLayout(1, 3));
        infoPane.setPreferredSize(new Dimension(0, 30));
        infoPane.setBackground(Color.BLACK);
        infoPane.setOpaque(true);
        infoPane.add(infoLabel1);
        infoPane.add(infoLabel2);
        infoPane.add(infoLabel3);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setOpaque(true);
        cancelButton.setForeground(Color.GREEN);
        cancelButton.setPreferredSize(new Dimension(0, 30));
        cancelButton.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(cancelButton, BorderLayout.CENTER);
        rightPanel.setBackground(Color.BLACK);
        rightPanel.setOpaque(true);
        rightPanel.setPreferredSize(new Dimension(30, 0));
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setOpaque(true);
        leftPanel.setPreferredSize(new Dimension(30, 0));
        add(topPanel, BorderLayout.NORTH);
        add(field, BorderLayout.CENTER);
        add(infoPane, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);
        add(leftPanel, BorderLayout.WEST);
        pack();
        setTitle("Battleships");
        setResizable(false);
        if(playerNumber == 1)
            setLocation(50, 100);
        if(playerNumber == 2)
            setLocation(650, 100);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setSquares(){
        for (JLabel[] square : squares) {
            for (int j = 0; j < square.length; j++) {
                square[j] = new JLabel();
                square[j].setBackground(Color.BLACK);
                square[j].setOpaque(true);
                square[j].setForeground(Color.GREEN);
                square[j].setFont(new Font("SansSerif", 1, 30));
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