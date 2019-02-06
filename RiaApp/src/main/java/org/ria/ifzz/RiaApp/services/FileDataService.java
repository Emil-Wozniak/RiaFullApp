package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.domain.FileData;
import org.ria.ifzz.RiaApp.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    public Iterable<FileData> loadAll() {
        return fileDataRepository.findAll();}
}
