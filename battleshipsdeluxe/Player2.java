package battleshipsdeluxe;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JLabel;

public class Player2{
    
    int playerNumber;
    int fieldSize;
    int shipsPlaced;
    int shipsHit;
    int ammo;
    
    GUI gui = new GUI();
    Thread activity2;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean loopAnimation;
    SessionDeluxe s;
    
    public Player2(int playerNumber, SessionDeluxe s, ObjectInputStream in, ObjectOutputStream out){
        this.playerNumber = playerNumber;
        this.in = in;
        this.out = out;
        this.s = s;
        gui.setWindowBasics(5, playerNumber);
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = 10;
    }
    
    public void fuckingGoTime() throws IOException, ClassNotFoundException {
        addListenersToAll();
        gui.infoLabel1.setText("Place your ships");
        s = (SessionDeluxe)in.readObject();
        out.writeObject(s);
        while(ammo > 0 && shipsHit < s.getShipCoordinates().size()){
            s = (SessionDeluxe)in.readObject();
            bombReceived(s.getBombCoordinates()[0], s.getBombCoordinates()[1]);
            out.writeObject(s);
        }
    }
    
    public void addListenersToAll(){
        for (JLabel[] square : gui.squares) {
            for (JLabel square1 : square) {
                square1.addMouseListener(p2Listener);
            }
        }
    }
    
    public void removeListenersFromAll(){
        for (JLabel[] square : gui.squares) {
            for (JLabel square1 : square) {
                square1.removeMouseListener(p2Listener);
            }
        }
    }
    
    MouseAdapter p2Listener = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
            for (int i = 0; i < gui.squares.length; i++) {
                for (int j = 0; j < gui.squares[i].length; j++) {
                    if(e.getSource() == gui.squares[i][j]){
                        if(gui.squares[i][j].getBackground() == Color.BLACK){
                            gui.squares[i][j].setBackground(Color.GREEN);
                            s.setShipCoordinates(j, i);
                            shipsPlaced++;
                            if(shipsPlaced > 4){
                                try {
                                    out.writeObject(s);
                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                gui.infoLabel1.setText("");
                                gui.infoLabel2.setText("Incoming");
                                removeListenersFromAll();
                            }
                        } 
                    }
                }
            }
        }
    };
    
    public void bombReceived(int x, int y) throws IOException{
        ammo--;
        if (gui.squares[y][x].getBackground() == Color.GREEN){
            gui.squares[y][x].setBackground(Color.RED);
            shipsHit++;
        } else if (gui.squares[y][x].getBackground() == Color.BLACK) {
            gui.squares[y][x].setText("X");
        }
        if (shipsHit >= s.getShipCoordinates().size()){
            gui.infoLabel3.setText("Defeat");
        } else if (ammo < 1) {
            gui.infoLabel3.setText("Victory");
        }
    }
}