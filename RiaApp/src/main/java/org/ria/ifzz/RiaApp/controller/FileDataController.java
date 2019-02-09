package org.ria.ifzz.RiaApp.controller;

import org.ria.ifzz.RiaApp.domain.FileData;
import org.ria.ifzz.RiaApp.repositories.FileDataRepository;
import org.ria.ifzz.RiaApp.repositories.FileEntityRepository;
import org.ria.ifzz.RiaApp.repositories.ResultRepository;
import org.ria.ifzz.RiaApp.services.FileDataService;
import org.ria.ifzz.RiaApp.services.MapValidationErrorService;
import org.ria.ifzz.RiaApp.services.ResultService;
import org.ria.ifzz.RiaApp.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/fileData")
@CrossOrigin(origins = "http://localhost:3000")
public class FileDataController {

    @Autowired
    MapValidationErrorService errorService;

    private final StorageService storageService;
    private final FileEntityRepository fileEntityRepository;
    private final ResultService resultService;
    private final FileDataService fileDataService;
    private final FileDataRepository fileDataRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public FileDataController(StorageService storageService, FileEntityRepository fileEntityRepository, ResultService resultService, FileDataService fileDataService, FileDataRepository fileDataRepository, ResultRepository resultRepository) {
        this.storageService = storageService;
        this.fileEntityRepository = fileEntityRepository;
        this.resultService = resultService;
        this.fileDataService = fileDataService;
        this.fileDataRepository = fileDataRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/{fileDataId}")
    public ResponseEntity<FileData> getFileEntityById(@PathVariable Long fileDataId) throws FileNotFoundException {
        FileData fileData = fileDataService.getById(fileDataId);
        return new ResponseEntity<>(fileData, HttpStatus.OK);
    }
}
