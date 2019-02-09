package org.ria.ifzz.RiaApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class CustomExceptionHandler {

    public ExceptionResponse exceptionResponse;
    public WebRequest webRequest;

    public CustomExceptionHandler(ExceptionResponse exceptionResponse, WebRequest webRequest) {
        this.exceptionResponse = exceptionResponse;
        this.webRequest = webRequest;
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(ExceptionMessageHandler exception, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
