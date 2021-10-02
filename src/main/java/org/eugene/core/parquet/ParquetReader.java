package org.eugene.core.parquet;

import org.apache.avro.generic.GenericData;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.eugene.core.common.Reader;
import org.eugene.ui.Notifier;

import java.util.ArrayList;
import java.util.List;

public class ParquetReader extends Reader {
    public List<GenericData.Record> read(Path path){
        List<GenericData.Record> list = new ArrayList<>();
        try{
            InputFile inputFile = HadoopInputFile.fromPath(path, super.getConfiguration());
            org.apache.parquet.hadoop.ParquetReader<GenericData.Record> reader =
                    AvroParquetReader.<GenericData.Record>builder(inputFile)
                            .disableCompatibility()
                            .withDataModel(GenericData.get())
                            .withConf(super.getConfiguration())
                            .build();
            GenericData.Record record;
            while((record = reader.read()) != null){
                list.add(record);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
            Notifier.errorWithException(e);
            return null;
        }
    }
}
