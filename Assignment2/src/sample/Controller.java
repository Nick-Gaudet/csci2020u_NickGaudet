package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

public class Controller {
    public void initialize() throws IOException { // client
        InetAddress ip =  InetAddress.getLocalHost();
        Socket s = new Socket(ip, 8080);

        PrintWriter p = new PrintWriter(s.getOutputStream());
        p.println("Hey its the client@@@");
        p.flush();

    }
}
