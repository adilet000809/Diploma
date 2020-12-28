package com.diploma.app.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "ForgotPasswordDTO", description = "DTO for password reset email. Stores user's email")
public class ForgotPasswordDto {

    private String email;

}
