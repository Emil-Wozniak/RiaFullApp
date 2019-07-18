package org.ria.ifzz.RiaApp.services.strategies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ria.ifzz.RiaApp.models.graph.Graph;
import org.ria.ifzz.RiaApp.models.results.ControlCurve;
import org.ria.ifzz.RiaApp.models.results.ExaminationPoint;
import org.ria.ifzz.RiaApp.models.results.ExaminationResult;
import org.ria.ifzz.RiaApp.services.examination.ControlCurveService;
import org.ria.ifzz.RiaApp.services.examination.GraphService;
import org.ria.ifzz.RiaApp.services.examination.ResultPointService;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@ToString
@Service
public class ExaminationResultSolution extends ExaminationResultStrategyImpl {

    @Setter
    private List<String> metadata;
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final CountResultUtil countResultUtil;
    private final ControlCurveService controlCurveService;
    private final GraphService graphService;
    private final ResultPointService resultPointService;
    @Getter
    private List<ExaminationResult> results = new ArrayList<>();
    private List<ControlCurve> controlCurvePoints = new ArrayList<>();
    private List<ExaminationPoint> probeExaminations = new ArrayList<>();
    private Graph graph;

    public ExaminationResultSolution(CountResultUtil countResultUtil,
                                     ControlCurveService controlCurveService,
                                     GraphService graphService,
                                     ResultPointService resultPointService) {
        this.countResultUtil = countResultUtil;
        this.controlCurveService = controlCurveService;
        this.graphService = graphService;
        this.resultPointService = resultPointService;
    }


    @Override
    void start() {
        if (metadata.isEmpty()) {
            LOGGER.warn("Reading file:");
        } else {
            stop();
        }
    }

    @Override
    boolean isControlCurve() {
        if (metadata.size() >= 1) {
            controlCurvePoints = controlCurveService.create(metadata);
            if (!controlCurvePoints.isEmpty()) {
                results.addAll(controlCurvePoints);
            }
        }
        return false;
    }

    @Override
    boolean isResultPoint() {
        if (metadata.size() > 25)
            probeExaminations = resultPointService.create(metadata, controlCurvePoints);
        if (!probeExaminations.isEmpty()) {
            results.addAll(probeExaminations);
        }
        return false;
    }

    @Override
    boolean isGraphPoint() {
        if (!controlCurvePoints.isEmpty()) {
            graph = graphService.create(metadata);
        }
        return false;
    }

    @Override
    boolean stop() {
        LOGGER.warn("Stop");
        return false;
    }
}
