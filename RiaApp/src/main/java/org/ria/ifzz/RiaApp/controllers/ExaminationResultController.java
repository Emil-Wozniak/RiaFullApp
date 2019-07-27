package org.ria.ifzz.RiaApp.controllers;

import org.jetbrains.annotations.NotNull;
import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.exception.GraphException;
import org.ria.ifzz.RiaApp.models.DataFileMetadata;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.ria.ifzz.RiaApp.repositories.results.ExaminationPointRepository;
import org.ria.ifzz.RiaApp.services.examination.ExaminationPointService;
import org.ria.ifzz.RiaApp.services.strategies.ExaminationResultSolution;
import org.ria.ifzz.RiaApp.utils.reader.CustomFileReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ria.ifzz.RiaApp.utils.reader.CustomFileReader.readFromStream;
import static org.ria.ifzz.RiaApp.utils.constants.DomainConstants.FILENAME_UNNECESSARY_PART;

@RestController
@RequestMapping("/api/examination/")
@CrossOrigin(origins = "http://localhost:3000")
public class ExaminationResultController implements CustomFileReader {

    private final ExaminationPointRepository repository;
    private final ExaminationResultSolution solution;
    private final ExaminationPointService service;
    private Map<String, String> response = new HashMap<>();

    public ExaminationResultController(ExaminationPointRepository repository, ExaminationResultSolution solution, ExaminationPointService service) {
        this.repository = repository;
        this.solution = solution;
        this.service = service;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> handleFileUpload(@Valid DataFileMetadata metadata, Principal principal) throws IOException, ControlCurveException, GraphException {
        List<String> examinationContent = readFromStream(metadata);

        try {
            List<ExaminationPoint> point = repository.findByProbeNumberAndIdentifierOrderByProbeNumber(25, getIdentifier(metadata));
            return (!point.isEmpty()) ? getNegativeResponse(metadata) : getSuccessResponse(examinationContent);
        } catch (ControlCurveException curveError) {
            return new ResponseEntity<>(curveError.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @NotNull
    private ResponseEntity<?> getNegativeResponse(@Valid DataFileMetadata metadata) {
        response.put("message", "File: " + getIdentifier(metadata) + " already uploaded");
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @NotNull
    private ResponseEntity<?> getSuccessResponse(List<String> examinationContent) throws ControlCurveException, GraphException {
        solution.setMetadata(examinationContent);
        solution.create();
        response.put("message", "Upload successful");
        return ResponseEntity.ok().body(response);
    }

    @NotNull
    private String getIdentifier(@Valid DataFileMetadata metadata) {
        return metadata.getContents().get().get(0).replace(FILENAME_UNNECESSARY_PART, "");
    }

    @GetMapping("/all/")
    public List<ExaminationPoint> getExaminationPoints() {
        return service.getExaminationResults();
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> getExaminationResultsByFilename(@PathVariable String filename) {
        return service.getExaminationResultsByFilename(filename);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<?> deleteExaminationPoint(@PathVariable String filename){
        return service.deleteExaminationPoint(filename);
    }
}
