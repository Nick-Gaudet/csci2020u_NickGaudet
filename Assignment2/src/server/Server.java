package server;

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

        while(true){
            Socket s = null;
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
            File dir = new File("./src/server/shared");

            try{
                String str = in.readUTF();
                if(str.split(" ")[0].equalsIgnoreCase("download")){
                    File f = new File(dir, str.split(" ")[1]);
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());

                    String line;
                    String toOutput = "";
                    while ((line = br.readLine()) != null){
                        toOutput += line + "\n";
                    }
                    out.writeUTF(toOutput);

                    this.s.close();
                    break;
                }
                if(str.equalsIgnoreCase("dir")){
                    String toSend = "";
                    for(File f : dir.listFiles()){
                        toSend = toSend + f.getName() + " ";
                    }
                    out.writeUTF(toSend);

                    this.s.close();
                    break;
                }

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
