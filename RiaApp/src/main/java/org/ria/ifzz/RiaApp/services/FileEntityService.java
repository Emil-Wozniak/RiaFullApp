package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.repositories.FileEntityRepository;
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

    public FileEntity getById(Long id) throws FileNotFoundException {
        FileEntity fileEntity = fileEntityRepository.getById(id);
        if (fileEntity == null) {
            throw new FileNotFoundException(
                    "Project does not exist");
        }
        return fileEntity;
    }
}
