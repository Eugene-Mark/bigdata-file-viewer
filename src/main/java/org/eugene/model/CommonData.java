package org.eugene.model;

import java.util.List;
import java.util.Map;

public class CommonData {
    private String schema;
    private List<List<String>> data;
    //private List<String> propertyList;
    private String name;
    private Map<String, String> columnToType;

    public void setSchema(String schema){
        this.schema = schema;
    }

    public String getSchema(){
        return schema;
    }

    public void setData(List<List<String>> data){
        this.data = data;
    }

    public List<List<String>> getData(){
        return data;
    }

    /**
    public void setPropertyList(List<String> propertyList){
        this.propertyList = propertyList;
    }

    public List<String> getPropertyList(){
        return propertyList;
    }
    **/

    public void setColumnToType(Map<String, String> columnToType){ this.columnToType = columnToType; }

    public Map<String, String> getColumnToType(){ return columnToType; }

    public void setName(String name){this.name = name;}

    public String getName(){return name;}
}
