package org.eugene.core.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.fs.Path;
import org.eugene.core.common.Reader;
import org.eugene.ui.Notifier;

import java.util.ArrayList;
import java.util.List;

public class AVROReader extends Reader {
    public List<GenericRecord> read(Path path){
        try{
            SeekableInput inputFile = new FsInput(path, super.getConfiguration());

            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
            //DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(new File(path.toString()), datumReader);
            FileReader<GenericRecord> dataFileReader = DataFileReader.openReader(inputFile, datumReader);
            List<GenericRecord> data = new ArrayList<>();
            while (dataFileReader.hasNext()) {
                data.add(dataFileReader.next());
            }
            return data;
        }catch(Exception e){
            e.printStackTrace();
            Notifier.errorWithException(e);
            return null;
        }
    }
}
