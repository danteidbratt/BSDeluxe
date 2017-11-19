package battleshipsdeluxe;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Player1{
    
    int playerNumber;
    int fieldSize;
    int shipsPlaced;
    int shipsHit;
    int ammo;
    int counter = 0;
    int aimX;
    int aimY;
    
    GUI gui;
    Thread activity2;
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket bridge;
    SessionDeluxe s;
    
    Color backgroundColor;
    Color gridColor;
    Color floatingShipColor;
    Color sunkenShipColor;
    Color bombAimColor;
    
    
    public Player1(int playerNumber, SessionDeluxe s, ObjectInputStream in, ObjectOutputStream out, int fieldSize){
        this.playerNumber = playerNumber;
        this.in = in;
        this.out = out;
        this.s = s;
        this.fieldSize = fieldSize;
        this.backgroundColor = s.backgroundColor;
        this.gridColor = s.gridColor;
        this.floatingShipColor = s.floatingShipColor;
        this.sunkenShipColor = s.sunkenShipColor;
        this.bombAimColor = s.bombAimColor;
        gui = new GUI(s);
        gui.field.addKeyListener(ka);
    }
    
    public void goTime() throws IOException, ClassNotFoundException, InterruptedException{
        gui.setWindowBasics(playerNumber);
        gui.infoLabel1.setText("       Waiting for opponent...");
        gui.infoLabel2.setText("");
        gui.revalidate();
        gui.repaint();
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = s.ammo;
        gui.cancelButton.addMouseListener(ma);
        s = (SessionDeluxe)in.readObject();
        gui.setFocusable(true);
        gui.field.setFocusable(true);
        gui.requestFocus();
        gui.field.requestFocusInWindow();
        addListenersToGrid();
        gui.infoLabel4.setText("       Fire away!");
        gui.infoLabel2.setText("Ammo: " + String.valueOf(ammo));
        gui.infoPane.remove(gui.infoLabel1);
        gui.infoPane.add(gui.infoLabel4, 0);
        gui.revalidate();
        gui.repaint();
        
        while(true) {
            s = (SessionDeluxe)in.readObject();
            if(s.getState() > 2)
                break;
        }
        removeListenersFromGrid();
        if (s.getState() == 3) {
            gui.infoLabel3.setText("Victory       ");
        }
        else if (s.getState() == 4) {
            gui.infoLabel3.setText("Defeat       ");
            exposeShips();
        }
        Thread.sleep(3000);
        out.writeObject(s);
    }
    
    public void addListenersToGrid(){
        for (JLabel[] square : gui.squares) {
            for (JLabel square1 : square) {
                square1.addMouseListener(ma);
            }
        }
    }
    
    public void removeListenersFromGrid(){
        for (JLabel[] square : gui.squares) {
            for (JLabel square1 : square) {
                square1.removeMouseListener(ma);
            }
        }
    }
    
    MouseAdapter ma = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() != gui.cancelButton) {
                for (int i = 0; i < gui.squares.length; i++) {
                    for (int j = 0; j < gui.squares[i].length; j++) {
                        if(e.getSource() == gui.squares[i][j]){
                            s.setBombCoordinates(j, i);
                            if (s.nukeState == 1) {
                                for (int k = i-1; k < i+2; k++) {
                                    for (int l = j-1; l < j+2; l++) {
                                        for (int m = 0; m < s.getShipCoordinates().length; m++) {
                                            if((int)s.getShipCoordinates()[m][0] == l &&
                                               (int)s.getShipCoordinates()[m][1] == k &&
                                                gui.squares[k][l].getBackground() != sunkenShipColor){
                                                gui.squares[k][l].setBackground(sunkenShipColor);
                                                shipsHit++;
                                            }
                                        }
                                        if (gui.squares[k][l].getBackground() == backgroundColor && !gui.squares[k][l].getText().equals("X")){
                                            gui.squares[k][l].setText("X");
                                        }
                                    }
                                }
                                gui.infoLabel3.setText("");
                                deactivateNuke();
                            }
                            else {
                                for (int k = 0; k < s.getShipCoordinates().length; k++) {
                                    if((int)s.getShipCoordinates()[k][0] == j &&
                                       (int)s.getShipCoordinates()[k][1] == i &&
                                        gui.squares[i][j].getBackground() != sunkenShipColor){
                                        gui.squares[i][j].setBackground(sunkenShipColor);
                                        shipsHit++;
                                    }
                                }
                                if (gui.squares[i][j].getBackground() == backgroundColor && !gui.squares[i][j].getText().equals("X")){
                                    gui.squares[i][j].setText("X");
                                }
                            }
                            ammo--;
                        }
                    }
                }
                try {
                    out.writeObject(s);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                gui.infoLabel2.setText("Ammo: " + String.valueOf(ammo));
                if (ammo == s.ammo-1){
                    gui.infoLabel3.setText("Press 'N' for Nuke       ");
                }
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(gridColor);
                gui.cancelButton.setForeground(backgroundColor);
            } 
            else {
                for (int i = 0; i < gui.squares.length; i++) {
                    for (int j = 0; j < gui.squares[i].length; j++) {
                        if (e.getSource() == gui.squares[i][j]) {
                            aimX = j;
                            aimY = i;
                            if (s.nukeState == 1) {
                                for (int k = i-1; k < i+2; k++) {
                                    for (int l = j-1; l < j+2; l++) {
                                        gui.squares[k][l].setBorder(BorderFactory.createLineBorder(bombAimColor, 5));
                                    }
                                }
                            } 
                            else {
                                gui.squares[i][j].setBorder(BorderFactory.createLineBorder(bombAimColor, 5));
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(backgroundColor);
                gui.cancelButton.setForeground(gridColor);
            } 
            for (JLabel[] square : gui.squares) {
                for (JLabel square1 : square) {
                    square1.setBorder(BorderFactory.createLineBorder(gridColor, 1));
                }
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(backgroundColor);
                gui.cancelButton.setForeground(gridColor);
            }
        }
            
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                System.exit(0);
            }
        }
    };
    
    KeyAdapter ka = new KeyAdapter(){
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'n') {
                if (s.nukeState == 0) {
                    s.nukeState = 1;
                    activateNuke();
                }
                else if (s.nukeState == 1) {
                    s.nukeState = 0;
                    deactivateNuke();
                }
            }
        }
    };
    
    public void activateNuke(){
        for (int i = aimY-1; i < aimY+2; i++) {
            for (int j = aimX-1; j < aimX+2; j++) {
                gui.squares[i][j].setBorder(BorderFactory.createLineBorder(bombAimColor, 5));
                gui.repaint();
            }
        }
    }
    
    public void deactivateNuke(){
        for (JLabel[] square : gui.squares) {
            for (JLabel square1 : square) {
                square1.setBorder(BorderFactory.createLineBorder(gridColor, 1));
            }
        }
        gui.squares[aimY][aimX].setBorder(BorderFactory.createLineBorder(bombAimColor, 5));
        gui.repaint();
    }
    
    public void exposeShips(){
        for (int[] sc : s.getShipCoordinates()) {
            if (gui.squares[sc[1]][sc[0]].getBackground() == backgroundColor)
                gui.squares[sc[1]][sc[0]].setBackground(floatingShipColor);
        }
    }
}