package com.allioc.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginModel {

    @NotBlank
    private String emailId;

    @NotBlank
    private String password;
}
