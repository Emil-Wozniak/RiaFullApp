package org.ria.ifzz.RiaApp.controller;

import org.ria.ifzz.RiaApp.domain.FileData;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.service.FileDataService;
import org.ria.ifzz.RiaApp.service.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/file_data")
@CrossOrigin(origins = "http://localhost:3000")
public class FileDataController {

    @Autowired
    MapValidationErrorService errorService;

    private final FileDataService fileDataService;

    @Autowired
    public FileDataController(FileDataService fileDataService) {
        this.fileDataService = fileDataService;
    }

    @GetMapping("/{fileDataId}")
    public ResponseEntity<FileData> getFileEntityById(@PathVariable Long fileDataId) throws FileNotFoundException {
        FileData fileData = fileDataService.getById(fileDataId);
        return new ResponseEntity<>(fileData, HttpStatus.OK);
    }

    @GetMapping("/{fileDataId}/{result_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable Long fileDataId, @PathVariable String result_id) throws FileNotFoundException {
        Result result = fileDataService.findResultById(fileDataId, result_id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
