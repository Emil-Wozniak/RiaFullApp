package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface StorageService {

    void init();

//    void store(MultipartFile file);

    void store(MultipartFile file, RedirectAttributes redirectAttributes);

    Iterable<FileEntity> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    FileEntity getByDataId(String dataId) throws FileNotFoundException;

    void delete(Long id);
}
