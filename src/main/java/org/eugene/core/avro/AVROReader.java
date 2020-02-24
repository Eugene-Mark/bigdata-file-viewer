package org.eugene.core.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.eugene.ui.Notifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AVROReader {
    public List<GenericRecord> read(Path path){
        try{
            Configuration configuration = new Configuration();
            SeekableInput inputFile = new FsInput(path, configuration);

            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
            //DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(new File(path.toString()), datumReader);
            FileReader<GenericRecord> dataFileReader = DataFileReader.openReader(inputFile, datumReader);
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
}
