package org.ria.ifzz.RiaApp.utils;

import org.ria.ifzz.RiaApp.models.DataFileMetadata;

public interface FileUtils {

    static String setFileName(DataFileMetadata file) {
        return file.getFileName();
    }
}
