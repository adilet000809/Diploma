package com.diploma.app.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "ProductDTO", description = "DTO for Product model class")
public class ProductDto {

    private Integer id;
    private String name;
    private String barcode;
    private Double price;
    private Double discount;
    private Integer categoryId;
    private Integer supermarketId;

}
