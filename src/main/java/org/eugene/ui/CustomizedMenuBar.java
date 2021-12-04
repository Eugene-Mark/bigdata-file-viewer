package org.eugene.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.hadoop.fs.Path;
import org.eugene.controller.Renderer;
import org.eugene.util.CSVWriter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomizedMenuBar extends MenuBar {
    private final MenuItem subCSV;
    private final TextField textField;
    private final Button goButton;
    private final MenuItem selectPropertiesMenuItem;
    private boolean firstTime = true;
    private final TextField csvDelimiterField = new TextField();
    private final Button setDelimiterButton;

    @Value("${menubar-label-gray-level}")
    double grayLevel;

    public CustomizedMenuBar(Stage stage){
        //File menu
        Menu file = new Menu();
        file.setText("File");
        Menu open = new Menu("Open");
        MenuItem localFileSystemItem = new MenuItem("Local File System");
        Renderer renderer = new Renderer(stage);
        localFileSystemItem.setOnAction(event -> {
            if (firstTime){
                renderer.initUI();
                firstTime = false;
            }
            boolean status = renderer.loadAndShow();
            if (status){
                enableAll();
            }
        });

        MenuItem HDFSItem = new MenuItem("HDFS");
        SetHDFSDialog setHDFSDialog = new SetHDFSDialog();
        HDFSItem.setOnAction(event -> {
            boolean status = false;
            if (firstTime){
                renderer.initUI();
                firstTime = false;
            }
            setHDFSDialog.init(stage, renderer);
            Optional<String> result = setHDFSDialog.getDialog().showAndWait();
            if(result.isPresent()){
                Path path = new Path(result.get());
                status = renderer.loadAndShow(path);
            }
            if (status){
                enableAll();
            }
        });

        MenuItem AWSItem = new MenuItem("AWS");
        SetAWSDialog setAWSDialog = new SetAWSDialog();
        AWSItem.setOnAction(event -> {
            boolean status = false;
            if (firstTime){
                renderer.initUI();
                firstTime = false;
            }
            setAWSDialog.init(stage, renderer);
            Optional<Map<String, String>> result = setAWSDialog.getDialog().showAndWait();
            if(result.isPresent()){
                status = renderer.loadAndShow(result.get());
            }
            if (status){
                enableAll();
            }
        });

        open.getItems().add(localFileSystemItem);
        open.getItems().add(HDFSItem);
        open.getItems().add(AWSItem);

        Menu saveas= new Menu("Save as...");
        subCSV = new MenuItem("CSV");
        saveas.getItems().add(subCSV);
        saveas.setOnAction( event -> {
            FileChooser fileChooser = new FileChooser();
            File csvFile = fileChooser.showSaveDialog(stage);
            String absolutePath = csvFile.getAbsolutePath();
            if (!absolutePath.endsWith(".csv"))
            {
                absolutePath = absolutePath.concat(".csv");
            }
            ArrayList<List<String>> list = (ArrayList<List<String>>) renderer.getData();
            String delimiter = csvDelimiterField.getText();
            CSVWriter.write(new Path(absolutePath), list, delimiter);
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
        label.setTextFill(Color.gray(grayLevel));
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

        //Settings menu
        Menu settings = new Menu();
        settings.setText("Settings");
        CustomMenuItem csvDelimiterItem = new CustomMenuItem();
        Label delimiterLabel = new Label("  Output CSV's delimiter");
        delimiterLabel.setTextFill(Color.gray(grayLevel));
        delimiterLabel.setPadding(new Insets(5,0,5,0));
        csvDelimiterField.setText(",");
        setDelimiterButton = new Button("Set");
        setDelimiterButton.setOnAction(event -> {
            try {
                String delimiter = csvDelimiterField.getText();
                if (delimiter.isEmpty()){
                    Notifier.error("No delimiter provided, comma is used by default");
                    csvDelimiterField.setText(",");
                    return;
                }
                renderer.refreshTable();
            }catch(Exception e){
                Notifier.error("String required");
                csvDelimiterField.setText(",");
                return;
            }
        });
        HBox settingsHBox = new HBox(csvDelimiterField, setDelimiterButton);
        VBox settingsVBox = new VBox(delimiterLabel, settingsHBox);
        csvDelimiterItem.setContent(settingsVBox);
        settings.getItems().add(csvDelimiterItem);
        this.getMenus().add(settings);

        //About menu
        Menu about = new Menu();
        about.setText("About");
        MenuItem aboutItem = new MenuItem();
        aboutItem.setText("About");
        aboutItem.setOnAction(event -> {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.init();
            aboutDialog.getDialog().show();
        });
        about.getItems().add(aboutItem);
        this.getMenus().add(about);

        disableAll();
    }

    private void disableAll(){
        subCSV.setDisable(true);
        textField.setDisable(true);
        goButton.setDisable(true);
        csvDelimiterField.setDisable(true);
        setDelimiterButton.setDisable(true);
        selectPropertiesMenuItem.setDisable(true);
    }

    private void enableAll(){
        subCSV.setDisable(false);
        textField.setDisable(false);
        goButton.setDisable(false);
        csvDelimiterField.setDisable(false);
        setDelimiterButton.setDisable(false);
        selectPropertiesMenuItem.setDisable(false);
    }
}
