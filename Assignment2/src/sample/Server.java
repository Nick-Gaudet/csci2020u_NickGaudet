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
        Socket s = ss.accept();
        PrintWriter pW = new PrintWriter(s.getOutputStream());
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(in);
//        System.out.println(ep);
        while(true){

            System.out.println("client connected!");

            String str;
            while((str = br.readLine()) != null){
                System.out.println(str);

            }
//            pW.println("Server" + ": "+scan.nextLine());
//            pW.flush();
//            pW.close();

//            s.close();
        }


    }
}
