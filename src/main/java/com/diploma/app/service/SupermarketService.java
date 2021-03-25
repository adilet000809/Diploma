package com.diploma.app.service;

import com.diploma.app.model.Supermarket;
import com.diploma.app.model.Users;

import java.util.List;
import java.util.Optional;

public interface SupermarketService {

    Supermarket save(Supermarket supermarket);
    Optional<Supermarket> findById(Integer id);
    List<Supermarket> findAll();
    List<Supermarket> findAllByCityId(Integer cityId);
    void deleteById(Integer id);

    void deleteSupermarketProduct(Supermarket supermarket, Integer id);
}
