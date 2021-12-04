package org.eugene.ui;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;

public class Main {
    private Stage stage;
    private Table table;
    private Dashboard dashboard;

    @Value("${split-pane-ratio}")
    private int splitPaneRatio;

    public Main(Stage stage, Table table, Dashboard dashboard){
        this.stage = stage;
        this.table = table;
        this.dashboard = dashboard;
    }

    public void initUI(){
        VBox mainVBox = (VBox)stage.getScene().getRoot();
        SplitPane splitPane = new SplitPane();
        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        table.setVBox(rightVBox);
        dashboard.setVBox(leftVBox);
        splitPane.getItems().addAll(leftVBox, rightVBox);
        splitPane.setDividerPosition(0, splitPaneRatio);
        mainVBox.getChildren().add(splitPane);
    }

    public Table getTable(){
        return table;
    }

    public Dashboard getDashboard(){
        return dashboard;
    }
}
