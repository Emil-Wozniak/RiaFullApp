package org.ria.ifzz.RiaApp.web;

import org.ria.ifzz.RiaApp.domain.GraphCurve;
import org.ria.ifzz.RiaApp.service.GraphCurveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin
public class GraphCurveController {

    private final GraphCurveService graphCurveService;

    @Autowired
    public GraphCurveController(GraphCurveService graphCurveService) {
        this.graphCurveService = graphCurveService;
    }

    @GetMapping("/{dataId}")
    public Iterable<GraphCurve> getFileEntityBacklogGC(@PathVariable String dataId) throws FileNotFoundException {
        return graphCurveService.findBacklogByDataId(dataId);
    }

    @GetMapping("/{dataId}/{fileName}")
    public ResponseEntity<?> getGraphCurvePoint(@PathVariable String dataId, @PathVariable String fileName) throws FileNotFoundException {

        GraphCurve graphCurve = graphCurveService.findResultByDataId(dataId, fileName);
        return new ResponseEntity<>(graphCurve, HttpStatus.OK);
    }
}
