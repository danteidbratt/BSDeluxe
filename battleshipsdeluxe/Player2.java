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
    
    public Player2(int playerNumber, SessionDeluxe s, ObjectInputStream in, ObjectOutputStream out, int fieldSize){
        this.playerNumber = playerNumber;
        this.in = in;
        this.out = out;
        this.s = s;
        this.fieldSize = fieldSize;
        shipsPlaced = 0;
        gui = new GUI(fieldSize);
    }
    
    public void fuckingGoTime() throws IOException, ClassNotFoundException, InterruptedException {
        gui.setWindowBasics(playerNumber);
        gui.cancelButton.addMouseListener(ma);
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = s.ammo;
        addListenersToGrid();
        gui.infoLabel1.setText("       Place your ships");
        
        while((s = (SessionDeluxe)in.readObject()).getState() == 2){
            bombReceived(s.getBombCoordinates()[0], s.getBombCoordinates()[1]);
            if (s.getState() > 2)
                break;
            out.writeObject(s);
        }
        System.out.println("2. under loopen, state: " + s.getState());
        if(s.getState() == 3)
            gui.infoLabel3.setText("Defeat       ");
        if(s.getState() == 4)
            gui.infoLabel3.setText("Victory       ");
        out.writeObject(s);
        Thread.sleep(2000);
        s = new SessionDeluxe();
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
                            if(gui.squares[i][j].getBackground() == Color.BLACK){
                                shipsPlaced++;
                                s.setShipCoordinates(j, i);
                                gui.squares[i][j].setBackground(Color.GREEN);
                                if(shipsPlaced > s.numberOfShips-1){
                                    try {
                                        s.setState(2);
                                        System.out.println("state: " + s.getState() + 
                                                "\nship coordinates");
                                        for (int k = 0; k < s.getShipCoordinates().length; k++) {
                                            for (int l = 0; l < s.getShipCoordinates()[k].length; l++) {
                                                System.out.print(s.getShipCoordinates()[k][l] + "\t");
                                            }
                                            System.out.println();
                                        }
                                        out.writeObject(s);
                                    } catch (IOException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                    gui.infoLabel1.setText("");
                                    gui.infoLabel2.setText("Shots remaining: " + ammo);
                                    gui.squares[i][j].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
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
                gui.cancelButton.setBackground(Color.GREEN);
                gui.cancelButton.setForeground(Color.BLACK);
            } else {
                for (JLabel[] square : gui.squares) {
                    for (JLabel square1 : square) {
                        if (e.getSource() == square1) {
                            square1.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
                        }
                    }
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(Color.BLACK);
                gui.cancelButton.setForeground(Color.GREEN);
            } else {
                for (JLabel[] square : gui.squares) {
                    for (JLabel square1 : square) {
                        if (e.getSource() == square1) {
                            square1.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
                        }
                    }
                }
            }
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
    
    public void bombReceived(int x, int y) throws IOException{
        gui.infoLabel2.setText("Shots remaining: " + --ammo);
        if (gui.squares[y][x].getBackground() == Color.GREEN){
            gui.squares[y][x].setBackground(Color.RED);
            shipsHit++;
        } else if (gui.squares[y][x].getBackground() == Color.BLACK) {
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