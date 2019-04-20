package org.ria.ifzz.RiaApp.web;

import org.ria.ifzz.RiaApp.domain.GraphCurve;
import org.ria.ifzz.RiaApp.domain.GraphCurveLines;
import org.ria.ifzz.RiaApp.service.GraphCurveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.Optional;

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
    public GraphCurve getFileEntityBacklogGC(@PathVariable String dataId) throws FileNotFoundException {
        return graphCurveService.findBacklogByDataId(dataId);
    }

    @GetMapping("/{dataId}/{fileName}/coordinates/{id}")
    public ResponseEntity<?> getGraphCurvePoints(@PathVariable String dataId, @PathVariable String fileName, @PathVariable Long id) throws FileNotFoundException {

        Optional<GraphCurveLines> graphCurveLines = graphCurveService.findResultForCoordinatesByDataId(dataId, fileName, id);
        return new ResponseEntity<>(graphCurveLines, HttpStatus.OK);
    }
}
