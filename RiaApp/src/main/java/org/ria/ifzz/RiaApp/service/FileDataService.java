package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileData;
import org.ria.ifzz.RiaApp.repositorie.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    public FileData getById(Long id) throws FileNotFoundException {
        FileData fileData = fileDataRepository.getById(id);
        if (fileData == null) {
            throw new FileNotFoundException(
                    "Project does not exist");
        }
        return fileData;
    }
}
