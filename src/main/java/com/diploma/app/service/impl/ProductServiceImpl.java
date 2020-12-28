package com.diploma.app.service.impl;

import com.diploma.app.model.Product;
import com.diploma.app.model.Supermarket;
import com.diploma.app.repository.ProductRepository;
import com.diploma.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.getOne(id);
    }

    @Override
    public Optional<Product> findByBarcodeAndSupermarket(String barcode, Integer supermarketId) {
        return productRepository.findByBarcodeAndSupermarket_Id(barcode, supermarketId);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

}
