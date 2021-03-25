package com.diploma.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Product", description = "Product class")
public class Product extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "barcode")
    private String barcode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private Set<SupermarketProduct> supermarketProducts;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<PurchaseProduct> purchaseProducts;

    public void addOrUpdateProduct(SupermarketProduct supermarketProduct) {
        if (supermarketProducts == null) {
            supermarketProducts = new HashSet<>();
        }
        if (supermarketProduct.getId() != null) {
            supermarketProducts.removeIf(sp -> sp.getId().equals(supermarketProduct.getId()));
        }
        supermarketProducts.add(supermarketProduct);
    }

    public void removeProduct(Integer id) {
        if (supermarketProducts == null) {
            supermarketProducts = new HashSet<>();
        }
        supermarketProducts.removeIf(sp -> sp.getId().equals(id));
    }

    public void addPurchaseProduct(PurchaseProduct purchaseProduct) {
        if (purchaseProducts == null) {
            purchaseProducts = new HashSet<>();
        }
        purchaseProducts.add(purchaseProduct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(getName(), product.getName()) && Objects.equals(getBarcode(), product.getBarcode()) && Objects.equals(getCategory(), product.getCategory()) && Objects.equals(getSupermarketProducts(), product.getSupermarketProducts()) && Objects.equals(getPurchaseProducts(), product.getPurchaseProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getBarcode(), getCategory(), getSupermarketProducts(), getPurchaseProducts());
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}
