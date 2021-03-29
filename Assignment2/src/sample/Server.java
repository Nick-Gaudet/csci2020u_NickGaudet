package sample;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    public static void main(String [] args) throws IOException{
        Scanner scan = new Scanner(System.in);
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
            String str;
            while((str = br.readLine()) != null){
                System.out.println(str);
                PrintWriter pW = new PrintWriter(s.getOutputStream());
                pW.println("Server" + ": "+scan.nextLine());
                pW.flush();
            }


//            s.close();
        }


    }
}
