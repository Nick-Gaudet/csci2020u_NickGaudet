package server;


import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server.fxml"));
        Parent root = loader.load();
        Controller c = (Controller) (loader.getController());
        primaryStage.setTitle("Lab10 Server");
        Scene scene = new Scene(root,500,300);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    c.runServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}


