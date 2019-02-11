package org.ria.ifzz.RiaApp.exception;

import lombok.Data;

@Data
public class FileEntityExceptionResponse {
    private  String dataId;

    public FileEntityExceptionResponse(String dataId) {
        this.dataId = dataId;
    }
}
