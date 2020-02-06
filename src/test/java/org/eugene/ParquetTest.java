package org.eugene;

import org.apache.avro.generic.GenericData;
import org.apache.hadoop.fs.Path;
import org.eugene.core.parquet.ParquetReader;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

public class ParquetTest {
    @Test
    public void testParquetRead() throws Exception {
        ParquetReader reader = new ParquetReader();
        URL url;
        url = getClass().getClassLoader().getResource("000000_0.parquet");
        assertNotNull(url);
        Path path = new Path(url.toURI());
        List<GenericData.Record> list = reader.read(path);
        assertNotNull(list);
    }
}
