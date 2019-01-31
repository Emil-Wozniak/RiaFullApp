package org.ria.ifzz.RiaApp.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username can't be black")
    private String username;
    @NotBlank(message = "Username can't be black")
    private String password;

}
