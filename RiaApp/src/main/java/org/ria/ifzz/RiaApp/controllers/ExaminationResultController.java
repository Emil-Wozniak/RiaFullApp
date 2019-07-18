package org.ria.ifzz.RiaApp.controllers;

import org.ria.ifzz.RiaApp.exception.StorageException;
import org.ria.ifzz.RiaApp.models.DataFileMetadata;
import org.ria.ifzz.RiaApp.models.results.ExaminationResult;
import org.ria.ifzz.RiaApp.services.examination.ExaminationPointService;
import org.ria.ifzz.RiaApp.services.strategies.ExaminationResultSolution;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.utils.CustomFileReader.readFromStream;

@RestController
@RequestMapping("/api/examination/")
@CrossOrigin(origins = "http://localhost:3000")
public class ExaminationResultController implements CustomFileReader {

    private final ExaminationResultSolution solution;
    private final ExaminationPointService service;

    public ExaminationResultController(ExaminationResultSolution solution, ExaminationPointService service) {
        this.solution = solution;
        this.service = service;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> handleFileUpload(@Valid DataFileMetadata metadata, Principal principal) throws IOException, StorageException {

        List<String> examinationContent = readFromStream(metadata);
        List<ExaminationResult> results = new ArrayList<>();
        if (!examinationContent.isEmpty()) {
            solution.setMetadata(examinationContent);
            solution.create();
            results = new ArrayList<>(solution.getResults());
        }

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }

    @GetMapping("/examination_points/")
    public ResponseEntity<?> getExaminationPoints() {
        return service.getExaminationResults();
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> getExaminationResultsByFilename(@PathVariable String filename){
        return service.getExaminationResultsByFilename(filename);
    }
}
