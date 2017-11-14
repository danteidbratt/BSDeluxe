
package battleshipsdeluxe;

import java.io.*;
import java.util.*;

public class SessionDeluxe implements Serializable{
    
    private int playerNumber;
    
    public final int LOBBY = 0;
    public final int PLACESHIPS = 1;
    public final int FIRE = 2;
    public final int P1_WINS = 3;
    public final int P2_WINS = 4;
    
    private int state;
    private List<Integer[]> shipCoordinates = new ArrayList<>();
    private int[] bombCoordinates = new int[2];
    
    public SessionDeluxe(){
        playerNumber = 1;
    }
    
    public int getPlayerNumber(){
        return playerNumber++;
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
    
    public void setShipCoordinates(int x, int y){
        Integer[] temp = {x,y};
        shipCoordinates.add(temp);
    }
    
    public List<Integer[]> getShipCoordinates(){
        return shipCoordinates;
    }
    
    public void setBombCoordinates(int x, int y){
        bombCoordinates[0] = x;
        bombCoordinates[1] = y;
    }
    
    public int[] getBombCoordinates(){
        return bombCoordinates;
    }
    
    public void resetAll(){
        shipCoordinates = new ArrayList<>();
        state = 1;
    }
    
}