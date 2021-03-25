package com.diploma.app.service.impl;

import com.diploma.app.dto.ProductOfSupermarket;
import com.diploma.app.model.Product;
import com.diploma.app.model.PurchaseProduct;
import com.diploma.app.model.SupermarketProduct;
import com.diploma.app.repository.ProductRepository;
import com.diploma.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    public Optional<SupermarketProduct> findByBarcodeAndSupermarket(String barcode, Integer supermarketId) {
        return productRepository.findProductUsingBarcodeAndSupermarketId(barcode, supermarketId);
    }

    @Override
    public Optional<PurchaseProduct> findPurchaseProductBySupermarketProductId(Integer supermarketProductId) {
        return productRepository.findPurchaseProductUsingSupermarketProductId(supermarketProductId);
    }

    @Override
    public List<SupermarketProduct> findCheaperOptions(Integer cityId, Double price, String barcode) {
        return productRepository.findCheaperProducts(cityId, price, barcode);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    @Override
    public Optional<Product> findBySupermarketProductId(Integer supermarketProductId) {
        return productRepository.findProductUsingSupermarketProductId(supermarketProductId);
    }

    @Override
    public Optional<ProductOfSupermarket> findUsingId(Integer id) {
        return productRepository.findProductUsingId(id);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public void deleteSupermarketProduct(Product product, Integer id) {
        //product.removeProduct(id);
        productRepository.save(product);
    }

}
