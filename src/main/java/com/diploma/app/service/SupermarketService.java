package com.diploma.app.service;

import com.diploma.app.model.Supermarket;

import java.util.List;
import java.util.Optional;

public interface SupermarketService {

    Supermarket save(Supermarket supermarket);
    Optional<Supermarket> findById(Integer id);
    List<Supermarket> findAll();
    void deleteById(Integer id);

}
