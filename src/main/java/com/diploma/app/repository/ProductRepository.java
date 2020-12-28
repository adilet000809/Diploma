package com.diploma.app.repository;

import com.diploma.app.model.Product;
import com.diploma.app.model.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAll();
    Optional<Product> findByBarcodeAndSupermarket_Id(String barcode, Integer supermarketId);
    void deleteById(Integer id);

}
