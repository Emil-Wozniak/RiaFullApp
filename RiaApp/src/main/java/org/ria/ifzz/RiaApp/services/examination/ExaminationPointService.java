package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.ria.ifzz.RiaApp.repositories.results.ExaminationPointRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExaminationPointService extends FileExtractorImpl<ExaminationPoint> {

    private final ExaminationPointRepository examinationPointRepository;
    private Map<String,String> response = new HashMap<>();

    public ExaminationPointService(CountResultUtil countResultUtil, ExaminationPointRepository examinationPointRepository) {
        super(new ExaminationPoint(), countResultUtil);
        this.examinationPointRepository = examinationPointRepository;
    }

    /**
     * checks {@code standardPattern} fromResultPointService metadata and skips
     *
     * @param metadata      data from uploaded file
     * @param controlCurves Control Curve entities
     */
    public void create(List<String> metadata, List<ControlCurve> controlCurves) throws ControlCurveException {
        List<ExaminationPoint> examinationPoints = generateResults(controlCurves, metadata);
        examinationPointRepository.saveAll(examinationPoints);
    }

    public ResponseEntity<?> getExaminationResults() {
        List<ExaminationPoint> examinationPoints = (List<ExaminationPoint>) examinationPointRepository.findAll();
        return !examinationPoints.isEmpty() ? new ResponseEntity<>(examinationPoints, HttpStatus.FOUND) : new ResponseEntity<>(response.put("message", "No results available"), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getExaminationResultsByFilename(String filename) {
        List<ExaminationPoint> examinationPoints = examinationPointRepository.findAllByIdentifier(filename);
        return !examinationPoints.isEmpty() ?  new ResponseEntity<>(examinationPoints, HttpStatus.FOUND): new ResponseEntity<>(response.put("message", "No results available"), HttpStatus.NOT_FOUND);
    }
}
