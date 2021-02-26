package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;

public class Controller {
    @FXML private PasswordField password;
    @FXML private TextField fullName;
    @FXML private TextField userName;

    @FXML private TextField eMail;
    @FXML private TextField phoneNum;
    @FXML private DatePicker DOB;
    @FXML private Button registerButton;




    public void buttonPress(ActionEvent e) {
        System.out.println("User Name: " + userName.getText() + "\n" + "Full Name: " + fullName.getText() + "\n"
                + "E-Mail: " + eMail.getText() + "\n" +"Phone Number: " + phoneNum.getText() + "\n"
                + "Date of Birth: " + DOB.getValue() + "\n" + "Password: " + password.getText());
    }
}
