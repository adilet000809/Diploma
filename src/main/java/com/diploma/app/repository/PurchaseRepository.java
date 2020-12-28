package com.diploma.app.repository;

import com.diploma.app.model.Purchase;
import com.diploma.app.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByCustomer(Users user);
    Purchase findByIdAndCustomer(Integer id, Users user);

}
