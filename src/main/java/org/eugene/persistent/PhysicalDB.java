package org.eugene.persistent;

import org.eugene.model.CommonData;

import java.util.Map;

public class PhysicalDB{
    private static PhysicalDB instance = new PhysicalDB();
    private SqlliteWrapper sqlliteWrapper;
    CommonData commonData;

    private PhysicalDB(){
    }

    public void init(){
        sqlliteWrapper = new SqlliteWrapper();
        String databaseName = "bdf.db";
        sqlliteWrapper.createNewDatabase("bdf.db");
    }

    public static PhysicalDB getInstance(){
        return instance;
    }

    public boolean persist(CommonData commonData){
        this.commonData = commonData;
        sqlliteWrapper.persistData(commonData);
        return true;
    }

    public void updateLocation(String path){
        sqlliteWrapper.updateLocation(path);
    }

    public String getLocation(){
        return sqlliteWrapper.getLocation();
    }

    public Map<String,String> getAggregation(String columnName){
        return sqlliteWrapper.getAggregations(commonData, columnName);
    }

    public Map<String, Integer> getProportion(String columnName){
        return sqlliteWrapper.getProportion(commonData, columnName);
    }
}
