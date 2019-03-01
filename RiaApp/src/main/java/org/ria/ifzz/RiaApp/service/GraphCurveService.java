package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.GraphCurve;
import org.ria.ifzz.RiaApp.repository.GraphCurveRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.ria.ifzz.RiaApp.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GraphCurveService {

    List<GraphCurve> graphCurves = new ArrayList<>();

    private final GraphCurveRepository graphCurveRepository;
    private final FileEntityService fileEntityService;
    private final CountResultUtil countResultUtil;
    private final FileUtils fileUtils;

    @Autowired
    public GraphCurveService(GraphCurveRepository graphCurveRepository, FileEntityService fileEntityService, CountResultUtil countResultUtil, FileUtils fileUtils) {
        this.graphCurveRepository = graphCurveRepository;
        this.fileEntityService = fileEntityService;
        this.countResultUtil = countResultUtil;
        this.fileUtils = fileUtils;
    }

    /**
     * @param listX logDose list
     * @param listY Logarithm Real Zero list
     */
    public void setCoordinates(List<Double> listX, List<Double> listY) {
        graphCurves = new ArrayList<>();
        for (int i = 0; i < listX.size(); i++) {
            double x = listX.get(i);
            double y = listY.get(i);
            GraphCurve graphCurve = new GraphCurve();
            graphCurve.setX(x);
            graphCurve.setY(y);
            graphCurves.add(graphCurve);
        }
        graphCurveRepository.saveAll(graphCurves);
    }

    /**
     * @param file upload file
     * @return list of point for graphical curve, each point has set id(fileName) and set points x, y
     */
    public List<GraphCurve> setGraphCurveFileName(MultipartFile file) {
        List<GraphCurve> graphCurvesNamed = new ArrayList<>();
        GraphCurve graphCurve;
        setCoordinates(countResultUtil.getLogDoseList(), countResultUtil.getLogarithmRealZeroTable());
        for (int i = 0; i < graphCurves.size(); i++) {
            graphCurve = graphCurves.get(i);
            graphCurve.setFileName(fileUtils.setFileName(file) + "_" + i);
            graphCurvesNamed.add(graphCurve);
        }
        graphCurveRepository.saveAll(graphCurvesNamed);
        return graphCurvesNamed;
    }

    public Iterable<GraphCurve> findBacklogByDataId(String dataId) throws FileNotFoundException {
        fileEntityService.findFileEntityByDataId(dataId);
        return null;
    }
}
