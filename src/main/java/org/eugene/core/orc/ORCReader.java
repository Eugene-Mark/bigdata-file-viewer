package org.eugene.core.orc;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcFile;
import org.apache.hadoop.hive.ql.io.orc.Reader;
import org.apache.hadoop.hive.ql.io.orc.RecordReader;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.eugene.config.Config;
import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;
import org.eugene.persistent.PhysicalDB;
import org.eugene.persistent.VirtualDB;
import org.eugene.ui.Constants;
import org.eugene.ui.Notifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eugene.ui.Notifier;

public class ORCReader extends org.eugene.core.common.Reader {
    public boolean read(Path path){
        try{
            Reader reader = OrcFile.createReader(path, OrcFile.readerOptions(super.getConfiguration()));
            StructObjectInspector inspector = (StructObjectInspector)reader.getObjectInspector();
            String schema = reader.getSchema().toJson();
            //The JSON schema provided is illegal, so need to make it valid firstly
            schema = schema.replaceAll("(\"[\\w]+\"):([\\s]+[{]+)", "$1,$2");
            RecordReader records = reader.rows();
            List fields = inspector.getAllStructFieldRefs();
            Map<String, String> columnToType = new LinkedHashMap<String, String>();
            int columnNumber = fields.size();
            for(int i = 0; i < fields.size(); ++i) {
                columnToType.put(((StructField)fields.get(i)).getFieldName(), ((StructField) fields.get(i)).getFieldObjectInspector().getCategory().name());
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
            commonData.setColumnToType(columnToType);
            commonData.setSchema(schema);
            commonData.setData(data);
            String name = getName(path);
            commonData.setName(name);
            TableMeta tableMeta = new TableMeta();
            tableMeta.setColumn(columnNumber);
            tableMeta.setRow(data.size());
            VirtualDB.getInstance().setCommonData(commonData);
            VirtualDB.getInstance().setTableMeta(tableMeta);
            if(Config.getInstance().enableAnalytics()){
                PhysicalDB.getInstance().persist(commonData);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            Notifier.errorWithException(e);
            return false;
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
