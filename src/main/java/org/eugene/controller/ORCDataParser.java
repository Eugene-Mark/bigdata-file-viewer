package org.eugene.controller;

import org.apache.hadoop.fs.Path;
import org.eugene.core.orc.ORCReader;

public class ORCDataParser extends DataParser {
    @Override
    public boolean parseData(Path path) {
        super.parseData(path);
        ORCReader reader = new ORCReader();
        return reader.read(path);
    }
}
