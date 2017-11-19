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
    
    
    public Player1(int playerNumber, SessionDeluxe s, ObjectInputStream in, ObjectOutputStream out, int fieldSize){
        this.playerNumber = playerNumber;
        this.in = in;
        this.out = out;
        this.s = s;
        this.fieldSize = fieldSize;
        gui = new GUI(fieldSize);
    }
    
    public void fuckingGoTime() throws IOException, ClassNotFoundException, InterruptedException{
        gui.setWindowBasics(playerNumber);
        gui.infoLabel1.setText("       Waiting for opponent...");
        gui.infoLabel2.setText("");
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = s.ammo;
        gui.cancelButton.addMouseListener(ma);
        s = (SessionDeluxe)in.readObject();
        addListenersToGrid();
        gui.field.addKeyListener(ka);
        gui.field.setFocusable(true);
        gui.field.requestFocusInWindow();
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
        }
        Thread.sleep(2000);
        s.setState(1);
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
                            for (int k = 0; k < s.getShipCoordinates().length; k++) {
                                if((int)s.getShipCoordinates()[k][0] == j &&
                                   (int)s.getShipCoordinates()[k][1] == i &&
                                    gui.squares[i][j].getBackground() != Color.RED){
                                    gui.squares[i][j].setBackground(Color.RED);
                                    shipsHit++;
                                    ammo--;
                                }
                            }
                            if (gui.squares[i][j].getBackground() == Color.BLACK && !gui.squares[i][j].getText().equals("X")){
                                gui.squares[i][j].setText("X");
                                ammo--;
                            }
                        }
                    }
                }
                try {
                    out.writeObject(s);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                gui.infoLabel2.setText("Ammo: " + String.valueOf(ammo));
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(Color.GREEN);
                gui.cancelButton.setForeground(Color.BLACK);
            } 
            else {
                for (int i = 0; i < gui.squares.length; i++) {
                    for (int j = 0; j < gui.squares[i].length; j++) {
                        if (e.getSource() == gui.squares[i][j]) {
                            if (s.nukeState == 1) {
                                for (int k = i-1; k < i+2; k++) {
                                    for (int l = j-1; l < j+2; l++) {
                                        gui.squares[k][l].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                                    }
                                }
                            } 
                            else {
                                gui.squares[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                            }
                            aimX = j;
                            aimY = i;
                        }
                    }
                }
            }
//            else if (s.nukeState == 1) {
//                for (int i = 1; i < gui.squares.length-1; i++) {
//                    for (int j = 1; j < gui.squares[i].length-1; j++) {
//                        if (e.getSource() == gui.squares[i][j]){
//                            for (int k = i-1; k < i+2; k++) {
//                                for (int l = j-1; l < j+2; l++) {
//                                    gui.squares[k][l].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
//                                    aimX = j;
//                                    aimY = i;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(Color.BLACK);
                gui.cancelButton.setForeground(Color.GREEN);
            } 
            else {
                for (JLabel[] square : gui.squares) {
                    for (JLabel square1 : square) {
//                        if (e.getSource() == square1) {
                            square1.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
//                        }
                    }
                }
            }
//            else if (s.nukeState == 1) {
//                for (int i = 1; i < gui.squares.length-1; i++) {
//                    for (int j = 1; j < gui.squares[i].length-1; j++) {
//                        if (e.getSource() == gui.squares[i][j]){
//                            for (int k = i-1; k < i+2; k++) {
//                                for (int l = j-1; l < j+2; l++) {
//                                    gui.squares[k][l].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(Color.BLACK);
                gui.cancelButton.setForeground(Color.GREEN);
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
                if (s.nukeState != 1)
                    activateNuke();
                else 
                    deactivateNuke();
            }
        }
    };
    
    public void activateNuke(){
        s.nukeState = 1;
        for (int i = aimY-1; i < aimY+2; i++) {
            for (int j = aimX-1; j < aimX+2; j++) {
                gui.squares[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                gui.repaint();
            }
        }
    }
    
    public void deactivateNuke(){
        s.nukeState = 0;
        for (JLabel[] square : gui.squares) {
            for (JLabel square1 : square) {
                square1.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
            }
        }
        gui.squares[aimY][aimX].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        gui.repaint();
    }
}