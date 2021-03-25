package com.diploma.app.service.impl;

import com.diploma.app.model.City;
import com.diploma.app.repository.CityRepository;
import com.diploma.app.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) {
            LOGGER.trace("City with id: " + id + "found.");
        } else {
            LOGGER.error("City with id: " + id + "NOT found.");
        }
        return cityRepository.findById(id);
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        cityRepository.deleteById(id);
        LOGGER.trace("City with id: " + id + "deleted.");
    }
}
