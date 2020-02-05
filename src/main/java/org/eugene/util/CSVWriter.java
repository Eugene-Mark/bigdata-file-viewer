package org.eugene.util;

import oracle.jvm.hotspot.jfr.JFRTypeIDs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.fs.Path;

import java.io.PrintWriter;
import java.util.List;

public class CSVWriter {
    public static boolean write(Path path, List<GenericData.Record> list){
        try{
            PrintWriter out = new PrintWriter(path.toString());
            if (list.size() == 0) {
                out.write("");
                return true;
            }
            GenericData.Record firstRow = list.get(0);
            List<Schema.Field> fields = firstRow.getSchema().getFields();
            int colNumber = fields.size();
            for (int i = 0; i < colNumber; i++) {
                if (i == (colNumber - 1)){
                    out.println(fields.get(i).name());
                }else{
                    out.print(fields.get(i).name());
                    out.print(",");
                }
            }

            for (GenericData.Record record: list) {
                for (int i = 0; i < colNumber; i++) {
                    if (i == (colNumber - 1)) {
                        if (record.get(i) == null)
                            out.println("NULL");
                        else {
                                if (record.get(i).toString().contains(",")) {
                                    out.print("\"" + record.get(i) + "\"");
                                }else{
                                    out.println(record.get(i));
                                }
                             }
                    }else{
                        if (record.get(i) == null)
                            out.println("NULL");
                        else{
                            if (record.get(i).toString().contains(",")){
                                out.print("\"" + record.get(i) + "\"");
                            }else{
                                out.print(record.get(i));
                            }
                            out.print(",");
                        }
                    }
                }
            }
            out.flush();
            out.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
