package org.ria.ifzz.RiaApp.exception;

import lombok.Data;

@Data
public class UserNameAlreadyExistsResponse {

    private String username;

    public UserNameAlreadyExistsResponse(String username) {
        this.username = username;
    }
}

