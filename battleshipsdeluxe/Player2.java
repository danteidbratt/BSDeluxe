package battleshipsdeluxe;

import java.awt.Color;
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
        gui = new GUI(s);
    }
    
    public void goTime() throws IOException, ClassNotFoundException, InterruptedException {
        gui.setWindowBasics(playerNumber);
        gui.cancelButton.addMouseListener(ma);
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = s.ammo;
        addListenersToGrid();
        gui.infoLabel1.setText("       Place your ships");
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
                        if(e.getSource() == gui.squares[i][j]){
                            if(gui.squares[i][j].getBackground() == backgroundColor){
                                shipsPlaced++;
                                s.setShipCoordinates(j, i);
                                gui.squares[i][j].setBackground(floatingShipColor);
                                if(shipsPlaced > s.numberOfShips-1){
                                    try {
                                        s.setState(2);
                                        out.writeObject(s);
                                    } catch (IOException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                    gui.infoLabel1.setText("");
                                    gui.infoLabel2.setText("Shots remaining: " + ammo);
                                    gui.squares[i][j].setBorder(BorderFactory.createLineBorder(gridColor, 1));
                                    removeListenersFromGrid();
                                }
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
                for (JLabel[] square : gui.squares) {
                    for (JLabel square1 : square) {
                        if (e.getSource() == square1) {
                            square1.setBorder(BorderFactory.createLineBorder(placementAimColor, 5));
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
                        if (e.getSource() == square1) {
                            square1.setBorder(BorderFactory.createLineBorder(gridColor, 1));
                        }
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
}