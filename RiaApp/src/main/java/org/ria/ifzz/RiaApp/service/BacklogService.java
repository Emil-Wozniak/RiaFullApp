package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class BacklogService {

    private final FileEntityService fileEntityService;
    private final ResultRepository resultRepository;

    public BacklogService(FileEntityService fileEntityService, ResultRepository resultRepository) {
        this.fileEntityService = fileEntityService;
        this.resultRepository = resultRepository;
    }

    /**
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

    public Backlog setBacklog(FileEntity fileEntity) {
        Backlog backlog = new Backlog();
        backlog.setFileEntity(fileEntity);
        backlog.setFileName(fileEntity.getFileName());
        backlog.setDataId(fileEntity.getDataId());
        backlog.setContentType(fileEntity.getContentType());
        return backlog;
    }
}
