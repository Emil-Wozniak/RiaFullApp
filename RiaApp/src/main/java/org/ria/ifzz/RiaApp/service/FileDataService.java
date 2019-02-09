package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileData;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.repositorie.FileDataRepository;
import org.ria.ifzz.RiaApp.repositorie.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FileEntityService fileEntityService;

    @Autowired
    private ResultRepository resultRepository;

    public FileData getById(Long id) throws FileNotFoundException {
        FileData fileData = fileDataRepository.getById(id);
        if (fileData == null) {
            throw new FileNotFoundException(
                    "Project does not exist");
        }
        return fileData;
    }

    public Result findResultById(Long backlog_id, String samples) throws FileNotFoundException {
        fileEntityService.getById(backlog_id);

        Result result = resultRepository.findBySamples(samples);
        if (result == null) {
            throw new FileEntityNotFoundException("Result '" + samples + "' not found");
        }
        if (!result.getId().equals(backlog_id)) {
            throw new FileEntityNotFoundException("Result '" + samples + "' does not exist: '" + backlog_id);
        }
        return result;
    }
}
