package com.diploma.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "purchases_products")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "PurchaseProduct", description = "Bridge entity for storing quantity of product in purchase")
public class PurchaseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique=true, nullable = false)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="purchase_id")
    @JsonIgnore
    private Purchase purchase;

    public PurchaseProduct() {
    }

    public PurchaseProduct(double price, Product product) {
        this.price = price;
        this.product = product;
    }

    public PurchaseProduct(double price, Integer quantity, Product product, Purchase purchase) {
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.purchase = purchase;
    }

    public PurchaseProduct(Integer id, Integer quantity, Product product, Purchase purchase) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.purchase = purchase;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseProduct)) return false;
        PurchaseProduct that = (PurchaseProduct) o;
        return Double.compare(that.getPrice(), getPrice()) == 0 && Objects.equals(getId(), that.getId()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuantity(), getPrice());
    }
}
