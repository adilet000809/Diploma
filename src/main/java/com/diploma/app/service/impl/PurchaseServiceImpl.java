package com.diploma.app.service.impl;

import com.diploma.app.model.Purchase;
import com.diploma.app.model.Users;
import com.diploma.app.repository.PurchaseRepository;
import com.diploma.app.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> findByCustomer(Users user) {
        return purchaseRepository.findAllByCustomer(user);
    }

    @Override
    public Purchase findByIdAndCustomer(Integer id, Users user) {
        return purchaseRepository.findByIdAndCustomer(id, user);
    }
}
