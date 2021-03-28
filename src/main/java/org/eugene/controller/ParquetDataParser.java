package org.eugene.controller;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.fs.Path;
import org.eugene.core.parquet.ParquetReader;
import org.eugene.model.TableMeta;
import org.eugene.ui.Constants;
import org.eugene.ui.Notifier;

import java.util.*;

public class ParquetDataParser extends DataParser{
    @Override
    public boolean parseData(Path path) {
        super.parseData(path);
        ParquetReader reader = new ParquetReader();
        List<GenericData.Record> originalData = reader.read(path);
        if(originalData == null)
        {
            return false;
        }
        if (originalData.isEmpty()) {
            Notifier.info("The file is empty");
            return false;
        }

        GenericData.Record firstRecord = originalData.get(0);
        Schema schema = firstRecord.getSchema();

        int rowNumber = originalData.size();
        Map<String, String> columnToType = new LinkedHashMap<String, String>();

        for (Schema.Field field: schema.getFields())
        {
            columnToType.put(field.name(), field.schema().getType().getName());
        }

        int columnNumber = columnToType.size();
        TableMeta tableMeta = new TableMeta();
        tableMeta.setRow(rowNumber);
        tableMeta.setColumn(columnNumber);

        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < originalData.size(); i++) {
            GenericData.Record record = originalData.get(i);
            List<String> commonRecord = new ArrayList<>();
            for (int j = 0; j < columnNumber; j++) {
                if (record.get(j) == null){
                    commonRecord.add(Constants.NULL);
                }else{
                    commonRecord.add(String.valueOf(record.get(j)));
                }
            }
            data.add(commonRecord);
        }

        super.persistData(schema, columnToType, data, tableMeta, path);

        return true;
    }
}
