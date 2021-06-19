package com.diploma.app.service.impl;

import com.diploma.app.dto.CategoryExpenditure;
import com.diploma.app.model.Purchase;
import com.diploma.app.model.Users;
import com.diploma.app.repository.PurchaseRepository;
import com.diploma.app.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public Optional<Purchase> findByIdAndCustomer(Integer id, Users user) {
        return purchaseRepository.findByIdAndCustomer(id, user);
    }

    @Override
    public Optional<Purchase> findById(Integer id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public List<CategoryExpenditure> findExpenditure(Integer purchaseId) {
        return purchaseRepository.findExpenditure(purchaseId);
    }

    @Override
    public List<Purchase> findAllByDateAndCustomer(Date from, Date to, Users user) {
        return purchaseRepository.findAllByDateBetweenAndCustomer(from, to, user);
    }
}
