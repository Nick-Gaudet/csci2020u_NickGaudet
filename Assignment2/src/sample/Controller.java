package sample;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    public void initialize() throws IOException { // client
        Scanner scan = new Scanner(System.in);
        InetAddress ip =  InetAddress.getLocalHost();
        Socket s = new Socket("10.0.0.104", 8080);

        PrintWriter p = new PrintWriter(s.getOutputStream());

        while(true){
            p.println(ip.getHostAddress() + ": "+scan.nextLine());
            p.flush();
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String str = br.readLine();
            System.out.println(str);

        }

//        System.out.println(scan.nextLine());

//        p.println("Hey this is client: " + ip.getHostAddress());




    }
}
