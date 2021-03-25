package com.diploma.app.repository;

import com.diploma.app.dto.ProductOfSupermarket;
import com.diploma.app.model.Product;
import com.diploma.app.model.PurchaseProduct;
import com.diploma.app.model.SupermarketProduct;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @NotNull
    List<Product> findAll();

    @NotNull
    Optional<Product> findById(@NotNull Integer id);

    Optional<Product> findByBarcode(String barcode);

    @Query("SELECT " +
            "new com.diploma.app.model.SupermarketProduct(sp.id, sp.price, sp.discount, sp.product)" +
            "FROM Product p JOIN p.supermarketProducts sp WHERE sp.product.barcode=:barcode AND sp.supermarket.id=:supermarketId")
    Optional<SupermarketProduct> findProductUsingBarcodeAndSupermarketId(String barcode, Integer supermarketId);

    @Query("SELECT " +
            "new com.diploma.app.model.SupermarketProduct(sp.id, sp.price, sp.discount, sp.product, sp.supermarket)" +
            "FROM SupermarketProduct sp WHERE sp.supermarket.city.id=:cityId AND sp.price<:price AND sp.product.barcode=:barcode ORDER BY sp.price ASC")
    List<SupermarketProduct> findCheaperProducts(Integer cityId, Double price, String barcode);

    @Query("SELECT sp.product " +
            "FROM SupermarketProduct sp WHERE sp.id=:supermarketProductId")
    Optional<Product> findProductUsingSupermarketProductId(Integer supermarketProductId);

    @Query("SELECT " +
            "new com.diploma.app.dto.ProductOfSupermarket(p.id, p.name, p.barcode, p.category, sp.supermarket, sp.price, sp.discount)" +
            "FROM Product p, SupermarketProduct sp WHERE sp.id=:id")
    Optional<ProductOfSupermarket> findProductUsingId(Integer id);

    @Query("SELECT " +
            "new com.diploma.app.model.PurchaseProduct(sp.price, p)" +
            "FROM Product p, SupermarketProduct sp WHERE p.id=sp.product.id AND sp.id=:supermarketProductId")
    Optional<PurchaseProduct> findPurchaseProductUsingSupermarketProductId(Integer supermarketProductId);


    void deleteById(@NotNull Integer id);
}
