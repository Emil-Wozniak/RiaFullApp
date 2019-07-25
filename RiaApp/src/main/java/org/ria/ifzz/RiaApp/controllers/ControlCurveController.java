package org.ria.ifzz.RiaApp.controllers;

import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.services.examination.ControlCurveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/control_curve/")
@CrossOrigin(origins = "http://localhost:3000")
public class ControlCurveController {

    private final ControlCurveService service;

    public ControlCurveController(ControlCurveService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<?> getControlCurves(){
        return service.findAll();
    }

    @GetMapping("/{identifier}")
    public List<ControlCurve> getControlCurves(@PathVariable String identifier){
        return service.getControlCurves(identifier);
    }
}
