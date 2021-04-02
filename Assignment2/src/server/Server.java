package server;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Server {


    public static void main(String [] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Server started...\nEnter hostname ('localhost' or ipv4 address): ");
        String hostName = "10.0.0.150";
        String serverIP = String.valueOf(InetAddress.getLocalHost().getHostAddress());

        System.out.println(InetAddress.getLocalHost());
        int port = 8080;
        ServerSocket ss = new ServerSocket();
        SocketAddress eP = new InetSocketAddress(hostName,port);
        ss.bind(eP);

        while(true){ // waits for client connections
            Socket s = null;
            try{
                s = ss.accept(); // accepts a client

                System.out.println("A new client has connected: " + s.getInetAddress());

                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                Thread t = new ClientConnectionHandler(s,in,out); // handle the client on a seperate thread

                t.start();
            }catch(Exception e){
//                s.close();
                e.printStackTrace();
            }
        }


    }
}

class ClientConnectionHandler extends Thread{
    final Socket s;
    final DataInputStream in;
    final DataOutputStream out;
    File dir = new File("./src/server/shared");
    public ClientConnectionHandler(Socket s, DataInputStream in, DataOutputStream out){
        this.s = s;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        while(true){ // while listenting for client commands ; breaks after each command has concluded

            try{
                String str = in.readUTF(); // store the commands recieved


                // server recieves upload from client
                if(str.split(" ")[0].equalsIgnoreCase("upload")){ // if upload, make a new file
                                                                                // copy contents from client to new file
                    File f = new File(dir, str.split(" ")[1]);
                    if(!f.exists()){
                        f.createNewFile();
                    }
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                    bw.write(in.readUTF());
                    bw.close();
                    this.s.close();
                    break;
                }

                //Server recieves download from client
                if(str.split(" ")[0].equalsIgnoreCase("download")){// if download, send file data
                    File f = new File(dir, str.split(" ")[1]);
                    if(!f.exists()){
                        f.createNewFile();
                    }
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

                // server recieves dir from client
                if(str.equalsIgnoreCase("dir")){
                    String toSend = "";
                    for(File f : dir.listFiles()){ // loop through server folder of files
                        toSend = toSend + f.getName() + " ";
                    }
                    out.writeUTF(toSend); // send it to client via string

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
