package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Server {
    public static void main(String [] args) throws IOException{
        InetAddress iAdd = InetAddress.getLocalHost();
        int port = 8080;
        ServerSocket ss = new ServerSocket();
        SocketAddress eP = new InetSocketAddress(iAdd,port);
        ss.bind(eP);
        System.out.println(iAdd);
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
