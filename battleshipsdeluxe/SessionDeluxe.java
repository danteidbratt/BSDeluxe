
package battleshipsdeluxe;

import java.io.*;

public class SessionDeluxe implements Serializable{
    
    private int playerNumber;
    
    public final int LOBBY = 0;
    public final int PLACESHIPS = 1;
    public final int FIRE = 2;
    public final int P1_WINS = 3;
    public final int P2_WINS = 4;
    
    private int state;
    
    // 0 = Unused, 1 = Using, 2 = Used
    public int nukeState;
    
    public int shipCounter;
    public int fieldSize;
    public int numberOfShips;
    public int ammo;
    private int[][] shipCoordinates;
    private int[] bombCoordinates = new int[2];
    
    public SessionDeluxe(){
        // Do not touch
        state = 0;
        playerNumber = 0;
        shipCounter = 0;
        nukeState = 0;
        
        // Settings
        fieldSize = 7;
        numberOfShips = 4;
        ammo = 10;
        
        shipCoordinates = new int[numberOfShips][2];
    }
    
    public int getPlayerNumber(){
        return ++playerNumber;
    }
    
    /**
     * 0 = Searching for opponent,
     * 1 = Place battleships,
     * 2 = Player 1 wins,
     * 3 = Player 2 wins,
     * @param state 
     */
    
    public void setState(int state){
        this.state = state;
    }
    
    public int getState(){
        return state;
    }
    
    public void setShipCoordinates(int x, int y) {
        shipCoordinates[shipCounter][0] = x;
        shipCoordinates[shipCounter][1] = y;
        shipCounter++;
    }
    
    public int[][] getShipCoordinates(){
        return shipCoordinates;
    }
    
    public void setBombCoordinates(int x, int y){
        bombCoordinates[0] = x;
        bombCoordinates[1] = y;
    }
    
    public int[] getBombCoordinates(){
        return bombCoordinates;
    }
}