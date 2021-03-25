package com.diploma.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "EmailChangeDTO", description = "DTO for email change. Stores password and new email")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailChangeDto {

    private String email;
    private String password;

}
