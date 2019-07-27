package org.ria.ifzz.RiaApp.services.examination;

import org.ria.ifzz.RiaApp.exception.GraphException;
import org.ria.ifzz.RiaApp.models.graph.Graph;
import org.ria.ifzz.RiaApp.models.graph.GraphLine;
import org.ria.ifzz.RiaApp.repositories.results.GraphLineRepository;
import org.ria.ifzz.RiaApp.repositories.results.GraphRepository;
import org.ria.ifzz.RiaApp.utils.counter.Counter;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.ria.ifzz.RiaApp.utils.CustomFileReader.getStandardPattern;

@Service
public class GraphService implements CustomFileReader {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private List<GraphLine> graphLines = new ArrayList<>();
    private final Counter counter;
    private final GraphRepository graphRepository;
    private final GraphLineRepository graphLineRepository;

    public GraphService(Counter counter, GraphRepository graphRepository, GraphLineRepository graphLineRepository) {
        this.counter = counter;
        this.graphRepository = graphRepository;
        this.graphLineRepository = graphLineRepository;
    }

    public void create(List<String> metadata) throws GraphException {
        double correlation = 0.0, zeroBindingPercentage = 0.0, regressionParameterB = 0.0;
        try {
            correlation = counter.setCorrelation(getStandardPattern(metadata));
            zeroBindingPercentage = counter.setZeroBindingPercent();
            regressionParameterB = counter.getRegressionParameterB();

            String filename = metadata.get(0).trim();
            Graph graph = new Graph(filename, correlation, zeroBindingPercentage, regressionParameterB);

            graphLines = createGraphLines(filename, metadata, graph);
            graph.setGraphLines(graphLines);
            graphRepository.save(graph);
            graphLineRepository.saveAll(graphLines);
        } catch (GraphException graphError) {
            throw new GraphException("create method failed because: ", correlation, zeroBindingPercentage);
        }
    }

    private List<GraphLine> createGraphLines(String filename, List<String> metadata, Graph graph) {
        List<Double> listX = counter.getLogDoseList();
        List<Double> listY = counter.getLogarithmRealZeroTable();
        List<Double> patternPoints = getStandardPattern(metadata);
        try {
            graphLines = new ArrayList<>();
            for (int i = 0; i < listX.size(); i++) {
                double x = listX.get(i);
                double y = listY.get(i);
                double patternPoint = patternPoints.get(i);
                GraphLine graphCurveLine = new GraphLine(filename, x, y, patternPoint, graph);
                graphLines.add(graphCurveLine);
            }
        } catch (Exception error) {
            LOGGER.error(".setGraphCurve() msg:" + error.getMessage() + " and cause: " + error.getCause());
        }
        LOGGER.info("Graph created with: " + graphLines.size() + " lines");
        return graphLines;
    }

    public ResponseEntity<?> findAll() {
        List<Graph> graphs = (List<Graph>) graphRepository.findAll();
        return new ResponseEntity<>(graphs, HttpStatus.FOUND);
    }

    public ResponseEntity<?> findAllLines() {
        List<GraphLine> graphLines = (List<GraphLine>) graphLineRepository.findAll();
        return new ResponseEntity<>(graphLines, HttpStatus.FOUND);
    }

    public List<Graph> getGraph(String identifier) {
        return graphRepository.findAllByIdentifier(identifier);
    }

    public List<GraphLine> getGraphLines(String identifier) {
        return graphLineRepository.findAllByFilename(identifier);
    }
}
