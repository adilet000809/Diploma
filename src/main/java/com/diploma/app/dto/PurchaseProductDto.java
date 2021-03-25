package com.diploma.app.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "PurchaseProductDTO", description = "DTO for PurchaseProduct model class")
public class PurchaseProductDto {

    private Integer supermarketProductId;
    private Integer quantity;

}
