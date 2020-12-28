package com.diploma.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "purchases")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Purchase", description = "Purchase class")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date")
    private Date date;

    @Column(name = "total")
    private Double total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users customer;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<PurchaseProduct> purchaseProducts = new HashSet<>();

    public void addProduct(Product product, int quantity) {
        PurchaseProduct purchaseProduct = new PurchaseProduct();
        purchaseProduct.setProduct(product);
        purchaseProduct.setPurchase(this);
        purchaseProduct.setQuantity(quantity);
        if (this.purchaseProducts == null) {
            this.purchaseProducts = new HashSet<>();
        }
        this.purchaseProducts.add(purchaseProduct);
        product.getPurchaseProducts().add(purchaseProduct);
    }

}
