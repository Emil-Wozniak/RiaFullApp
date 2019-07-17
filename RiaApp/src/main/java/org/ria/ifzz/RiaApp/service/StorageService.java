package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.exception.StorageException;
import org.ria.ifzz.RiaApp.exception.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    void init() throws StorageException;

    FileEntity storeAndSaveFileEntity(MultipartFile file, String username) throws IOException, StorageException;

    Iterable<FileEntity> loadAll();

    void deleteAll();

    FileEntity getByDataId(String dataId) throws FileNotFoundException, FileEntityNotFoundException;

}
