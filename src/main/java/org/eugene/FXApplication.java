package org.eugene;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eugene.ui.CustomizedMenuBar;

public class FXApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        MenuBar menuBar = new CustomizedMenuBar(stage);
        VBox vbox = new VBox(menuBar);
        double width = 800;
        double height = 600;
        Scene scene = new Scene(vbox, width, height);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icons8-owl-64.png")));
        stage.setTitle("Bigdata File Viewer");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
