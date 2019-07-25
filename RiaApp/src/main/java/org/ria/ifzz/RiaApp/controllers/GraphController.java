package org.ria.ifzz.RiaApp.controllers;

import org.ria.ifzz.RiaApp.models.graph.Graph;
import org.ria.ifzz.RiaApp.models.graph.GraphLine;
import org.ria.ifzz.RiaApp.services.examination.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/graph/")
@CrossOrigin(origins = "http://localhost:3000")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getGraphs(){
        return graphService.findAll();
    }

    @GetMapping("/lines/")
    public ResponseEntity<?> getGraphsLines(){
        return graphService.findAllLines();
    }

    @GetMapping("/{identifier}")
    public List<Graph> getGraph(@PathVariable String identifier){
        return graphService.getGraph(identifier);
    }

    @GetMapping("/lines/{identifier}")
        public List<GraphLine> getGraphLines(@PathVariable String identifier){
        return graphService.getGraphLines(identifier);
        }
}
