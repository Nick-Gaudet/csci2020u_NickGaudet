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
        System.out.println("Chat: ");
//        p.println(scan.nextLine());
        System.out.println(scan.nextLine());

//        p.println("Hey this is client: " + ip.getHostAddress());

        p.flush();



    }
}
