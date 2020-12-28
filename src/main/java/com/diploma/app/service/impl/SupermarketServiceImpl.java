package com.diploma.app.service.impl;

import com.diploma.app.model.Supermarket;
import com.diploma.app.repository.SupermarketRepository;
import com.diploma.app.service.SupermarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupermarketServiceImpl implements SupermarketService {

    private final SupermarketRepository supermarketRepository;

    @Autowired
    public SupermarketServiceImpl(SupermarketRepository supermarketRepository) {
        this.supermarketRepository = supermarketRepository;
    }

    @Override
    public Supermarket save(Supermarket supermarket) {
        return supermarketRepository.save(supermarket);
    }

    @Override
    public Optional<Supermarket> findById(Integer id) {
        return supermarketRepository.findById(id);
    }

    @Override
    public List<Supermarket> findAll() {
        return supermarketRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        supermarketRepository.deleteById(id);
    }
}
