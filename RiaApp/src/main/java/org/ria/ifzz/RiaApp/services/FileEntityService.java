package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.repositories.FileEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileEntityService {

    @Autowired
    private FileEntityRepository fileEntityRepository;

    public Iterable<FileEntity> loadAll() {
        return fileEntityRepository.findAll();}
}
