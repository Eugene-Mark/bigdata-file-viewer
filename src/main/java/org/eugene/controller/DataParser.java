package org.eugene.controller;

import org.apache.hadoop.fs.Path;

public abstract class DataParser {
    public abstract boolean parseData(Path path);
}
