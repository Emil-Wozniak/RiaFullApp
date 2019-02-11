package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.repositorie.BacklogRepository;
import org.ria.ifzz.RiaApp.repositorie.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class BacklogService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private FileEntityService fileEntityService;

    @Autowired
    private ResultRepository resultRepository;

    public Backlog getByDataId(String dataId) throws FileNotFoundException {
        Backlog backlog = backlogRepository.findByDataId(dataId);
        if (backlog == null) {
            throw new FileNotFoundException(
                    "File does not exist");
        }
        return backlog;
    }

    public Result findResultById(String backlog_id, String dataId) throws FileNotFoundException {
        fileEntityService.getFileEntityByDataId(backlog_id);

        Result result = resultRepository.findByDataId(dataId);
        if (result == null) {
            throw new FileEntityNotFoundException("Result '" + dataId + "' not found");
        }
        if (!result.getId().equals(backlog_id)) {
            throw new FileEntityNotFoundException("Result '" + dataId + "' does not exist: '" + backlog_id);
        }
        return result;
    }
}
