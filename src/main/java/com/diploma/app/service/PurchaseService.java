package com.diploma.app.service;

import com.diploma.app.dto.CategoryExpenditure;
import com.diploma.app.model.Purchase;
import com.diploma.app.model.Users;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    Purchase save(Purchase purchase);
    List<Purchase> findByCustomer(Users user);
    Optional<Purchase> findByIdAndCustomer(Integer id, Users user);

    Optional<Purchase> findById(Integer id);
    List<CategoryExpenditure> findExpenditure(Integer purchaseId);
    List<Purchase> findAllByDateAndCustomer(Date from, Date to, Users user);
}
