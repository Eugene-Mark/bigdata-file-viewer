package org.eugene.controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.hadoop.fs.Path;
import org.eugene.core.common.AWSS3Reader;
import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;
import org.eugene.persistent.PhysicalDB;
import org.eugene.persistent.VirtualDB;
import org.eugene.ui.Constants;
import org.eugene.ui.Dashboard;
import org.eugene.ui.Main;
import org.eugene.ui.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Renderer {

    private Stage stage;
    private TableRenderer tableRenderer;
    private DashboardRenderer dashboardRenderer;

    private List<String> showingList;

    public Renderer(Stage stage){
        this.stage = stage;
        tableRenderer = new TableRenderer();
        dashboardRenderer = new DashboardRenderer();
    }

    public void initUI(){
        Table table = new Table(stage, this);
        Dashboard dashboard = new Dashboard(stage);
        tableRenderer.setTable(table);
        dashboardRenderer.setDashboard(dashboard);
        Main main = new Main(stage, table, dashboard);
        main.initUI();
    }

    private boolean load(Path path){
        DataParser dataParser;
        if (path.toString().toLowerCase().endsWith("orc")){
            dataParser = new ORCDataParser();
        }else if(path.toString().toLowerCase().endsWith("avro")){
            dataParser = new AVRODataParser();
        }else{
            dataParser = new ParquetDataParser();
        }
        boolean status = dataParser.parseData(path);
        if (status) {
            tableRenderer.init();
            CommonData commonData = VirtualDB.getInstance().getCommonData();
            TableMeta tableMeta = VirtualDB.getInstance().getTableMeta();
            showingList = new ArrayList<String>(commonData.getColumnToType().keySet());
            dashboardRenderer.refreshMetaInfo(commonData.getSchema(), path.toString(), tableMeta.getRow(), tableMeta.getColumn(), true);
            tableRenderer.refresh(showingList, showingList, tableMeta.getRow(), tableMeta.getColumn(), commonData.getData());
        }
        return status;
    }

    public boolean loadAndShow(Map<String, String> map){
        AWSS3Reader awss3Reader = new AWSS3Reader();
        Path path = awss3Reader.read(map.get(Constants.BUCKET), map.get(Constants.FILE), map.get(Constants.REGION), map.get(Constants.ACCESSKEY), map.get(Constants.SECRETKEY));
        load(path);
        return false;
    }

    public boolean loadAndShow(Path path){
        return load(path);
    }

    public boolean loadAndShow(){
        FileChooser filechooser = new FileChooser();
        String location = PhysicalDB.getInstance().getLocation();
        if(!location.equals("")){
            System.out.println("The location returned: " + location);
            filechooser.setInitialDirectory(new File(getDirectory(location)));
        }else{
            System.out.println("The location is empty");
        }
        File selectedFile = filechooser.showOpenDialog(stage);
        String absolutePath = selectedFile.getAbsolutePath();
        PhysicalDB.getInstance().updateLocation(absolutePath);
        Path path = new Path(absolutePath);
        return load(path);
    }

    private String getDirectory(String fullPath){
        String regex = "(.*)[\\\\][.]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullPath);
        String directory = "";
        if(matcher.find()){
            directory = matcher.group(1);
        }
        return directory;
    }

    public List<List<String>> getData(){
        return VirtualDB.getInstance().getCommonData().getData();
    }

    public void refreshTable(){
        refreshTable(showingList);
    }

    public void refreshTable(List<String> showingList){
        CommonData commonData = VirtualDB.getInstance().getCommonData();
        TableMeta tableMeta = VirtualDB.getInstance().getTableMeta();
        tableRenderer.refresh(showingList, new ArrayList<String>(commonData.getColumnToType().keySet()), tableMeta.getRow(), tableMeta.getColumn(), commonData.getData());
    }

    public void refreshAggregationPane(String columnName){
        CommonData commonData = VirtualDB.getInstance().getCommonData();
        List<String> typeList = new ArrayList<String>();
        typeList.add("INTEGER");
        typeList.add("INT");
        typeList.add("LONG");
        typeList.add("DOUBLE");
        typeList.add("FLOAT");
        typeList.add("UNION");
        if(!typeList.contains(commonData.getColumnToType().get(columnName).toUpperCase())){
            return;
        };
        Map<String, String> keyToValue = PhysicalDB.getInstance().getAggregation(columnName);
        dashboardRenderer.refreshAggregationPane(columnName, keyToValue);
    }

    public void refreshProportionPane(String columnName){
        CommonData commonData = VirtualDB.getInstance().getCommonData();
        List<String> typeList = new ArrayList<String>();
        typeList.add("INTEGER");
        typeList.add("INT");
        typeList.add("LONG");
        typeList.add("DOUBLE");
        typeList.add("FLOAT");
        typeList.add("UNION");
        if(typeList.contains(commonData.getColumnToType().get(columnName).toUpperCase())){
            return;
        }
        Map<String, Integer> itemToCount = PhysicalDB.getInstance().getProportion(columnName);
        dashboardRenderer.refreshProportionPane(columnName, itemToCount);
    }

}
