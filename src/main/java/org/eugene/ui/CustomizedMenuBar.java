package org.eugene.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.fs.Path;
import org.eugene.controller.Renderer;
import org.eugene.util.CSVWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomizedMenuBar extends MenuBar {
    private final MenuItem subCSV;
    private final TextField textField;
    private final Button goButton;
    private final MenuItem selectPropertiesMenuItem;
    private boolean firstTime = true;

    public CustomizedMenuBar(Stage stage){
        //File menu
        Menu file = new Menu();
        file.setText("File");
        MenuItem open = new MenuItem("Open");
        Renderer renderer = new Renderer(stage);
        open.setOnAction(event -> {
            if (firstTime){
                renderer.initUI();
                firstTime = false;
            }
            boolean status = renderer.loadAndShow();
            if (status){
                enableAll();
            }
        });
        Menu saveas= new Menu("Save as...");
        subCSV = new MenuItem("CSV");
        saveas.getItems().add(subCSV);
        saveas.setOnAction( event -> {
            FileChooser fileChooser = new FileChooser();
            File csvFile = fileChooser.showSaveDialog(stage);
            Path path = new Path(csvFile.getAbsolutePath());
            ArrayList<GenericData.Record> list = (ArrayList<GenericData.Record>) renderer.getData();
            CSVWriter.write(new Path(csvFile.getAbsolutePath()), list);
        });
        MenuItem close = new MenuItem("Close");
        close.setOnAction(event -> {
            stage.close();
        });
        file.getItems().add(open);
        file.getItems().add(saveas);
        file.getItems().add(close);

        //View menu
        Menu view = new Menu();
        view.setText("View");
        CustomMenuItem pageRowNumItem = new CustomMenuItem();
        Label label = new Label("  Maximum Row Number per Page");
        label.setTextFill(Color.gray(0.8));
        label.setPadding(new Insets(5,0,5,0));
        textField = new TextField();
        textField.setText(String.valueOf(Constants.MAX_ROW_NUM));
        goButton = new Button("Go");
        goButton.setOnAction(event -> {
            try {
                int pageRowNum = Integer.parseInt(textField.getText());
                if (pageRowNum <= 0){
                    Notifier.error("Positive Integer required");
                    textField.clear();
                    return;
                }
                Constants.MAX_ROW_NUM = pageRowNum;
                renderer.refreshTable();
            }catch(Exception e){
                Notifier.error("Positive Integer required");
                textField.clear();
                return;
            }
        });
        HBox hbox = new HBox(textField, goButton);
        VBox vbox = new VBox(label, hbox);
        pageRowNumItem.setContent(vbox);
        view.getItems().add(pageRowNumItem);

        selectPropertiesMenuItem = new MenuItem();
        selectPropertiesMenuItem.setText("Add/Remove Properties");
        SelectPropertyDialog selectPropertyDialog = new SelectPropertyDialog();
        selectPropertiesMenuItem.setOnAction(event -> {
            selectPropertyDialog.init(stage, renderer);
            Optional<List<String>> result = selectPropertyDialog.getDialog().showAndWait();
            result.ifPresent(renderer::refreshTable);
        });
        view.getItems().add(selectPropertiesMenuItem);

        this.getMenus().add(file);
        this.getMenus().add(view);
        disableAll();
    }

    private void disableAll(){
        subCSV.setDisable(true);
        textField.setDisable(true);
        goButton.setDisable(true);
        selectPropertiesMenuItem.setDisable(true);
    }

    private void enableAll(){
        subCSV.setDisable(false);
        textField.setDisable(false);
        goButton.setDisable(false);
        selectPropertiesMenuItem.setDisable(false);
    }
}
