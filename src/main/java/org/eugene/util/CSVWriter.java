package org.eugene.util;

import org.apache.hadoop.fs.Path;
import org.eugene.persistent.VirtualDB;
import org.eugene.ui.Constants;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {
    public static boolean write(Path path, List<List<String>> data, String delimiter){
        try{
            PrintWriter out = new PrintWriter(path.toString());
            if (data.size() == 0) {
                out.write("");
                return true;
            }
            List<String> propertyList = new ArrayList<String>(VirtualDB.getInstance().getCommonData().getColumnToType().keySet());
            int colNumber = propertyList.size();
            for (int i = 0; i < colNumber; i++) {
                if (i == (colNumber - 1)){
                    out.println(propertyList.get(i));
                }else{
                    out.print(propertyList.get(i));
                    out.print(delimiter);
                }
            }

            for (List<String> record: data) {
                for (int i = 0; i < colNumber; i++) {
                    if (i == (colNumber - 1)) {
                        if (record.get(i) == null)
                            out.println(Constants.NULL);
                        else {
                                if (record.get(i).toString().contains(delimiter)) {
                                    out.println("\"" + record.get(i) + "\"");
                                }else{
                                    out.println(record.get(i));
                                }
                             }
                    }else{
                        if (record.get(i) == null){
                            out.print(Constants.NULL);
                        }
                        else{
                            if (record.get(i).toString().contains(delimiter)){
                                out.print("\"" + record.get(i) + "\"");
                            }else{
                                out.print(record.get(i));
                            }
                        }
                        out.print(delimiter);
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
