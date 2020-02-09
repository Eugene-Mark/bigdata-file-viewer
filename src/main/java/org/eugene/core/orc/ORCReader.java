package org.eugene.core.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcFile;
import org.apache.hadoop.hive.ql.io.orc.Reader;
import org.apache.hadoop.hive.ql.io.orc.RecordReader;

import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;
import org.eugene.persistent.VirtualDB;
import org.eugene.ui.Constants;
import org.eugene.ui.Notifier;

import java.util.ArrayList;
import java.util.List;

public class ORCReader {
    public boolean read(Path path){
        try{
            Reader reader = OrcFile.createReader(path, OrcFile.readerOptions(new Configuration()));
            StructObjectInspector inspector = (StructObjectInspector)reader.getObjectInspector();
            String schema = reader.getSchema().toJson();
            //The JSON schema provided is illegal, so need to make it valid firstly
            schema = schema.replaceAll("(\"[\\w]+\"):([\\s]+[{]+)", "$1,$2");
            RecordReader records = reader.rows();
            List fields = inspector.getAllStructFieldRefs();
            List<String> propertyList = new ArrayList<>();
            int columnNumber = fields.size();
            for(int i = 0; i < fields.size(); ++i) {
                propertyList.add(((StructField)fields.get(i)).getFieldObjectInspector().getTypeName());
            }

            Object row = null;
            List<List<String>> data = new ArrayList<>();
            while(records.hasNext())
            {
                row = records.next(row);
                List list = inspector.getStructFieldsDataAsList(row);
                StringBuilder builder = new StringBuilder();
                List<String> record = new ArrayList<>();
                for(Object field : list) {
                    if(field != null){
                        record.add(field.toString());
                    }
                    else{
                        record.add(Constants.NULL);
                    }
                }
                data.add(record);
            }
            CommonData commonData = new CommonData();
            commonData.setPropertyList(propertyList);
            commonData.setSchema(schema);
            commonData.setData(data);
            TableMeta tableMeta = new TableMeta();
            tableMeta.setColumn(columnNumber);
            tableMeta.setRow(data.size());
            VirtualDB.getInstance().setCommonData(commonData);
            VirtualDB.getInstance().setTableMeta(tableMeta);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            Notifier.error("Failed to load the file! The exception throws is:  " + e.getMessage());
            return false;
        }

    }

}
