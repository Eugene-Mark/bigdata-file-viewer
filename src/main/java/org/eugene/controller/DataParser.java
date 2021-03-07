package org.eugene.controller;

import org.apache.avro.Schema;
import org.apache.hadoop.fs.Path;
import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;
import org.eugene.persistent.PhysicalDB;
import org.eugene.persistent.VirtualDB;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {
    String name;
    public boolean parseData(Path path){
        name = getName(path);
        return true;
    }

    void persistData(Schema schema, List<String> propertyList, List<List<String>> data, TableMeta tableMeta, Path path){
        CommonData commonData = new CommonData();
        commonData.setSchema(schema.toString());
        commonData.setData(data);
        commonData.setPropertyList(propertyList);
        commonData.setName(name);

        VirtualDB.getInstance().setCommonData(commonData);
        VirtualDB.getInstance().setTableMeta(tableMeta);

        PhysicalDB.getInstance().persist(commonData);
    }
    private String getName(Path path){
        String regex = "[\\/ | \\\\]([\\w]+)([.](parquet|orc|avro))?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path.getName());
        String name = "place-holder";
        if (matcher.find()){
            name = matcher.group(1);
        }
        return name;
    }
}
