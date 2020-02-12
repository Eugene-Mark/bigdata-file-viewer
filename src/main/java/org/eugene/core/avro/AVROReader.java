package org.eugene.core.avro;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.hadoop.fs.Path;
import org.eugene.ui.Notifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AVROReader {
    public List<GenericRecord> read(Path path){
        try{
            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
            DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(new File(path.toString()), datumReader);
            List<GenericRecord> data = new ArrayList<>();
            GenericRecord record = null;
            while (dataFileReader.hasNext()) {
                record = dataFileReader.next(record);
                data.add(record);
            }
            return data;
        }catch(Exception e){
            e.printStackTrace();
            Notifier.error("Failed to load the file! The exception throws is:  " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        AVROReader reader = new AVROReader();
        reader.read(new Path("/Users/upma/gene/kylo/samples/sample-data/avro/userdata1.avro"));
    }
}
