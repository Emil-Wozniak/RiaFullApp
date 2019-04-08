package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.*;
import org.ria.ifzz.RiaApp.exception.FileEntityNotFoundException;
import org.ria.ifzz.RiaApp.repository.GraphCurveLinesRepository;
import org.ria.ifzz.RiaApp.repository.GraphCurveRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GraphCurveService {

    List<GraphCurveLines> graphCurveLinesList = new ArrayList<>();

    private final GraphCurveRepository graphCurveRepository;
    private final FileEntityService fileEntityService;
    private final CountResultUtil countResultUtil;
    private final FileUtils fileUtils;
    private final GraphCurveLinesRepository graphCurveLinesRepository;

    @Autowired
    public GraphCurveService(GraphCurveRepository graphCurveRepository, FileEntityService fileEntityService, CountResultUtil countResultUtil, FileUtils fileUtils, GraphCurveLinesRepository graphCurveLinesRepository) {
        this.graphCurveRepository = graphCurveRepository;
        this.fileEntityService = fileEntityService;
        this.countResultUtil = countResultUtil;
        this.fileUtils = fileUtils;
        this.graphCurveLinesRepository = graphCurveLinesRepository;
    }

    /**
     * //     * @param listX      logDose list
     * //     * @param listY      Logarithm Real Zero list
     *
     * @param graphCurve
     */
    public List<GraphCurveLines> setCoordinates(GraphCurve graphCurve, Backlog backlog) {
        List<Double> listX = countResultUtil.getLogDoseList();
        List<Double> listY = countResultUtil.getLogarithmRealZeroTable();
        try {
            graphCurveLinesList = new ArrayList<>();
            for (int i = 0; i < listX.size(); i++) {
                double x = listX.get(i);
                double y = listY.get(i);
                GraphCurveLines graphCurveLines = new GraphCurveLines();
                graphCurveLines.setX(x);
                graphCurveLines.setY(y);
                graphCurveLines.setDataId(graphCurve.getDataId());
                graphCurveLines.setFileName(graphCurve.getFileName());
                graphCurveLines.setBacklog(backlog);
                graphCurveLines.setGraphCurve(graphCurve);
                graphCurveLinesList.add(graphCurveLines);
            }
        } catch (Exception e) {
            System.out.println("setCoordinates(): " + e.getMessage() + " | Cause: " + e.getCause());
        }
        return graphCurveLinesList;
    }

    /**
     * @param file upload file
     * @return list of point for graphical curve, each point has set id(fileName) and set points x, y
     */
    public GraphCurve setGraphCurveFileName(FileModel file, FileEntity fileEntity, Backlog backlog) {
        GraphCurve graphCurve = new GraphCurve();
        try {
            String fileId = fileEntity.getDataId();
            graphCurve.setFileName(fileUtils.setFileName(file) + "_" + 0);
            graphCurve.setDataId(fileId);
            graphCurve.setBacklog(backlog);
            graphCurve.setR(countResultUtil.setCorrelation());
            graphCurve.setZeroBindingPercent(countResultUtil.setZeroBindingPercent());
        } catch (Exception e) {
            System.out.println("setGraphCurveFileName(): " + e.getMessage() + " | Cause: " + e.getCause());
        }
        return graphCurve;
    }

    public Iterable<GraphCurve> findBacklogByDataId(String dataId) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);
        return graphCurveRepository.findByDataIdOrderByFileName(dataId);
    }

    public GraphCurve findGraphCurveByDataId(String dataId, String fileName) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);

        GraphCurve graphCurve = graphCurveRepository.findByFileName(fileName);
        if (graphCurve == null) {
            throw new FileEntityNotFoundException("File with ID: '" + fileName + "' not found");
        }
        if (!graphCurve.getDataId().equals(dataId)) {
            throw new FileEntityNotFoundException("Graph '" + fileName + "' does not exist: '" + dataId);
        }
        return graphCurve;
    }

    public Optional<GraphCurveLines> findResultForCoordinatesByDataId(String dataId, String fileName, Long id) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);

        Optional<GraphCurveLines> graphCurveLines = graphCurveLinesRepository.findById(id);
        if (!graphCurveLines.isPresent()) {
            throw new FileEntityNotFoundException("File with ID: '" + fileName + "' not found");
        }
        return graphCurveLines;
    }
}
