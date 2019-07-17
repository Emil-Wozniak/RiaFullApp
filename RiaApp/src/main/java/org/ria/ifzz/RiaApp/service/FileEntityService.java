package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.repository.FileEntityRepository;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class FileEntityService {

    private final FileEntityRepository fileEntityRepository;

    public FileEntityService(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
    }

    void findFileEntityByDataId(String dataId) throws FileNotFoundException {
        FileEntity fileEntity = fileEntityRepository.findByDataId(dataId);
        if (fileEntity == null) {
            throw new FileNotFoundException("File does not exist");}
    }
}
