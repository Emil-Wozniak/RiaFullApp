package org.ria.ifzz.RiaApp.models.responces;

import java.util.Map;

public class SuccessResponse {

    private Map<String, String> response;

    public SuccessResponse(Map<String, String> response) {
        this.response = response;
    }
}
