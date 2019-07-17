package org.ria.ifzz.RiaApp.exception;

import lombok.Getter;

@Getter
class StorageExceptionResponse {

    private String message;

    StorageExceptionResponse(String message) {
        this.message = message;
    }
}
