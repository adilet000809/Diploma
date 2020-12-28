package com.diploma.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "PasswordChangeDTO", description = "DTO for password change. Stores old and new passwords")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordChangeDto {

    private String oldPassword;
    private String newPassword;

}
