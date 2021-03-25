package com.diploma.app.service.impl;

import com.diploma.app.model.SupermarketProduct;
import com.diploma.app.repository.SupermarketProductRepository;
import com.diploma.app.service.SupermarketProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SupermarketProductServiceImpl implements SupermarketProductService {

    private final SupermarketProductRepository supermarketProductRepository;

    @Autowired
    public SupermarketProductServiceImpl(SupermarketProductRepository supermarketProductRepository) {
        this.supermarketProductRepository = supermarketProductRepository;
    }

    @Override
    public Optional<SupermarketProduct> findById(Integer id) {
        return supermarketProductRepository.findById(id);
    }

    @Override
    public SupermarketProduct save(SupermarketProduct supermarketProduct) {
        return supermarketProductRepository.save(supermarketProduct);
    }

}
