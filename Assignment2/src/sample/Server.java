package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Server {
    public static void main(String [] args) throws IOException{
        Inet4Address ip4 = (Inet4Address) Inet4Address.getLocalHost();
        InetAddress iAdd = InetAddress.getLocalHost();
        int port = 8080;
        ServerSocket ss = new ServerSocket();
        SocketAddress eP = new InetSocketAddress("10.0.0.104",port);
        ss.bind(eP);
//        System.out.println(ep);
        while(true){
            Socket s = ss.accept();
            System.out.println("client connected!");
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader br = new BufferedReader(in);
            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in));
            String str;
            while((str = stdIn.readLine()) != null){
                System.out.println(str);

            }
//            s.close();
        }


    }
}
