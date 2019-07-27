package org.ria.ifzz.RiaApp.services.strategies;

import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.exception.GraphException;

public abstract class ExaminationResultStrategyImpl implements ExaminationResultStrategy{

    @Override
    public void create() throws ControlCurveException, GraphException {
        start();
        while(true){
            if (!isControlCurve()) break;
        }
        while(true){
            if (!isResultPoint()) break;
        }
        while (true){
            if (!isGraphPoint())break;
        }
        while(true) {
            stop();
            break;
        }
    }

    abstract void start();
    abstract boolean isControlCurve() throws ControlCurveException;
    abstract boolean isResultPoint() throws ControlCurveException;
    abstract boolean isGraphPoint() throws GraphException;
    abstract boolean stop();
}
