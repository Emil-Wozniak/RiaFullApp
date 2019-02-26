package org.ria.ifzz.RiaApp.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUtils {

    public String setFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName;
    }
}
