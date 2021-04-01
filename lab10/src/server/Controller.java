package server;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class Controller {
    public TextArea chatBox;

    public void runServer() throws IOException{
        String hostName = "10.0.0.104";
        int port = 8080;
        ServerSocket ss = new ServerSocket();
        SocketAddress sA = new InetSocketAddress(hostName, port);
        System.out.println(ss.isBound());
        ss.bind(sA);
        System.out.println(ss.isBound());

        while (true) {
            Socket s = null;
            try {
                s = ss.accept();
                System.out.println("Client : " + s.getInetAddress());

                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                ClientConnectionHandler cHand = new ClientConnectionHandler(s, in, out);
                cHand.setChatBox(chatBox);
                Thread t = cHand;
                t.start();
                chatBox = cHand.getChatBox();


                break;

            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
    public void initialize() throws IOException {

    }

    public void exitClicked(ActionEvent actionEvent) {
    }
}
class ClientConnectionHandler extends Thread{
    final Socket s;
    final DataInputStream in;
    final DataOutputStream out;
    public TextArea chatBox;
    public ClientConnectionHandler(Socket s, DataInputStream in, DataOutputStream out){
        this.s = s;
        this.in = in;
        this.out = out;
    }
    public void setChatBox(TextArea chatBox){
        this.chatBox = chatBox;
    }
    public TextArea getChatBox(){
        return chatBox;
    }

    @Override
    public void run() {
        try{
            while(true){
                String[] str = in.readUTF().split("//");

                this.chatBox.appendText(str[0] + " : " + str[1]);
                break;


            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
