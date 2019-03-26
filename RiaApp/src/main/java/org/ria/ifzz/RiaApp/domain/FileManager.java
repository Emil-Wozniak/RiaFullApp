package org.ria.ifzz.RiaApp.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FileManager {

    @Getter
    private Map<Long, Supplier<String>> dataTable = new HashMap<>();

    public void process(FileModel metadata){
        dataTable.put(metadata.getCustomerId(), metadata.getContents());
    }
}
