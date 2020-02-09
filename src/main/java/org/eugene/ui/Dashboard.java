package org.eugene.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Dashboard {
    private Stage stage;
    private VBox vBox;

    public Dashboard(Stage stage){
        this.stage = stage;
    }

    public void setVBox(VBox vBox){
        this.vBox = vBox;
    }

    public void refresh(String schema, File selectedFile, int rowNumber, int columnNumber){
        vBox.getChildren().clear();
        Accordion accordion = new Accordion();
        refreshSummaryPane(selectedFile, rowNumber, columnNumber, accordion);
        refreshMetaPane(schema, accordion);
        vBox.getChildren().add(accordion);
    }

    private void refreshSummaryPane(File selectedFile, int rowNumber, int columnNumber, Accordion accordion){
        final int fontSize = 15;
        final int padding = 10;
        TitledPane summaryPane = new TitledPane();
        summaryPane.setText("Basic Information");

        Label nameKey = new Label("File name:   ");
        nameKey.setFont(Font.font("Arial", FontWeight.BLACK, fontSize));
        Label name = new Label(selectedFile.getName());
        name.setFont(Font.font(fontSize));
        HBox nameHBox = new HBox(nameKey, name);
        nameHBox.setPadding(new Insets(padding,padding,0,padding));

        Label rowNumberKey = new Label("Row number:   ");
        rowNumberKey.setFont(Font.font("Arial", FontWeight.BLACK, fontSize));
        Label rowLabel = new Label(String.valueOf(rowNumber));
        rowLabel.setFont(Font.font(fontSize));
        HBox rowNumberHBox = new HBox(rowNumberKey, rowLabel);
        rowNumberHBox.setPadding(new Insets(padding,padding,0,padding));

        Label colNumberKey = new Label("Column number:   ");
        colNumberKey.setFont(Font.font("Arial", FontWeight.BLACK, fontSize));
        Label columnLabel = new Label(String.valueOf(columnNumber));
        columnLabel.setFont(Font.font(fontSize));
        HBox colNumberHBox = new HBox(colNumberKey, columnLabel);
        colNumberHBox.setPadding(new Insets(padding,padding,0,padding));

        summaryPane.setContent(new VBox(nameHBox, rowNumberHBox, colNumberHBox));
        accordion.getPanes().add(summaryPane);
    }

    private void refreshMetaPane(String schema, Accordion accordion){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(schema);
        String prettySchemaJson = gson.toJson(je);
        Map<String, String> schemaMap = new HashMap<>();
        TitledPane metaPane = new TitledPane();
        metaPane.setText("Schema Information");
        VBox metaBox = new VBox();

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setText(prettySchemaJson);

        metaBox.getChildren().add(textArea);
        metaPane.setContent(metaBox);
        accordion.getPanes().add(metaPane);
    }
}
