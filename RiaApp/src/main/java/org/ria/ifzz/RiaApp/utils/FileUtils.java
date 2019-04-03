package org.ria.ifzz.RiaApp.utils;

import org.ria.ifzz.RiaApp.domain.FileModel;
import org.springframework.stereotype.Service;

@Service
public class FileUtils {

    public String setFileName(FileModel file) {
        String fileName = file.getFileName();
        return fileName;
    }
}
