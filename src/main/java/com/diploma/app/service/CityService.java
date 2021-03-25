package com.diploma.app.service;

import com.diploma.app.model.City;

import java.util.List;
import java.util.Optional;

public interface CityService {

    City save(City city);
    Optional<City> findById(Integer id);
    List<City> findAll();
    void deleteById(Integer id);

}
