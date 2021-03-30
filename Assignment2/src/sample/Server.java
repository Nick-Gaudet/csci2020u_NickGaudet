package sample;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    private String hostName;
    private int port;

    public static void main(String [] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        Inet4Address ip4 = (Inet4Address) Inet4Address.getLocalHost();
        InetAddress iAdd = InetAddress.getLocalHost();
        int port = 8080;
        ServerSocket ss = new ServerSocket();
        SocketAddress eP = new InetSocketAddress("localhost",port);
        ss.bind(eP);
//        PrintWriter pW = new PrintWriter(s.getOutputStream());
//        InputStreamReader in = new InputStreamReader(s.getInputStream());
//        BufferedReader br = new BufferedReader(in);
//        System.out.println(ep);
        while(true){
            Socket s = null;

//            System.out.println(s.isConnected());
            try{
                s = ss.accept();

                System.out.println("A new client has connected: " + s.getInetAddress());

                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                Thread t = new ClientConnectionHandler(s,in,out);

                t.start();
            }catch(Exception e){
                s.close();
                e.printStackTrace();
            }
//            String str;
//            while((str = br.readLine()) != null){
//                System.out.println(str);
//
//            }
//
//            s.close();
//            s.close();
//            pW.println("Server" + ": "+scan.nextLine());
//            pW.flush();
//            pW.close();

//            s.close();
        }


    }
}
class ClientConnectionHandler extends Thread{
    final Socket s;
    final DataInputStream in;
    final DataOutputStream out;
    public ClientConnectionHandler(Socket s, DataInputStream in, DataOutputStream out){
        this.s = s;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        while(true){
            try{
                String str = in.readUTF();

                if(Arrays.asList(str.split(" ")).contains("exit") || str == null){
                    System.out.println("Client Connection closing...");
                    this.s.close();
                    break;
                }
                System.out.println(str);

                this.s.close();
                break;

            }catch(IOException e){
                e.printStackTrace();
            }
        }
        try{
            this.in.close();
            this.out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
