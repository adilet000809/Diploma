package com.diploma.app.controller.city;

import com.diploma.app.dto.CityDto;
import com.diploma.app.model.City;
import com.diploma.app.service.CityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("cities")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of all cities")})
    public List<City> getAllCities() {
        return cityService.findAll();
    }

    @GetMapping("cities/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "City object"),
            @ApiResponse(code = 404, message = "City not found")})
    public ResponseEntity<City> getCity(@PathVariable Integer id) {
        Optional<City> city = cityService.findById(id);
        return city.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("admin/cities/add")
    @ApiOperation(value = "Add city", consumes = "City object", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "City created successfully")})
    public ResponseEntity<Map<String, String>> addCategory(@RequestBody CityDto city) {
        Map<String, String> response = new HashMap<>();
        cityService.save(new City(city.getName()));
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("admin/cities/update")
    @ApiOperation(value = "City category", consumes = "City object", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Category object"),
            @ApiResponse(code = 404, message = "Category not found")})
    public ResponseEntity<Map<String, String>> updateCity(@RequestBody CityDto oldCity) {
        Map<String, String> response = new HashMap<>();
        Optional<City> city = cityService.findById(oldCity.getId());
        if (city.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City with id:" + oldCity.getId() + "not found");
        }
        City newCity = city.get();
        newCity.setId(oldCity.getId());
        newCity.setName(oldCity.getName());
        cityService.save(newCity);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("admin/cities/{id}")
    @ApiOperation(value = "Delete city", consumes = "Path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> deleteCity(@PathVariable Integer id) {
        cityService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

}
