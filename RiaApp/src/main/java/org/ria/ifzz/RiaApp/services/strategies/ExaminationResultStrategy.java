package org.ria.ifzz.RiaApp.services.strategies;

import org.ria.ifzz.RiaApp.exception.ControlCurveException;
import org.ria.ifzz.RiaApp.exception.GraphException;

public interface ExaminationResultStrategy {
    void create() throws ControlCurveException, GraphException;
}
