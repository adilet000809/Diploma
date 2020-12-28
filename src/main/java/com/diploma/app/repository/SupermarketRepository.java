package com.diploma.app.repository;

import com.diploma.app.model.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupermarketRepository extends JpaRepository<Supermarket, Integer> {

    Optional<Supermarket> findById(Integer id);
    List<Supermarket> findAll();
    void deleteById(Integer id);

}
