package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.repositorie.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class BacklogService {

    @Autowired
    private FileEntityService fileEntityService;

    @Autowired
    private ResultRepository resultRepository;

    /**
     *
     * @param dataId unique identifier
     * @return Result from repository if exists
     * @throws FileNotFoundException
     */
    public Iterable<Result> findBacklogByDataId(String dataId) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);
        return resultRepository.findByDataIdOrderByFileName(dataId);
    }

    public Result findResultByDataId(String dataId, String fileName) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);

        Result result = resultRepository.findByFileName(fileName);
        if (result == null) {
            throw new FileEntityNotFoundException("File with ID: '" + fileName + "' not found");
        }
        if (!result.getDataId().equals(dataId)) {
            throw new FileEntityNotFoundException("Result '" + fileName + "' does not exist: '" + dataId);
        }
        return result;
    }
}
