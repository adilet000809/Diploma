package com.diploma.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "supermarket_products")
@NoArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class SupermarketProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount")
    private Double discount = 0.0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="supermarket_id")
    private Supermarket supermarket;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseProduct> purchaseProducts;

    public SupermarketProduct(Integer id, Double price, Double discount, Product product) {
        this.id = id;
        this.price = price;
        this.discount = discount;
        this.product = product;
    }

    public SupermarketProduct(Integer id, Double price, Double discount, Product product, Supermarket supermarket) {
        this.id = id;
        this.price = price;
        this.discount = discount;
        this.product = product;
        this.supermarket = supermarket;
    }

    public void addPurchase(Purchase purchase, int quantity) {
        PurchaseProduct purchaseProduct = new PurchaseProduct();
        purchaseProduct.setPurchase(purchase);
        purchaseProduct.setQuantity(quantity);
        if (this.purchaseProducts == null) {
            this.purchaseProducts = new HashSet<>();
        }
        this.purchaseProducts.add(purchaseProduct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupermarketProduct)) return false;
        SupermarketProduct that = (SupermarketProduct) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getDiscount(), that.getDiscount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrice(), getDiscount());
    }

    @Override
    public String toString() {
        return "SupermarketProduct{" +
                "id=" + id +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }
}
