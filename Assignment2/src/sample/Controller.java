package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    public Button downButton;

    // UI objects
    @FXML private ListView clientDir;
    @FXML private ListView serverDir;

    // List Contents to be viewed in UI
    ObservableList<String> serverData = FXCollections.observableArrayList();
    ObservableList<String> clientData = FXCollections.observableArrayList();

    //fields
    private String toBeUploaded;
    private String toBeDownloaded;
    private File dir = new File(".\\src\\sample\\shared");
    private File [] filesInFolder = dir.listFiles();
    private String hostName = "10.0.0.104";
    private int port = 8080;

    public void setHostName(String s){
        this.hostName = s;
    }
    public File [] getFilesInFolder(){
        return this.filesInFolder;
    }
    public void setClientData(){ // sets client listview data on launch
        for(File f : getFilesInFolder()){
            clientData.add(f.getName());
        }
        clientDir.setItems(clientData);
    }
    public boolean isConnectionValid(String hostName) throws IOException {
        if( new Socket(hostName,port).isConnected()){
            return true;
        }
        return false;
    }
    public void initialize() throws IOException { // client

        setClientData();
        dir();
        serverDir.setItems(serverData);


    }
    public void dir() throws IOException{ // sends list of server folder contents to client
        Socket s = new Socket(hostName, port);
        try{

            //streams
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            while(true){ // send command to server
                out.writeUTF("dir");
                break;
            }
            System.out.println("Awaiting server response...");
            while(true){ // read in files of server folder

                serverData.addAll(in.readUTF().split(" "));
                s.close();
                in.close();
                break;

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void upload(ActionEvent actionEvent) throws IOException{ // user wants to upload file
        Socket s = new Socket(hostName, port);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        if(clientDir.getSelectionModel().getSelectedItem() != null){ // if the user has selected and item to upload
            this.toBeUploaded = clientDir.getSelectionModel().getSelectedItem().toString(); // file (client -> server folder)
            out.writeUTF("upload " + this.toBeUploaded + "\n"); // send server command

            while(true){ // wait for response
                try{

                    //read in the file to be sent to server
                    BufferedReader br = new BufferedReader(new FileReader(new File(this.dir,this.toBeUploaded)));
                    String toSend = "";
                    String line;
                    while((line = br.readLine()) != null){ // read in file and store contents into string
                        toSend += line + "\n";
                    }
                    out.writeUTF(toSend); // send the file data to server
                    s.close();
                    break;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            // add to and update listview for server
            serverData.add(this.toBeUploaded);
            serverDir.refresh();
        }



    }

    public void download(ActionEvent actionEvent) throws IOException{ // user wants to download
        if(serverDir.getSelectionModel().getSelectedItem() != null){ // if right list element is selected

            //Store file name and open streams & sockets
            this.toBeDownloaded = serverDir.getSelectionModel().getSelectedItem().toString();
            Socket s = new Socket(hostName, port);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF("download " + this.toBeDownloaded); // send server command

            while(true){ // while listening to server
                try{
                    String inStr = in.readUTF();

                    System.out.println(inStr); // prints content of file requested to client terminal
                    File f = new File(dir, this.toBeDownloaded); // create the file in the client dir
                    if(!f.exists()){
                        f.createNewFile();
                    }
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                    bw.write(inStr); // write the contents passed by server to the new file
                    bw.close();
                    s.close();
                    break;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            // add to and update listview for client
            clientData.add(this.toBeDownloaded);
            clientDir.refresh();
        }

    }

    public void delete(ActionEvent actionEvent) { // delete button clicked

        if(clientDir.getSelectionModel().getSelectedItem() != null){ // if user wants to delete file locally
            String fileName  = clientDir.getSelectionModel().getSelectedItem().toString();
            File f = new File(dir, fileName);
            f.delete();
            clientData.remove(fileName);
            clientDir.refresh();
        }
    }

    public void exit(ActionEvent actionEvent)  throws IOException{
        System.out.println("Exiting Client...");
        System.exit(-1);
    }
}
