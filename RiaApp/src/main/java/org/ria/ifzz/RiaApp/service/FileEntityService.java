package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.repositorie.FileEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class FileEntityService {

    @Autowired
    private FileEntityRepository fileEntityRepository;

    public Iterable<FileEntity> loadAll() {
        return fileEntityRepository.findAll();
    }

    public FileEntity getFileEntityByDataId(String dataId) throws FileNotFoundException {
        FileEntity fileEntity = fileEntityRepository.findByDataId(dataId.toUpperCase());
        if (fileEntity == null) {
            throw new FileNotFoundException(
                    "File does not exist");
        }
        return fileEntity;
    }
}
