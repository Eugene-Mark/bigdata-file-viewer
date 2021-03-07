package org.eugene.model;

import java.util.List;

public class CommonData {
    private String schema;
    private List<List<String>> data;
    private List<String> propertyList;
    private String name;

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

    public void setPropertyList(List<String> propertyList){
        this.propertyList = propertyList;
    }

    public List<String> getPropertyList(){
        return propertyList;
    }

    public void setName(String name){this.name = name;}

    public String getName(){return name;}
}
