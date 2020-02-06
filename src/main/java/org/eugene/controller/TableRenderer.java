package org.eugene.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.fs.Path;
import org.eugene.core.parquet.ParquetReader;
import org.eugene.ui.Constants;
import org.eugene.ui.Notifier;
import org.eugene.util.TypeFetcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableRenderer {

    private List<GenericData.Record> list;
    private final Stage stage;
    private TableView<List<StringProperty>> tableView;
    private int rowNum = 0;
    private Pagination pagination;
    private final ParquetReader reader = new ParquetReader();
    private Schema schema;
    private List<String> showingList;
    private List<String> propertyList;
    private VBox leftVBox = new VBox();
    private VBox rightVBox = new VBox();
    private File selectedFile;

    public TableRenderer(Stage stage){
        this.stage = stage;

    }

    private boolean prepareData(){
        FileChooser filechooser = new FileChooser();
        selectedFile = filechooser.showOpenDialog(stage);
        Path path = new Path(selectedFile.getAbsolutePath());
        list = reader.read(path);
        if(list == null)
        {
            return false;
        }
        if (list.isEmpty()) {
            Notifier.info("The file is empty");
        }
        rowNum = list.size();
        GenericData.Record record = list.get(0);
        schema = record.getSchema();
        System.out.println(schema);
        return true;
    }

    public void initUIOnce(){
        VBox mainVBox = (VBox)stage.getScene().getRoot();
        SplitPane splitPane = new SplitPane();
        leftVBox = new VBox();
        rightVBox = new VBox();
        splitPane.getItems().addAll(leftVBox, rightVBox);
        splitPane.setDividerPosition(0, 0.4);
        mainVBox.getChildren().add(splitPane);
    }

    public boolean loadAndShow(){
        boolean status = prepareData();
        if (status) {

            initTable();
            showingList = new ArrayList<>();
            propertyList = new ArrayList<>();
            for (Schema.Field field: schema.getFields())
            {
                String property = field.name();
                showingList.add(property);
                propertyList.add(property);
            }
            refreshMetaInfo();
            refreshTable(showingList);
        }
        return status;
    }

    private void initTable(){
        if (tableView != null)
            rightVBox.getChildren().remove(tableView);
        if (pagination != null)
            rightVBox.getChildren().remove(pagination);
        tableView = new TableView();
        pagination = new Pagination();
        tableView.prefHeightProperty().bind(stage.heightProperty());
        tableView.prefWidthProperty().bind(stage.widthProperty());
        rightVBox.getChildren().add(tableView);
        rightVBox.getChildren().add(pagination);
    }

    private void refreshMetaInfo(){
        leftVBox.getChildren().clear();
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
        //ScrollPane scrollSummaryPane = new ScrollPane();
        Label nameKey = new Label("File name:   ");
        nameKey.setFont(Font.font("Arial", FontWeight.BLACK, 15));
        Label name = new Label(selectedFile.getName());
        name.setFont(Font.font(15));
        HBox nameHBox = new HBox(nameKey, name);
        nameHBox.setPadding(new Insets(10,10,0,10));
        Label rowNumberKey = new Label("Row number:   ");
        rowNumberKey.setFont(Font.font("Arial", FontWeight.BLACK, 15));
        Label rowNumber = new Label(String.valueOf(rowNum));
        rowNumber.setFont(Font.font(15));
        HBox rowNumberHBox = new HBox(rowNumberKey, rowNumber);
        rowNumberHBox.setPadding(new Insets(10,10,0,10));

        Label colNumberKey = new Label("Column number:   ");
        colNumberKey.setFont(Font.font("Arial", FontWeight.BLACK, 15));
        Label colNumber = new Label(String.valueOf(propertyList.size()));
        colNumber.setFont(Font.font(15));
        HBox colNumberHBox = new HBox(colNumberKey, colNumber);
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
        textArea.setText(getSchema().toString());
        System.out.println("name:" + getSchema().getName());
        System.out.println("namespace:" + getSchema().getNamespace());
        System.out.println("Type" + getSchema().getType());
        metaBox.getChildren().add(textArea);
        metaPane.setContent(metaBox);
        accordion.getPanes().add(summaryPane);
        accordion.getPanes().add(metaPane);
        leftVBox.getChildren().add(accordion);
    }

    public Schema getSchema(){
        return schema;
    }
    public void refreshTable(){
        refreshTable(showingList);
    }
    public void refreshTable(List<String> showingList){
        initTable();
        this.showingList = showingList;


        int index = 0;
        for (String property: propertyList){
            if (!showingList.contains(property)){
                continue;
            }
            TableColumn<List<StringProperty>, String> tableColumn = new TableColumn<>(property);
            int finalIndex = index;
            tableColumn.setCellValueFactory(data -> data.getValue().get(finalIndex));
            tableView.getColumns().add(tableColumn);
            index++;
        }

        int pageCount = rowNum / Constants.MAX_ROW_NUM + 1;
        if (rowNum % Constants.MAX_ROW_NUM == 0) {
            pageCount--;
        }
        int colNumber = propertyList.size();
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(10);

        pagination.setPageFactory((pageIndex) -> {
            generatePage(list, tableView, pageIndex, Constants.MAX_ROW_NUM, colNumber, showingList);
            VBox vbox = new VBox();
            vbox.getChildren().add(tableView);
            return vbox;
        });
    }

    private void generatePage(List<GenericData.Record> list, TableView tableView, int pageIndex, int pageRowNum, int colNumber, List<String> showingList){
        ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();
        int start = pageIndex * pageRowNum;
        int end = start + pageRowNum;
        if (end > list.size()){
            end = list.size();
        }
        for (int i = start; i < end; i++) {
            GenericData.Record r = list.get(i);
            List<StringProperty> row = new ArrayList<>();
            int index = 0;
            for (int j = 0; j < colNumber; j++){
                if(showingList.contains(propertyList.get(j))){
                    if (r.get(j) == null){
                        row.add(index, new SimpleStringProperty("NULL"));
                    }
                    else{
                        row.add(index, new SimpleStringProperty(r.get(j).toString()));
                    }
                    index++;
                }
            }
            data.add(row);
        }
        tableView.setItems(data);
    }

    public List<GenericData.Record> getData(){
        return list;
    }
}
