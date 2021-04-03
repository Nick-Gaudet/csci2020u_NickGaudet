package server;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Server {


    public static void main(String [] args) throws IOException{
        System.out.println("Server started...");
        String hostName = "localhost"; // set as default local host -> change to IPV4

        System.out.println(InetAddress.getLocalHost());
        int port = 8081;
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
                s.close();
                e.printStackTrace();
            }
        }


    }
}

class ClientConnectionHandler extends Thread{ // handles incoming clients creates a new thread each connection
    final Socket s;
    final DataInputStream in;
    final DataOutputStream out;
    File dir = new File("./src/server/shared");
    private String splitter = "<>";
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
                if(str.split(splitter)[0].equalsIgnoreCase("upload")){ // if upload, make a new file
                                                                                // copy contents from client to new file

                    new Thread (() ->{ // makes a new thread so file get created and wrote to as server is still running
                                        // on main thread
                        try{
                            File f = new File(dir, str.split(splitter)[1]);
                            if(!f.exists()){
                                f.createNewFile();
                            }
                            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                            bw.write(str.split(splitter)[2]);
                            bw.flush();
                            bw.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }).start();

                    this.s.close();
                    break;
                }

                //Server recieves download from client
                if(str.split(splitter)[0].equalsIgnoreCase("download")){// if download, send file data
                    File f = new File(dir, str.split(splitter)[1]);
                    if(!f.exists()){ // pretty sure redundant but JUST incase
                        f.createNewFile();
                    }
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());

                    String line;
                    String toOutput = "";
                    while ((line = br.readLine()) != null){ // read file, store in string to send to client
                        toOutput += line + "\n";
                    }
                    out.writeUTF(toOutput); // send file contents to client
                    out.flush();

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
            // close streams on client connection closed
            this.in.close();
            this.out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
