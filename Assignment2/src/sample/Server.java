package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String [] args) throws IOException{
        ServerSocket ss = new ServerSocket(8080);
        while(true){
            Socket s = ss.accept();
            System.out.println("client connected!");
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String str = br.readLine();
            System.out.println(str);
        }


    }
}
