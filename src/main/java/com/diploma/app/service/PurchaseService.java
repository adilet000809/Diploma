package com.diploma.app.service;

import com.diploma.app.model.Purchase;
import com.diploma.app.model.Users;

import java.util.List;

public interface PurchaseService {

    Purchase save(Purchase purchase);
    List<Purchase> findByCustomer(Users user);
    Purchase findByIdAndCustomer(Integer id, Users user);

}
