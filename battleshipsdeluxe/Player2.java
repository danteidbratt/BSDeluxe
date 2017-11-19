package battleshipsdeluxe;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Player2{
    
    int playerNumber;
    int fieldSize;
    int shipsPlaced;
    int shipsHit;
    int ammo;
    boolean vertical;
    int aimX;
    int aimY;
    
    GUI gui;
    ObjectOutputStream out;
    ObjectInputStream in;
    SessionDeluxe s;
    
    Color backgroundColor;
    Color gridColor;
    Color floatingShipColor;
    Color sunkenShipColor;
    Color placementAimColor;
    
    public Player2(int playerNumber, SessionDeluxe s, ObjectInputStream in, ObjectOutputStream out, int fieldSize){
        this.playerNumber = playerNumber;
        this.in = in;
        this.out = out;
        this.s = s;
        this.fieldSize = fieldSize;
        this.backgroundColor = s.backgroundColor;
        this.gridColor = s.gridColor;
        this.floatingShipColor = s.floatingShipColor;
        this.sunkenShipColor = s.sunkenShipColor;
        this.placementAimColor = s.placementAimColor;
        shipsPlaced = 0;
        vertical = true;
        gui = new GUI(s);
        gui.field.addKeyListener(ka);
    }
    
    public void goTime() throws IOException, ClassNotFoundException, InterruptedException {
        gui.setWindowBasics(playerNumber);
        gui.field.setFocusable(true);
        gui.field.requestFocusInWindow();
        gui.cancelButton.addMouseListener(ma);
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = s.ammo;
        addListenersToGrid();
        gui.infoLabel1.setText("       Place your ships");
        gui.infoLabel3.setText("Press 'R' to rotate       ");
        gui.revalidate();
        gui.repaint();
        while((s = (SessionDeluxe)in.readObject()).getState() == 2){
            bombReceived(s.getBombCoordinates()[0], s.getBombCoordinates()[1]);
            if (s.getState() > 2)
                break;
            out.writeObject(s);
        }
        if(s.getState() == 3)
            gui.infoLabel3.setText("Defeat       ");
        if(s.getState() == 4)
            gui.infoLabel3.setText("Victory       ");
        out.writeObject(s);
        Thread.sleep(3000);
        s = new SessionDeluxe();
        s.nukeState = 0;
        in.readObject();
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
                        if (e.getSource() == gui.squares[i][j]){
                            if (isPlaceable()){
                                shipsPlaced++;
                                if (vertical) {
                                    for (int k = i-1; k < i+2; k++) {
                                        gui.squares[k][j].setBackground(floatingShipColor);
                                        s.setShipCoordinates(j, k);
                                    }
                                }
                                else {
                                    for (int k = j-1; k < j+2; k++) {
                                        gui.squares[i][k].setBackground(floatingShipColor);
                                        s.setShipCoordinates(k, i);
                                    }
                                }
                                if(shipsPlaced == s.numberOfShips){
                                    s.setState(2);
                                    gui.infoLabel1.setText("");
                                    gui.infoLabel2.setText("Shots remaining: " + ammo);
                                    for (JLabel[] square : gui.squares) {
                                        for (JLabel square1 : square) {
                                            square1.setBorder(BorderFactory.createLineBorder(gridColor, 1));
                                        }
                                    }
                                    removeListenersFromGrid();
                                    try {
                                        out.writeObject(s);
                                    } catch (IOException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                }

//                                if(gui.squares[i][j].getBackground() == backgroundColor){
//                                    shipsPlaced++;
//                                    s.setShipCoordinates(j, i);
//                                    gui.squares[i][j].setBackground(floatingShipColor);
//                                    if(shipsPlaced > s.numberOfShips-1){
//                                        try {
//                                            s.setState(2);
//                                            out.writeObject(s);
//                                        } catch (IOException ex) {
//                                            System.out.println(ex.getMessage());
//                                        }
//                                        gui.infoLabel1.setText("");
//                                        gui.infoLabel2.setText("Shots remaining: " + ammo);
//                                        gui.squares[i][j].setBorder(BorderFactory.createLineBorder(gridColor, 1));
//                                        removeListenersFromGrid();
//                                    }
//                                } 
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(gridColor);
                gui.cancelButton.setForeground(backgroundColor);
            } else {
                for (int i = 1; i < gui.squares.length-1; i++) {
                    for (int j = 1; j < gui.squares.length-1; j++) {
                        if(e.getSource() == gui.squares[i][j]){
                            aimX = j;
                            aimY = i;
                            if(vertical){
                                for (int k = i-1; k < i+2; k++) {
                                    gui.squares[k][j].setBorder(BorderFactory.createLineBorder(placementAimColor, 5));
                                }
                            }
                            else {
                                for (int k = j-1; k < j+2; k++) {
                                    gui.squares[i][k].setBorder(BorderFactory.createLineBorder(placementAimColor, 5));
                                }
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
            } else {
                for (JLabel[] square : gui.squares) {
                    for (JLabel square1 : square) {
                        square1.setBorder(BorderFactory.createLineBorder(gridColor, 1));
                    }
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
    
    public void bombReceived(int x, int y) throws IOException{
        gui.infoLabel2.setText("Shots remaining: " + --ammo);
        if (s.nukeState == 1) {
            for (int i = y-1; i < y+2; i++) {
                for (int j = x-1; j < x+2; j++) {
                    if (gui.squares[i][j].getBackground() == floatingShipColor){
                        gui.squares[i][j].setBackground(sunkenShipColor);
                        shipsHit++;
                    } else if (gui.squares[y][x].getBackground() == backgroundColor) {
                        gui.squares[i][j].setText("X");
                    }
                    if (shipsHit == s.getShipCoordinates().length){
                        s.setState(3);
                    }
                    else if (ammo < 1) {
                        s.setState(4);
                    }
                }
            }
            gui.repaint();
            s.nukeState = 2;
        }
        else {
            if (gui.squares[y][x].getBackground() == floatingShipColor){
                gui.squares[y][x].setBackground(sunkenShipColor);
                shipsHit++;
            } else if (gui.squares[y][x].getBackground() == backgroundColor) {
                gui.squares[y][x].setText("X");
            }
            if (shipsHit == s.getShipCoordinates().length){
                s.setState(3);
            }
            else if (ammo < 1) {
                s.setState(4);
            }
        }
    }
    
    KeyAdapter ka = new KeyAdapter(){
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'r' && s.getState() == 1) {
                System.out.println("R");
                for (JLabel[] square : gui.squares) {
                    for (JLabel square1 : square) {
                        square1.setBorder(BorderFactory.createLineBorder(gridColor, 1));
                    }
                }
                if (vertical) {
                    for (int i = aimX-1; i < aimX+2; i++) {
                        gui.squares[aimY][i].setBorder(BorderFactory.createLineBorder(placementAimColor, 5));
                    }
                    vertical = false;
                    }
                else {
                    for (int j = aimY-1; j < aimY+2; j++) {
                        gui.squares[j][aimX].setBorder(BorderFactory.createLineBorder(placementAimColor, 5));
                    }
                    vertical = true;
                }
            }
            gui.repaint();
        }
    };
    
    public boolean isPlaceable(){
        if (vertical) {
            if(aimY < 2 || aimY > gui.squares.length-3) {
                return false;
            }
            for (int i = aimY-1; i < aimY+2; i++) {
                for (int[] sc : s.getShipCoordinates()) {
                    if (sc[0] == aimX && sc[1] == i)
                        return false;
                }
            }
        }
        else {
            if(aimX < 2 || aimX > gui.squares.length-3) {
                return false;
            }
            for (int i = aimX-1; i < aimX+2; i++) {
                for (int[] sc : s.getShipCoordinates()) {
                    if (sc[0] == i && sc[1] == aimY)
                        return false;
                }
            }
        }
        return true;
    }
}