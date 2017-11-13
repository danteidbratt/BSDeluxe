
package battleshipsdeluxe;

import java.io.*;
import java.net.*;

public class ServerDeluxe {
    
    public ServerDeluxe() throws IOException, ClassNotFoundException {
        SessionDeluxe s = new SessionDeluxe();
        ServerSocket serverSocket = new ServerSocket(12321);
        
        Socket clientSocket1 = serverSocket.accept();
        ObjectOutputStream out1 = new ObjectOutputStream(clientSocket1.getOutputStream());
        ObjectInputStream in1 = new ObjectInputStream(clientSocket1.getInputStream());
        
        
        Socket clientSocket2 = serverSocket.accept();
        ObjectOutputStream out2 = new ObjectOutputStream(clientSocket2.getOutputStream());
        ObjectInputStream in2 = new ObjectInputStream(clientSocket2.getInputStream());
        
        while(true){
            out1.writeObject(s);
            s = (SessionDeluxe)in1.readObject();
            out2.writeObject(s);
            s = (SessionDeluxe)in2.readObject();
        }
    }
    
    public static void main(String[] args){
        try{
            ServerDeluxe s = new ServerDeluxe();
        } catch (IOException e) {
            System.out.println("Server:\n" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
