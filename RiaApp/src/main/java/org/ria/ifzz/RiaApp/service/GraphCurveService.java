package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.GraphCurve;
import org.ria.ifzz.RiaApp.repository.GraphCurveRepository;
import org.ria.ifzz.RiaApp.utils.CountResultUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraphCurveService {

    private final CountResultUtil countResultUtil;
    private final GraphCurveRepository graphCurveRepository;

    public GraphCurveService(CountResultUtil countResultUtil, GraphCurveRepository graphCurveRepository) {
        this.countResultUtil = countResultUtil;
        this.graphCurveRepository = graphCurveRepository;
    }

    public void setCoordinates(List<Double> listX, List<Double> listY){

        List<GraphCurve> graphCurves = new ArrayList<>();
        for (int i =0; i<listX.size();i++) {
            double x = listX.get(i);
            double y = listY.get(i);
            GraphCurve graphCurve = new GraphCurve();
            graphCurve.setX(x);
            graphCurve.setY(y);
            graphCurves.add(graphCurve);
        }
        graphCurveRepository.saveAll(graphCurves);
    }
}
