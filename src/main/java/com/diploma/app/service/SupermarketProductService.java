package com.diploma.app.service;

import com.diploma.app.model.SupermarketProduct;

import java.util.Optional;

public interface SupermarketProductService {

    Optional<SupermarketProduct> findById(Integer id);

    SupermarketProduct save(SupermarketProduct supermarketProduct);
}
