package org.ria.ifzz.RiaApp.utils;

import org.ria.ifzz.RiaApp.domain.DataFileMetadata;
import org.springframework.stereotype.Service;

public interface FileUtils {

    static String setFileName(DataFileMetadata file) {
        return file.getFileName();
    }
}
