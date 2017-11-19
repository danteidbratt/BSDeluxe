
package battleshipsdeluxe;

import java.io.*;
import java.net.*;

public class ServerDeluxe {
    
    Socket cs1;
    Socket cs2;
    SessionDeluxe s;
    
    public ServerDeluxe(Socket cs1, Socket cs2) {
        this.cs1 = cs1;
        this.cs2 = cs2;
        s = new SessionDeluxe();
    }
    
    public void startServing() {
        
        try {
            ObjectOutputStream out1 = new ObjectOutputStream(cs1.getOutputStream());
            ObjectInputStream in1 = new ObjectInputStream(cs1.getInputStream());
            ObjectOutputStream out2 = new ObjectOutputStream(cs2.getOutputStream());
            ObjectInputStream in2 = new ObjectInputStream(cs2.getInputStream());
            
            while (true) {
                out1.writeObject(s);
                s = (SessionDeluxe) in1.readObject();
                out2.writeObject(s);
                s = (SessionDeluxe) in2.readObject();
            }
        } catch (IOException | ClassNotFoundException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}