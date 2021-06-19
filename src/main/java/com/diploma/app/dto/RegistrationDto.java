package com.diploma.app.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "RegistrationDTO", description = "DTO class for storing User info for registration purposes")
public class RegistrationDto {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;

}
