package org.eugene.ui;

import javafx.scene.control.ProgressBar;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ProgressWorker {
    private Popup popup;

    public void start(Stage stage){
        popup = new Popup();
        ProgressBar progressBar = new ProgressBar();
        popup.getContent().add(progressBar);
        popup.show(stage);
    }

    public void end(){
        popup.hide();
    }
}
