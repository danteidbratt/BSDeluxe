
package battleshipsdeluxe;

import java.io.*;
import java.net.Socket;

public class ClientDeluxe{
    
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket bridge;
    
    SessionDeluxe s;
    int playerNumber;
    
    public ClientDeluxe() throws IOException, ClassNotFoundException{
        bridge = new Socket("127.0.0.1", 12321);
        in = new ObjectInputStream(bridge.getInputStream());
        out = new ObjectOutputStream(bridge.getOutputStream());
        s = (SessionDeluxe)in.readObject();
        playerNumber = s.getPlayerNumber();
    }
    
    public void orderSelector() throws IOException, ClassNotFoundException, InterruptedException{
        if (playerNumber == 1) {
            Player1 p1 = new Player1(playerNumber, s, in, out);
            while(true){
                p1.fuckingGoTime();
            }
        }
        if (playerNumber == 2) {
            Player2 p2 = new Player2(playerNumber, s, in, out);
            while(true){
                p2.fuckingGoTime();
            }
        }
    }
    
    public static void main(String[] args){
        try{
            ClientDeluxe c = new ClientDeluxe();
            c.orderSelector();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
