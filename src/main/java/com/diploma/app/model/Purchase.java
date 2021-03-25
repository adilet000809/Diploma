package com.diploma.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.*;

@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name = "purchase_only"),
        @NamedEntityGraph(name = "purchase_with_products", attributeNodes = @NamedAttributeNode("purchaseProducts"))
})
@Table(name = "purchases")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Purchase", description = "Purchase class")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Column(name = "date")
    private Date date = new Date();

    @Column(name = "total")
    private Double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users customer;

    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<PurchaseProduct> purchaseProducts;

    public void addProduct(PurchaseProduct purchaseProduct) {
        if (this.purchaseProducts == null) {
            this.purchaseProducts = new HashSet<>();
        }
        this.purchaseProducts.add(purchaseProduct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(getId(), purchase.getId()) && Objects.equals(getDate(), purchase.getDate()) && Objects.equals(getTotal(), purchase.getTotal()) && Objects.equals(getCustomer(), purchase.getCustomer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getTotal(), getCustomer());
    }

}
