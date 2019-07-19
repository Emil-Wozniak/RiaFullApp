package org.ria.ifzz.RiaApp.services.strategies;

public abstract class ExaminationResultStrategyImpl implements ExaminationResultStrategy{

    @Override
    public void create() {
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
    abstract boolean isControlCurve();
    abstract boolean isResultPoint();
    abstract boolean isGraphPoint();
    abstract boolean stop();
}
