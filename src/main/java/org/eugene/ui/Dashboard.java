package org.eugene.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.eugene.persistent.SqlliteWrapper;

import java.util.HashMap;
import java.util.Map;

public class Dashboard {
    private Stage stage;
    private VBox vBox;
    Accordion accordion;
    TitledPane aggregationPane = null;

    public Dashboard(Stage stage){
        this.stage = stage;
    }

    public void setVBox(VBox vBox){
        this.vBox = vBox;
    }

    public void refresh(String schema, String path, int rowNumber, int columnNumber, boolean init){
        vBox.getChildren().clear();
        accordion = new Accordion();
        refreshSummaryPane(path, rowNumber, columnNumber, accordion);
        refreshMetaPane(schema, accordion);
        refreshAggregationPane(accordion, "", null, init);
        vBox.getChildren().add(accordion);
    }

    private void refreshSummaryPane(String path, int rowNumber, int columnNumber, Accordion accordion){
        final int fontSize = 15;
        final int padding = 10;
        TitledPane summaryPane = new TitledPane();
        summaryPane.setText("Basic Information");

        Label nameKey = new Label("File name:   ");
        nameKey.setFont(Font.font("Arial", FontWeight.BLACK, fontSize));
        Label name = new Label(path);
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

    public void refreshAggregationPane(Accordion accordion, String columnName, Map<String, String> keyToValue, boolean init){
        VBox aggregationBox = null;
        if(aggregationPane == null || init == true){
            System.out.println("aggregation is null");
            aggregationPane = new TitledPane();
            aggregationPane.setText("Aggregation");
            aggregationPane.getContent();
            aggregationBox = new VBox();
            aggregationPane.setContent(aggregationBox);
            accordion.getPanes().add(aggregationPane);
            return;
        }
        if (keyToValue == null){
            System.out.println("keyToValue is null");
            Label label = new Label();
            label.setText("Click column to show its aggregations");
            aggregationBox = (VBox)aggregationPane.getContent();
            aggregationBox.getChildren().add(label);
            return;
        }
        aggregationBox = (VBox)aggregationPane.getContent();
        aggregationBox.getChildren().clear();
        ListView listView = null;

        listView = new ListView();
        listView.getItems().add(columnName + " : ");
        listView.getItems().add(SqlliteWrapper.MAX + " : " + keyToValue.get(SqlliteWrapper.MAX));
        listView.getItems().add(SqlliteWrapper.MIN + " : " + keyToValue.get(SqlliteWrapper.MIN));
        listView.getItems().add(SqlliteWrapper.SUM + " : " + keyToValue.get(SqlliteWrapper.SUM));
        listView.getItems().add(SqlliteWrapper.AVG + " : " + keyToValue.get(SqlliteWrapper.AVG));

        aggregationBox.getChildren().add(listView);
        aggregationPane.setExpanded(true);
    }

    public void refreshAggregationPane(String columnName, Map<String, String> keyToValue){
        refreshAggregationPane(accordion, columnName, keyToValue, false);
    }

}
