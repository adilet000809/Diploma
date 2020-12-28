package com.diploma.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "PasswordResetDTO", description = "DTO for password reset. Stores token and new passwords")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordResetDto {

    private String email;
    private String newPassword;
    private String token;

}
