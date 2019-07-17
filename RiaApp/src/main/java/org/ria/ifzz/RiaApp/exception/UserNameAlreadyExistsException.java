package org.ria.ifzz.RiaApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNameAlreadyExistsException extends Exception{

    public UserNameAlreadyExistsException(String s) {
        super(s);
    }
}

