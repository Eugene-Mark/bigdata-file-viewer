package org.eugene.ui;

import javafx.scene.control.ProgressBar;
import javafx.stage.Popup;
import javafx.stage.Stage;

class ProgressWorker {
    private final Popup popup = new Popup();

    public void start(Stage stage){
        ProgressBar progressBar = new ProgressBar();
        popup.getContent().add(progressBar);
        popup.show(stage);
    }

    public void end(){
        popup.hide();
    }
}
