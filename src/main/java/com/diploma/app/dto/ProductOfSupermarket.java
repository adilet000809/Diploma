package com.diploma.app.dto;

import com.diploma.app.model.Category;
import com.diploma.app.model.Supermarket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOfSupermarket {

    private Integer id;
    private String name;
    private String barcode;

    private Category category;

    private Supermarket supermarket;
    private double price;
    private double discount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductOfSupermarket)) return false;
        ProductOfSupermarket that = (ProductOfSupermarket) o;
        return Double.compare(that.getPrice(), getPrice()) == 0 && Double.compare(that.getDiscount(), getDiscount()) == 0 && Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getBarcode(), that.getBarcode()) && Objects.equals(getCategory(), that.getCategory()) && Objects.equals(getSupermarket(), that.getSupermarket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getBarcode(), getCategory(), getSupermarket(), getPrice(), getDiscount());
    }
}
