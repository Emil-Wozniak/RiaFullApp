package org.ria.ifzz.RiaApp.exception;

import lombok.Data;

@Data
public class StorageExceptionResponse {

    private String filename;

    public StorageExceptionResponse(String filename) {
        this.filename = filename;
    }
}
