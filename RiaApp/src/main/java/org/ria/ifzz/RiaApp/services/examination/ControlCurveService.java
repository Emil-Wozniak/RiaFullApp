package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.repositories.results.ControlCurveRepository;
import org.ria.ifzz.RiaApp.utils.counter.Counter;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ControlCurveService extends FileExtractorImpl<ControlCurve> implements CustomFileReader, FileExtractor {

    private final ControlCurveRepository controlCurveRepository;

    public ControlCurveService(ControlCurveRepository controlCurveRepository, Counter counter) {
        super(new ControlCurve(), counter);
        this.controlCurveRepository = controlCurveRepository;
    }

    /**
     * generate ControlCurve entities and returns them with metadata set to the attributes
     *
     * @param metadata lines of data from uploaded file
     * @return controlCurve entities with all needed data from file metadata
     */
    public List<ControlCurve> create(List<String> metadata) throws ControlCurveException {
        List<ControlCurve> controlCurves = new ArrayList<>();
        controlCurves = generateResults(controlCurves, metadata);
        controlCurveRepository.saveAll(controlCurves);
        return controlCurves;
    }

    public ResponseEntity<?> findAll() {
        List<ControlCurve> controlCurves = (List<ControlCurve>) controlCurveRepository.findAll();
        return new ResponseEntity<>(controlCurves, HttpStatus.FOUND);
    }

    public List<ControlCurve> getControlCurves(String identifier) {
        return controlCurveRepository.findAllByIdentifier(identifier);
    }
}

