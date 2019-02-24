package org.ria.ifzz.RiaApp.controller;

import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.service.BacklogService;
import org.ria.ifzz.RiaApp.service.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    MapValidationErrorService errorService;

    private final BacklogService backlogService;

    @Autowired
    public BacklogController(BacklogService backlogService) {
        this.backlogService = backlogService;
    }

    @GetMapping("/{dataId}")
    public Iterable<Result> getFileEntityBacklog(@PathVariable String dataId) throws FileNotFoundException {
        return backlogService.findBacklogByDataId(dataId);
    }

    @GetMapping("/{dataId}/{fileName}")
    public ResponseEntity<?> getResult(@PathVariable String dataId, @PathVariable String fileName) throws FileNotFoundException {

        Result result = backlogService.findResultByDataId(dataId, fileName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
