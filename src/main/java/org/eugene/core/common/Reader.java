package org.eugene.core.common;


import lombok.Getter;

import org.apache.hadoop.conf.Configuration;

@Getter
public class Reader {
  private final Configuration configuration;

  public Reader() {
	configuration = new Configuration();
	configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
	configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
	configuration.set("parquet.avro.readInt96AsFixed", "true");
	System.out.println("parquet.avro.readInt96AsFixed=true");
  }
}
