package org.ria.ifzz.RiaApp.exception;

import lombok.Data;

@Data
public class StorageExceptionResponse {

    private String message;

    public StorageExceptionResponse(String message) {
        this.message = message;
    }
}
