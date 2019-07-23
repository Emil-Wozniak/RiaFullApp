package org.ria.ifzz.RiaApp.controllers;

import org.ria.ifzz.RiaApp.services.examination.ControlCurveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{probeNumber}")
    public ResponseEntity<?> getControlCurvePoint(@PathVariable Integer probeNumber){
        return service.findByProbeNumberOrderByIdentifier(probeNumber);
    }
}
