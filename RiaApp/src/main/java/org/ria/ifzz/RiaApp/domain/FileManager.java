package org.ria.ifzz.RiaApp.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FileManager {

    @Getter
    private Map<Long, Supplier<List<String>>> dataTable = new HashMap<>();

    public void process(DataFileMetadata metadata){
        dataTable.put(metadata.getCustomerId(), metadata.getContents());
    }
}
