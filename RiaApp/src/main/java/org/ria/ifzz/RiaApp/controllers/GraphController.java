package org.ria.ifzz.RiaApp.controllers;

import org.ria.ifzz.RiaApp.services.examination.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
