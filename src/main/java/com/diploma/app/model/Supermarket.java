package com.diploma.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "supermarkets")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Supermarket", description = "Supermarket class")
@NoArgsConstructor
public class Supermarket extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "supermarket", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
//    @JoinTable(name = "supermarket_products",
//            joinColumns = { @JoinColumn(name = "supermarket_id") },
//            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonIgnore
    private Set<SupermarketProduct> supermarketProducts;

    public Supermarket(String name, City city) {
        this.name = name;
        this.city = city;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_supermarkets",
            joinColumns = { @JoinColumn(name = "supermarket_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
    @JsonIgnore
    private Set<Users> managers;

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

    public void clearManagers() {
        managers.clear();
    }

    public void clearProducts() {
        supermarketProducts.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supermarket)) return false;
        if (!super.equals(o)) return false;
        Supermarket that = (Supermarket) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getCity(), that.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getCity());
    }
}
