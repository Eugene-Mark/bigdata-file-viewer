package org.eugene.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.eugene.config.Config;
import org.eugene.persistent.SqlliteWrapper;
import org.eugene.persistent.VirtualDB;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class Dashboard {
    private Stage stage;
    private VBox vBox;
    Accordion accordion;
    TitledPane aggregationPane = null;
    TitledPane proportionPane = null;

    @Value("${summary-pane-font-size}")
    int fontSize;
    @Value("${summary-pane-box-padding}")
    int padding;
    @Value("${proportion-max-item}")
    int maxShowing;

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
        if(Config.getInstance().enableAnalytics()){
            refreshAggregationPane(accordion, "", null, init);
            refreshProportionPane(accordion, "", null, init);
        }
        vBox.getChildren().add(accordion);
    }

    private void refreshSummaryPane(String path, int rowNumber, int columnNumber, Accordion accordion){
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

    private void refreshAggregationPane(Accordion accordion, String columnName, Map<String, String> keyToValue, boolean init){
        VBox aggregationBox = null;
        if(aggregationPane == null || init == true){
            aggregationPane = new TitledPane();
            aggregationPane.setText("Aggregation");
            aggregationBox = new VBox();
            aggregationPane.setContent(aggregationBox);
            accordion.getPanes().add(aggregationPane);
            return;
        }
        if (keyToValue == null){
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

    public void refreshProportionPane(String columnName, Map<String, Integer> itemToCount){
        refreshProportionPane(accordion, columnName, itemToCount, false);
    }

    public void refreshProportionPane(Accordion accordion, String columnName, Map<String, Integer> itemToCount, boolean init){
        VBox proportionBox = null;
        if(proportionPane == null || init == true){
            proportionPane = new TitledPane();
            proportionPane.setText("Proportion");
            proportionBox = new VBox();
            proportionPane.setContent(proportionBox);
            accordion.getPanes().add(proportionPane);
        }

        if(itemToCount == null){
            Label label = new Label();
            label.setText("Click column to show its proportions");
            proportionBox = (VBox)proportionPane.getContent();
            proportionBox.getChildren().add(label);
            return;
        }
        proportionBox = (VBox)proportionPane.getContent();
        proportionBox.getChildren().clear();
        PieChart pieChart = new PieChart();

        int maxShowing = 5;
        int index = 0;
        int sum = 0;
        for(Map.Entry<String, Integer> entry: itemToCount.entrySet()){
            String key = entry.getKey();
            int count = itemToCount.get(key);
            sum += count;
            PieChart.Data slice = new PieChart.Data(key, itemToCount.get(key));
            pieChart.getData().add(slice);
            index++;
            if(index == maxShowing){
                break;
            }
        }

        int othersCount = VirtualDB.getInstance().getTableMeta().getRow() - sum;
        if (othersCount > 0){
            PieChart.Data slice = new PieChart.Data("Others", othersCount);
            pieChart.getData().add(slice);
        }

        proportionBox.getChildren().add(pieChart);
        proportionPane.setExpanded(true);
    }

}
