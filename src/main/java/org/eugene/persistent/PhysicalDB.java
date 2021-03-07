package org.eugene.persistent;

import org.eugene.model.CommonData;

public class PhysicalDB{
    private static PhysicalDB instance = new PhysicalDB();
    private SqlliteWrapper sqlliteWrapper;
    CommonData commonData;

    private PhysicalDB(){
    }

    public void init(){
        sqlliteWrapper = new SqlliteWrapper();
        sqlliteWrapper.createNewDatabase("bdf.db");
    }

    public static PhysicalDB getInstance(){
        return instance;
    }

    public boolean persist(CommonData commonData){

        return false;
    }

    public void updateLocation(String path){
        sqlliteWrapper.updateLocation(path);
    }

    public String getLocation(){
        return sqlliteWrapper.getLocation();
    }
}
