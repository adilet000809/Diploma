package com.diploma.app.repository;

import com.diploma.app.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findById(Integer id);
    List<City> findAll();
}
