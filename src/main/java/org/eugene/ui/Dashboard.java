package org.eugene.ui;

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
import org.apache.avro.Schema;
import org.eugene.util.TypeFetcher;

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

    public void refresh(Schema schema, File selectedFile, int rowNumber, int columnNumber){
        vBox.getChildren().clear();
        Map<String, String> schemaMap = new HashMap<>();
        for (Schema.Field field: schema.getFields())
        {
            String name = field.name();
            String type = TypeFetcher.getType(field.schema().toString());
            schemaMap.put(name,type);
        }
        Accordion accordion = new Accordion();
        TitledPane summaryPane = new TitledPane();
        summaryPane.setText("Basic Information");
        Label nameKey = new Label("File name:   ");
        nameKey.setFont(Font.font("Arial", FontWeight.BLACK, 15));
        Label name = new Label(selectedFile.getName());
        name.setFont(Font.font(15));
        HBox nameHBox = new HBox(nameKey, name);
        nameHBox.setPadding(new Insets(10,10,0,10));
        Label rowNumberKey = new Label("Row number:   ");
        rowNumberKey.setFont(Font.font("Arial", FontWeight.BLACK, 15));
        Label rowLabel = new Label(String.valueOf(rowNumber));
        rowLabel.setFont(Font.font(15));
        HBox rowNumberHBox = new HBox(rowNumberKey, rowLabel);
        rowNumberHBox.setPadding(new Insets(10,10,0,10));

        Label colNumberKey = new Label("Column number:   ");
        colNumberKey.setFont(Font.font("Arial", FontWeight.BLACK, 15));
        Label columnLabel = new Label(String.valueOf(columnNumber));
        columnLabel.setFont(Font.font(15));
        HBox colNumberHBox = new HBox(colNumberKey, columnLabel);
        colNumberHBox.setPadding(new Insets(10,10,0,10));

        summaryPane.setContent(new VBox(nameHBox, rowNumberHBox, colNumberHBox));
        TitledPane metaPane = new TitledPane();
        metaPane.setText("Schema Information");
        VBox metaBox = new VBox();
        schemaMap.forEach((k,v) -> {
            //metaBox.getChildren().add(new Label(k + " : " + v));
        });
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setText(schema.toString());
        System.out.println("name:" + schema.getName());
        System.out.println("namespace:" + schema.getNamespace());
        System.out.println("Type" + schema.getType());
        metaBox.getChildren().add(textArea);
        metaPane.setContent(metaBox);
        accordion.getPanes().add(summaryPane);
        accordion.getPanes().add(metaPane);
        vBox.getChildren().add(accordion);
    }
}
