package org.ria.ifzz.RiaApp.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class FileModel {

    @Getter
    @Setter
    private MultipartFile file;
}
