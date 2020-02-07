package org.eugene.controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.fs.Path;
import org.eugene.core.parquet.ParquetReader;
import org.eugene.model.Parquet;
import org.eugene.model.TableMeta;
import org.eugene.ui.Dashboard;
import org.eugene.ui.Main;
import org.eugene.ui.Notifier;
import org.eugene.ui.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private Stage stage;
    private TableRenderer tableRenderer;
    private DashboardRenderer dashboardRenderer;

    private List<String> showingList;
    private List<String> propertyList;

    private File selectedFile;

    private Parquet parquet;
    private TableMeta tableMeta;

    private Schema schema;

    public Renderer(Stage stage){
        this.stage = stage;
        tableRenderer = new TableRenderer();
        dashboardRenderer = new DashboardRenderer();
    }

    public void initUI(){
        Table table = new Table(stage);
        Dashboard dashboard = new Dashboard(stage);
        tableRenderer.setTable(table);
        dashboardRenderer.setDashboard(dashboard);
        Main main = new Main(stage, table, dashboard);
        main.initUI();
    }

    public boolean loadAndShow(){
        boolean status = prepareData();
        if (status) {
            tableRenderer.init();
            showingList = propertyList;
            dashboardRenderer.refreshMetaInfo(parquet.getSchema(), selectedFile, tableMeta.getRow(), tableMeta.getColumn());
            tableRenderer.refresh(showingList, propertyList, tableMeta.getRow(), tableMeta.getColumn(), parquet.getData());
        }
        return status;
    }

    private boolean prepareData(){
        FileChooser filechooser = new FileChooser();
        selectedFile = filechooser.showOpenDialog(stage);
        Path path = new Path(selectedFile.getAbsolutePath());

        ParquetReader reader = new ParquetReader();
        List<GenericData.Record> data = reader.read(path);
        if(data == null)
        {
            return false;
        }
        if (data.isEmpty()) {
            Notifier.info("The file is empty");
        }
        parquet = new Parquet();
        parquet.setData(data);
        GenericData.Record record = data.get(0);
        schema = record.getSchema();
        parquet.setSchema(schema);
        int rowNumber = data.size();
        showingList = new ArrayList<>();
        propertyList = new ArrayList<>();
        for (Schema.Field field: schema.getFields())
        {
            String property = field.name();
            showingList.add(property);
            propertyList.add(property);
        }
        int columnNumber = propertyList.size();
        tableMeta = new TableMeta();
        tableMeta.setRow(rowNumber);
        tableMeta.setColumn(columnNumber);

        return true;
    }

    public List<GenericData.Record> getData(){
        return parquet.getData();
    }

    public void refreshTable(){
        refreshTable(showingList);
    }

    public void refreshTable(List<String> showingList){
        tableRenderer.refresh(showingList, propertyList, tableMeta.getRow(), tableMeta.getColumn(), parquet.getData());
    }

    public Schema getSchema() {
        return schema;
    }
}
