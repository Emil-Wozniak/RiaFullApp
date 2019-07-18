package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.models.graph.Graph;
import org.ria.ifzz.RiaApp.models.graph.GraphLine;
import org.ria.ifzz.RiaApp.repositories.results.GraphLineRepository;
import org.ria.ifzz.RiaApp.repositories.results.GraphRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static org.ria.ifzz.RiaApp.models.pattern.HormonesPattern.CORTISOL_PATTERN;

@Service
public class GraphService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private List<GraphLine> graphLines = new ArrayList<>();
    private final CountResultUtil countResultUtil;
    private final GraphRepository graphRepository;
    private final GraphLineRepository graphLineRepository;

    public GraphService(CountResultUtil countResultUtil, GraphRepository graphRepository, GraphLineRepository graphLineRepository) {
        this.countResultUtil = countResultUtil;
        this.graphRepository = graphRepository;
        this.graphLineRepository = graphLineRepository;
    }


    public Graph create(List<String> metadata) {
        return createGraph(metadata);
    }

    private Graph createGraph(List<String> metadata) {
        double correlation = countResultUtil.setCorrelation(setStandardPattern(metadata));
        double zeroBindingPercentage = countResultUtil.setZeroBindingPercent();
        double regressionParameterB = countResultUtil.getRegressionParameterB();

        String filename = metadata.get(0);
        String pattern = metadata.get(1);
        Graph graph = new Graph(filename, pattern, correlation, zeroBindingPercentage, regressionParameterB);
        LOGGER.info("Create graph: " + graph.toString());
        graphLines = createGraphLines(filename, metadata, graph);
        graph.setGraphLines(graphLines);
        graphRepository.save(graph);
        graphLineRepository.saveAll(graphLines);
        return graph;
    }

    private List<GraphLine> createGraphLines(String filename, List<String> metadata, Graph graph) {
        List<Double> listX = countResultUtil.getLogDoseList();
        List<Double> listY = countResultUtil.getLogarithmRealZeroTable();
        List<Double> patternPoints = setStandardPattern(metadata);
        try {
            graphLines = new ArrayList<>();
            for (int i = 0; i < listX.size(); i++) {
                double x = listX.get(i);
                double y = listY.get(i);
                double patternPoint = patternPoints.get(i);
                List<Double> meterReadingPg = countResultUtil.countMeterReading();
                GraphLine graphCurveLine = new GraphLine(filename, metadata.get(0), x, y, patternPoint, meterReadingPg.get(i), graph);
                graphLines.add(graphCurveLine);
            }
        } catch (Exception error) {
            LOGGER.error(".setGraphCurve() msg:" + error.getMessage() + " and cause: " + error.getCause());
        }
        LOGGER.info("Graph created with: " + graphLines.size() + " lines");
        return graphLines;
    }


    private List<Double> setStandardPattern(List<String> metadata) {
        List<Double> hormonePattern = new ArrayList<>();
        if (metadata.get(1).equals("KORTYZOL_5_MIN")) {
            LOGGER.info("Pattern detected: " + metadata.get(1));
            hormonePattern = DoubleStream.of(CORTISOL_PATTERN).boxed().collect(Collectors.toCollection(ArrayList::new));
        }
        return hormonePattern;
    }
}
