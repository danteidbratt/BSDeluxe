
package battleshipsdeluxe;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class ClientDeluxe extends JFrame implements Runnable{
    
    int playerNumber;
    int fieldSize;
    int shipsPlaced;
    int shipsHit;
    int ammo;
    
    JPanel field = new JPanel();
    JLabel[][] squares;
    JPanel infoPane = new JPanel();
    JLabel infoLabel1 = new JLabel("");
    JLabel infoLabel2 = new JLabel("");
    JLabel infoLabel3 = new JLabel("");
    Thread activity2;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean loopAnimation;
    private SessionDeluxe s;
    
    
    public ClientDeluxe() throws IOException, ClassNotFoundException{
        shipsPlaced = 0;
        shipsHit = 0;
        ammo = 10;
        Socket bridge = new Socket("127.0.0.1", 12321);
        out = new ObjectOutputStream(bridge.getOutputStream());
        in = new ObjectInputStream(bridge.getInputStream());
        s = (SessionDeluxe)in.readObject();
        playerNumber = s.getPlayerNumber();
    }
    
    public void setWindowBasics(int fieldSize){
        setLayout(new BorderLayout());
        this.fieldSize = fieldSize;
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
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j] = new JLabel();
                squares[i][j].setBackground(Color.BLACK);
                squares[i][j].setOpaque(true);
                squares[i][j].setForeground(Color.GREEN);
                squares[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                squares[i][j].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
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
    
    public void player1() throws IOException, ClassNotFoundException, InterruptedException{
        out.writeObject(s);
        s.resetAll();
        activity2 = new Thread(this);
        activity2.start();
        loopAnimation = true;
        setSquares();
        setField();
        out.writeObject(s);
        s = (SessionDeluxe)in.readObject();
        loopAnimation = false;
        addListenersToAll();
        while(ammo > 0 && shipsHit < s.getShipCoordinates().size()){
            s = (SessionDeluxe)in.readObject();
        }
        removeListenersFromAll();
        s.setState(3);
        out.writeObject(s);
    }
    
    public void player2() throws IOException, ClassNotFoundException{
        addListenersToAll();
        infoLabel1.setText("Place your ships");
        s = (SessionDeluxe)in.readObject();
        out.writeObject(s);
        while(ammo > 0 && shipsHit < s.getShipCoordinates().size()){
            s = (SessionDeluxe)in.readObject();
            bombReceived(s.getBombCoordinates()[0], s.getBombCoordinates()[1]);
            out.writeObject(s);
        }
    }
    

    @Override
    public void run(){
        String tempS = "Wait for opponent";
        while(loopAnimation){
            try {
                infoLabel1.setText(tempS);
                Thread.sleep(500);
                infoLabel1.setText(tempS + ".");
                Thread.sleep(500);
                infoLabel1.setText(tempS + "..");
                Thread.sleep(500);
                infoLabel1.setText(tempS + "...");
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        infoLabel1.setText("Fire away!");
        infoLabel2.setText("Ammo: " + String.valueOf(ammo));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        infoLabel1.setText("");
    }
    
    public void addListenersToAll(){
        for (JLabel[] square : squares) {
            for (JLabel square1 : square) {
                if (playerNumber == 1)
                    square1.addMouseListener(p1Listener);
                if (playerNumber == 2)
                    square1.addMouseListener(p2Listener);
            }
        }
    }
    
    public void removeListenersFromAll(){
        for (JLabel[] square : squares) {
            for (JLabel square1 : square) {
                if (playerNumber == 1)
                    square1.removeMouseListener(p1Listener);
                if (playerNumber == 2)
                    square1.removeMouseListener(p2Listener);
            }
        }
    }
    
    MouseAdapter p1Listener = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
            infoLabel2.setText("Ammo: " + String.valueOf(--ammo));
            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares[i].length; j++) {
                    if(e.getSource() == squares[i][j]){
                        s.setBombCoordinates(j, i);
                        for (int k = 0; k < s.getShipCoordinates().size(); k++) {
                            if((int)s.getShipCoordinates().get(k)[0] == j && (int)s.getShipCoordinates().get(k)[1] == i){
                                squares[i][j].setBackground(Color.RED);
                                shipsHit++;
                            }
                        }
                        if (squares[i][j].getBackground() == Color.BLACK)
                            squares[i][j].setText("X");
                    }
                }
            }
            if (shipsHit == s.getShipCoordinates().size()){
                infoLabel3.setText("Victory");
            } else if (ammo < 1){
                infoLabel3.setText("Defeat");
            }
            try {
                out.writeObject(s);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    };
    
    MouseAdapter p2Listener = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares[i].length; j++) {
                    if(e.getSource() == squares[i][j]){
                        if(squares[i][j].getBackground() == Color.BLACK){
                            squares[i][j].setBackground(Color.GREEN);
                            s.setShipCoordinates(j, i);
                            shipsPlaced++;
                            if(shipsPlaced > 4){
                                try {
                                    out.writeObject(s);
                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                infoLabel1.setText("");
                                infoLabel2.setText("Incoming");
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
        if (squares[y][x].getBackground() == Color.GREEN){
            squares[y][x].setBackground(Color.RED);
            shipsHit++;
        } else if (squares[y][x].getBackground() == Color.BLACK) {
            squares[y][x].setText("X");
        }
        if (shipsHit >= s.getShipCoordinates().size()){
            infoLabel3.setText("Defeat");
        } else if (ammo < 1) {
            infoLabel3.setText("Victory");
        }
    }
    
    public void program() throws IOException, ClassNotFoundException, InterruptedException{
        setWindowBasics(5);
        if (playerNumber == 1) {
            while(true){
                player1();
            }
        }
        if (playerNumber == 2) {
            while(true){
                player2();
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException{
        try{
            ClientDeluxe c = new ClientDeluxe();
            c.program();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
