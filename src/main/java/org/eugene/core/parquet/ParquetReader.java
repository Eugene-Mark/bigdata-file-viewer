package org.eugene.core.parquet;

import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.eugene.ui.Notifier;

import java.util.ArrayList;
import java.util.List;

public class ParquetReader {
    public List<GenericData.Record> read(Path path){
        List<GenericData.Record> list = new ArrayList<>();
        try{
            Configuration conf = new Configuration();
            conf.set("fs.hdfs.impl",
                    org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
            );
            conf.set("fs.file.impl",
                    org.apache.hadoop.fs.LocalFileSystem.class.getName()
            );
            InputFile inputFile = HadoopInputFile.fromPath(path, conf);
            org.apache.parquet.hadoop.ParquetReader<GenericData.Record> reader =
                    AvroParquetReader.<GenericData.Record>builder(inputFile)
                            .disableCompatibility()
                            .withDataModel(GenericData.get())
                            .withConf(conf)
                            .build();
            GenericData.Record record;
            while((record = reader.read()) != null){
                list.add(record);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
            Notifier.error("Failed to load the file! The exception throws is:  " + e.getMessage());
            return null;
        }
    }
}
