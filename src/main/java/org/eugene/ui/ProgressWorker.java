package org.eugene.ui;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProgressWorker {
    Popup popup = new Popup();

    public void start(Stage stage){
        ProgressBar progressBar = new ProgressBar();
        popup.getContent().add(progressBar);
        popup.show(stage);
    }

    public void end(){
        popup.hide();
    }
}
