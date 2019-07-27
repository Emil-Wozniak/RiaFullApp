package org.ria.ifzz.RiaApp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphException extends RuntimeException {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String prop_message;

    private void getProp_message(Double correlation, Double zeroBindingPercentage) {
        if (correlation == null) {
            LOGGER.error(this.prop_message + "Correlation is null");
        } else if (zeroBindingPercentage == 0.0) {
            LOGGER.error(this.prop_message + "zero is 0");
        } else {
            LOGGER.error(this.prop_message + "regressionParameterB is 0");
        }
    }

    public GraphException(String prop_message, double correlation, double zeroBindingPercentage) {
        super(prop_message);
        this.prop_message = prop_message;
        getProp_message(correlation, zeroBindingPercentage);
    }

}
