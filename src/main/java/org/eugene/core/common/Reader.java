package org.eugene.core.common;


import org.apache.hadoop.conf.Configuration;

public class Reader {
    Configuration configuration;
    public Reader(){
        configuration = new Configuration();
        configuration.set("fs.hdfs.impl",
                org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
        );
        configuration.set("fs.file.impl",
                org.apache.hadoop.fs.LocalFileSystem.class.getName()
        );
    }

    public Configuration getConfiguration(){
        return configuration;
    }
}
