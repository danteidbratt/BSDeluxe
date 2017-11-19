package battleshipsdeluxe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MatcherDeluxe {
    
    public void program() throws IOException{
        ServerSocket serverSocket = new ServerSocket(12321);
        while(true){
            Socket cs1 = serverSocket.accept();
            Socket cs2 = serverSocket.accept();
            ServerDeluxe sd = new ServerDeluxe(cs1, cs2);
            sd.startServing();
        }
    }
    
    public static void main(String[] args){
        MatcherDeluxe md = new MatcherDeluxe();
        try {
            md.program();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
