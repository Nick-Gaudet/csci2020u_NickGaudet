package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {


    public TextField userNameText;
    public TextField msgText;

    public void sendClicked(ActionEvent actionEvent) throws IOException {
        String hostName = "localhost";
        int port = 8080;
        Socket s = new Socket(hostName,port);

        try{
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String toSend = userNameText.getText() + "//" + msgText.getText();
            out.writeUTF(toSend);
            s.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void exitClicked(ActionEvent actionEvent) {
    }
}
