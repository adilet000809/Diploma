package com.diploma.app.service.impl;

import com.diploma.app.model.PurchaseProduct;
import com.diploma.app.repository.PurchaseProductRepository;
import com.diploma.app.service.PurchaseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseProductServiceImpl implements PurchaseProductService {

    private final PurchaseProductRepository purchaseProductRepository;

    @Autowired
    public PurchaseProductServiceImpl(PurchaseProductRepository purchaseProductRepository) {
        this.purchaseProductRepository = purchaseProductRepository;
    }

    @Override
    public PurchaseProduct save(PurchaseProduct purchaseProduct) {
        return purchaseProductRepository.save(purchaseProduct);
    }
}
