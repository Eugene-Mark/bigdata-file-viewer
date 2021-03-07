package org.eugene.persistent;

import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;

public class VirtualDB {
    private static VirtualDB instance = new VirtualDB();
    private CommonData commonData;
    private TableMeta tableMeta;

    private VirtualDB(){

    }

    public static VirtualDB getInstance(){
        return instance;
    }

    public void setCommonData(CommonData commonData){
        this.commonData = commonData;
    }

    public CommonData getCommonData(){
        return commonData;
    }

    public void setTableMeta(TableMeta tableMeta){
        this.tableMeta = tableMeta;
    }

    public TableMeta getTableMeta(){
        return tableMeta;
    }
}
