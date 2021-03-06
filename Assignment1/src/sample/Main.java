package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller myCont = loader.getController(); // loads the current controller
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);
        myCont.setMainDirectory(mainDirectory);
        myCont.training();
        myCont.initTable();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();

//        try{
//            System.out.println("TEST");
//            System.out.println("Working Directory = " + System.getProperty("user.dir"));
//
//
//        }catch(Exception e){
//
//        }
    }


    public static void main(String[] args) {


        launch(args);

    }
}
