package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {
    public void initialize() throws IOException { // client
        Socket s = new Socket("localhost", 4999);

        PrintWriter p = new PrintWriter(s.getOutputStream());
        p.println("Hey its the client@@@");
        p.flush();

    }
}
