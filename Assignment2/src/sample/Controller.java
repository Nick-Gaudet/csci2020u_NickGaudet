package sample;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.*;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    public Button downButton;
    @FXML private ListView clientDir;
    @FXML private ListView serverDir;
    ObservableList<String> serverData = FXCollections.observableArrayList();
    ObservableList<String> clientData = FXCollections.observableArrayList();
    private String toBeUploaded;
    private String toBeDownloaded;

    private String hostName = "10.0.0.104";
    private int port = 8080;

    public void initialize() throws IOException { // client
        File [] files = new File[4];
        File dir = new File("./src/sample/shared");
        dir.mkdirs();
        files[0] = new File(dir,"1.txt");
        files[1] = new File(dir,"2.txt");
        files[2] = new File(dir,"3.txt");
        files[3] = new File(dir,"4.txt");

        for(File f : files){
            f.createNewFile();
        }
        for(File f : files){
            clientData.add(f.getName());
        }
        clientDir.setItems(clientData);
        dir();
        serverDir.setItems(serverData);


    }
    public void dir() throws IOException{ // sends list of server folder contents to client
        Socket s = new Socket(hostName, port);
        try{
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            while(true){
                out.writeUTF("dir");
                break;
            }
            System.out.println("Awaiting server response...");
            while(true){
                serverData.addAll(in.readUTF().split(" "));
                s.close();
                break;

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void upload(ActionEvent actionEvent) throws IOException{


        InetAddress ip =  InetAddress.getLocalHost();

        Scanner scan = new Scanner(System.in);
        Socket s = new Socket(hostName, port);

        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        boolean connected = true;
        while(true){
            Stage stage = (Stage) clientDir.getScene().getWindow();
            FileChooser f = new FileChooser();
            File selectedFile = f.showOpenDialog(stage);
            out.writeUTF(selectedFile.getName());
            s.close();
            break;

        }
        out.close();
        System.out.println("Here");
    }

    public void download(ActionEvent actionEvent) throws IOException{

        this.toBeDownloaded = serverDir.getSelectionModel().getSelectedItem().toString();
        Socket s = new Socket(hostName, port);
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        out.writeUTF("download " + this.toBeDownloaded);

        while(true){
            try{
                System.out.println(in.readUTF());
                s.close();
                break;
            }catch(Exception e){
                e.printStackTrace();
            }
        }




    }


}
