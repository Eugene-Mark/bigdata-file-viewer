package org.eugene.ui;

import javafx.scene.control.Alert;

public class Notifier {
    public static void warn(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void error(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setContentText(message);
        alert.show();
    }

    public static void info(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
