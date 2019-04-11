package org.ria.ifzz.RiaApp.utils;

import org.ria.ifzz.RiaApp.domain.DataFileMetadata;
import org.springframework.stereotype.Service;

@Service
public class FileUtils {

    public String setFileName(DataFileMetadata file) {
        String fileName = file.getFileName();
        return fileName;
    }
}
