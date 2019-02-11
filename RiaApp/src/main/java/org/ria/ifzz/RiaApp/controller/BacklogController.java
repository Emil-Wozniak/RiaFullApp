package org.ria.ifzz.RiaApp.controller;

import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.service.BacklogService;
import org.ria.ifzz.RiaApp.service.MapValidationErrorService;
import org.ria.ifzz.RiaApp.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin(origins = "http://localhost:3000")
public class BacklogController {

    @Autowired
    MapValidationErrorService errorService;

    private final BacklogService backlogService;
    private final ResultService resultService;

    @Autowired
    public BacklogController(BacklogService backlogService, ResultService resultService) {
        this.backlogService = backlogService;
        this.resultService = resultService;
    }

    @GetMapping("/{dataId}")
    public Iterable<Result> getFileEntityByFileDataId(@PathVariable String dataId) throws FileNotFoundException {
        return resultService.getFileDataById(dataId);
    }

    @GetMapping("/{dataId}/{result_id}")
    public ResponseEntity<?> getResult(@PathVariable String fileDataId, @PathVariable String result_id) throws FileNotFoundException {

        Result result = backlogService.findResultById(fileDataId, result_id);
        return new ResponseEntity<Result>(result, HttpStatus.OK);
    }
}
