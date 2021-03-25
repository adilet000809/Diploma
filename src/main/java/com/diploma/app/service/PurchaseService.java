package com.diploma.app.service;

import com.diploma.app.model.Purchase;
import com.diploma.app.model.Users;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    Purchase save(Purchase purchase);
    List<Purchase> findByCustomer(Users user);
    Optional<Purchase> findByIdAndCustomer(Integer id, Users user);

    Optional<Purchase> findById(Integer id);
}
