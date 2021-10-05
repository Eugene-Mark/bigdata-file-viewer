package org.eugene.controller;

import org.apache.avro.Schema;
import org.apache.hadoop.fs.Path;
import org.eugene.config.Config;
import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;
import org.eugene.persistent.PhysicalDB;
import org.eugene.persistent.VirtualDB;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {
    String name;
    public boolean parseData(Path path){
        name = getName(path);
        return true;
    }

    void persistData(Schema schema, Map<String, String> columnToType, List<List<String>> data, TableMeta tableMeta, Path path){
        CommonData commonData = new CommonData();
        commonData.setSchema(schema.toString());
        commonData.setData(data);
        commonData.setColumnToType(columnToType);
        commonData.setName(name);

        VirtualDB.getInstance().setCommonData(commonData);
        VirtualDB.getInstance().setTableMeta(tableMeta);

        if(Config.getInstance().enableAnalytics()){
            PhysicalDB.getInstance().persist(commonData);
        }
    }
    private String getName(Path path){
        String regex = ".*[\\/|\\\\]([\\w]+)[.]?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path.toString());
        System.out.println("The path is: " + path);
        String name = "place_holder";
        if (matcher.find()){
            name = matcher.group(1);
        }else{
            System.out.println("Not match");
        }
        return name;
    }
}
