package com.diploma.app.service;


import com.diploma.app.model.Product;
import com.diploma.app.model.Supermarket;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product save(Product product);
    Product findById(Integer id);
    Optional<Product> findByBarcodeAndSupermarket(String barcode, Integer supermarketId);
    List<Product> findAll();
    void deleteById(Integer id);

}
