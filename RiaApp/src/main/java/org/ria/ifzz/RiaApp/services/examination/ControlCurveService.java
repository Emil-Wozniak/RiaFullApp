package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.repositories.results.ControlCurveRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ControlCurveService extends FileExtractorImpl<ControlCurve> implements CustomFileReader, FileExtractor {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final ControlCurveRepository controlCurveRepository;
    private final CountResultUtil countResultUtil;

    public ControlCurveService(ControlCurveRepository controlCurveRepository, CountResultUtil countResultUtil) {
        super(new ControlCurve(), countResultUtil);
        this.controlCurveRepository = controlCurveRepository;
        this.countResultUtil = countResultUtil;
    }

    /**
     * generate ControlCurve entities and returns them with metadata set to the attributes
     *
     * @param metadata lines of data from uploaded file
     * @return controlCurve entities with all needed data from file metadata
     */
    public List<ControlCurve> create(List<String> metadata) {
        List<ControlCurve> controlCurves = new ArrayList<>();
        return generateResults(controlCurves,metadata);
    }

    public ResponseEntity<?> findAll() {
        List<ControlCurve> controlCurves = (List<ControlCurve>) controlCurveRepository.findAll();
        return new ResponseEntity<>(controlCurves, HttpStatus.FOUND);
    }
}

