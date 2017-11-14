package battleshipsdeluxe;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JLabel;

public class Player1 implements Runnable{
    
    int playerNumber;
    int fieldSize;
    int shipsPlaced;
    int shipsHit;
    int ammo;
    
    GUI gui = new GUI();
    Thread activity2;
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket bridge;
    boolean loopAnimation;
    SessionDeluxe s;
    
    
    public Player1(int playerNumber, SessionDeluxe s, ObjectInputStream in, ObjectOutputStream out){
        this.playerNumber = playerNumber;
        this.in = in;
        this.out = out;
        this.s = s;
    }
    
    public void fuckingGoTime() throws IOException, ClassNotFoundException{
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = 10;
        gui.cancelButton.addMouseListener(ma);
        activity2 = new Thread(this);
        loopAnimation = true;
        activity2.start();
        s = (SessionDeluxe)in.readObject();
        loopAnimation = false;
        addListenersToAll();
        while(true){
            if (s.getState() == 2){
                s = (SessionDeluxe)in.readObject();
            } else {
                break;
            }
        }
        System.out.println("tja1");
    }
    
    public void addListenersToAll(){
        for (JLabel[] square : gui.squares) {
            for (JLabel square1 : square) {
                square1.addMouseListener(ma);
            }
        }
    }
    
    public void removeListenersFromAll(){
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
                gui.infoLabel2.setText("Ammo: " + String.valueOf(--ammo));
                for (int i = 0; i < gui.squares.length; i++) {
                    for (int j = 0; j < gui.squares[i].length; j++) {
                        if(e.getSource() == gui.squares[i][j]){
                            s.setBombCoordinates(j, i);
                            for (int k = 0; k < s.getShipCoordinates().size(); k++) {
                                if((int)s.getShipCoordinates().get(k)[0] == j && (int)s.getShipCoordinates().get(k)[1] == i){
                                    gui.squares[i][j].setBackground(Color.RED);
                                    shipsHit++;
                                }
                            }
                            if (gui.squares[i][j].getBackground() == Color.BLACK){
                                gui.squares[i][j].setText("X");
                            }
                        }
                    }
                }
                if (shipsHit == s.getShipCoordinates().size()){
                    gui.infoLabel3.setText("Victory");
                    s.setState(3);
                } else if (ammo < 1){
                    gui.infoLabel3.setText("Defeat");
                    s.setState(4);
                }
                try {
                    out.writeObject(s);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(Color.GREEN);
                gui.cancelButton.setForeground(Color.BLACK);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == gui.cancelButton){
                gui.cancelButton.setBackground(Color.BLACK);
                gui.cancelButton.setForeground(Color.GREEN);
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
    
    @Override
    public void run(){
        String tempS = "       Wait for opponent";
        while(loopAnimation){
            try {
                gui.infoLabel1.setText(tempS);
                Thread.sleep(500);
                gui.infoLabel1.setText(tempS + ".");
                Thread.sleep(500);
                gui.infoLabel1.setText(tempS + "..");
                Thread.sleep(500);
                gui.infoLabel1.setText(tempS + "...");
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        gui.infoLabel1.setText("Fire away!");
        gui.infoLabel2.setText("Ammo: " + String.valueOf(ammo));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        gui.infoLabel1.setText("");
    }
    }