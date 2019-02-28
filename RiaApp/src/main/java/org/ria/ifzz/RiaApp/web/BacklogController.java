package org.ria.ifzz.RiaApp.web;

import org.ria.ifzz.RiaApp.domain.ControlCurve;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.service.BacklogService;
import org.ria.ifzz.RiaApp.service.ControlCurveService;
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
    private final ControlCurveService controlCurveService;

    @Autowired
    public BacklogController(BacklogService backlogService, ControlCurveService controlCurveService) {
        this.backlogService = backlogService;
        this.controlCurveService = controlCurveService;
    }

    @GetMapping("/{dataId}")
    public Iterable<Result> getFileEntityBacklog(@PathVariable String dataId) throws FileNotFoundException {
        return backlogService.findBacklogByDataId(dataId);
    }

    @GetMapping("/{dataId}/{fileName}")
    public ResponseEntity<?> getCurve(@PathVariable String dataId, @PathVariable String fileName) throws FileNotFoundException {

        ControlCurve controlCurve = controlCurveService.findResultByDataId(dataId, fileName);
        return new ResponseEntity<>(controlCurve, HttpStatus.OK);
    }

    @GetMapping("/{dataId}/curve")
    public Iterable<ControlCurve> getFileEntityBacklogCC(@PathVariable String dataId) throws FileNotFoundException {
        return controlCurveService.findCCBacklogByDataId(dataId);
    }

    @GetMapping("/{dataId}/curve/{fileName}")
    public ResponseEntity<?> getResult(@PathVariable String dataId, @PathVariable String fileName) throws FileNotFoundException {

        Result result = backlogService.findResultByDataId(dataId, fileName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
