package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.*;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    @FXML private ListView clientDir;
    public void initialize() throws IOException { // client
        ObservableList<String> data = FXCollections.observableArrayList("Hey","Test");
        clientDir.setItems(data);
    }

    public void upload(ActionEvent actionEvent) throws IOException{
        InetAddress ip =  InetAddress.getLocalHost();

        Scanner scan = new Scanner(System.in);
        Socket s = new Socket("localhost", 8080);

//        PrintWriter p = new PrintWriter(s.getOutputStream());
//        InputStreamReader in = new InputStreamReader(s.getInputStream());
//        BufferedReader br = new BufferedReader(in);

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

//            p.println("exit " + selectedFile.getName());
//            p.flush();
//            p.close();
//            String str;
//            while((str = br.readLine()) != null){
//                System.out.println(str);
//            }

        }
        out.close();
        System.out.println("Here");
    }

    public void download(ActionEvent actionEvent) {
    }
}
