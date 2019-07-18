package org.ria.ifzz.RiaApp.controllers;

import org.ria.ifzz.RiaApp.exception.StorageException;
import org.ria.ifzz.RiaApp.models.*;
import org.ria.ifzz.RiaApp.models.results.ExaminationResult;
import org.ria.ifzz.RiaApp.services.strategies.ExaminationResultSolution;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.utils.CustomFileReader.*;

@RestController
@RequestMapping("/api/examination/")
@CrossOrigin(origins = "http://localhost:3000")
public class ExaminationResultController implements CustomFileReader {

    private final ExaminationResultSolution solution;

    public ExaminationResultController(ExaminationResultSolution solution) {
        this.solution = solution;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> handleFileUpload(@Valid DataFileMetadata metadata, BindingResult result, Principal principal) throws IOException, StorageException {

        List<String> examinationContent = readFromStream(metadata);
        List<ExaminationResult> results = new ArrayList<>();
        if (!examinationContent.isEmpty()) {
            solution.setMetadata(examinationContent);
            solution.create();
            results = new ArrayList<>(solution.getResults());
        }

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }
}
