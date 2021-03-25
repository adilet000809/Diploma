package com.diploma.app.service;


import com.diploma.app.dto.ProductOfSupermarket;
import com.diploma.app.model.Product;
import com.diploma.app.model.PurchaseProduct;
import com.diploma.app.model.SupermarketProduct;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product save(Product product);
    Product findById(Integer id);
    Optional<SupermarketProduct> findByBarcodeAndSupermarket(String barcode, Integer supermarketId);
    Optional<PurchaseProduct> findPurchaseProductBySupermarketProductId(Integer supermarketProductId);
    List<SupermarketProduct> findCheaperOptions(Integer cityId, Double price, String barcode);
    List<Product> findAll();
    Optional<Product> findByBarcode(String barcode);
    Optional<Product> findBySupermarketProductId(Integer supermarketProductId);
    Optional<ProductOfSupermarket> findUsingId(Integer id);
    void deleteById(Integer id);

    void deleteSupermarketProduct(Product product, Integer id);
}
