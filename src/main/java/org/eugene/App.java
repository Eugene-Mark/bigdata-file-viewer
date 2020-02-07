package org.eugene;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eugene.ui.CustomizedMenuBar;


/**
 * Hello world!
 *
 */
public class App extends Application
{
    @Override
    public void start(Stage stage){
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        MenuBar menuBar = new CustomizedMenuBar(stage);
        VBox vbox = new VBox(menuBar);
        double width = 800;
        double height = 600;
        Scene scene = new Scene(vbox, width, height);
        stage.setTitle("Bigdata File Viewer");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
