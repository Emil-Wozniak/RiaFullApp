package org.ria.ifzz.RiaApp.exceptions;

import lombok.Data;

@Data
public class ExceptionResponse {

    private String username;

    public ExceptionResponse(String username) {
        this.username = username;
    }
}
