package org.eugene.controller;

import org.apache.avro.Schema;
import org.apache.hadoop.fs.Path;
import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;
import org.eugene.persistent.VirtualDB;

import java.util.List;

public abstract class DataParser {
    public abstract boolean parseData(Path path);
    void persistData(Schema schema, List<String> propertyList, List<List<String>> data, TableMeta tableMeta){
        CommonData commonData = new CommonData();
        commonData.setSchema(schema.toString());
        commonData.setData(data);
        commonData.setPropertyList(propertyList);

        VirtualDB.getInstance().setCommonData(commonData);
        VirtualDB.getInstance().setTableMeta(tableMeta);
    }
}
