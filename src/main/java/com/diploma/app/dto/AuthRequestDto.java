package com.diploma.app.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "AuthDTO", description = "DTO for authorization. Stores username and password")
public class AuthRequestDto {
    private String userName;
    private String password;
}
