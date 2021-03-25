package com.diploma.app.controller.supermarket;

import com.diploma.app.dto.SupermarketDto;
import com.diploma.app.dto.SupermarketManagerDto;
import com.diploma.app.model.City;
import com.diploma.app.model.Supermarket;
import com.diploma.app.model.Users;
import com.diploma.app.service.CityService;
import com.diploma.app.service.SupermarketService;
import com.diploma.app.service.UserService;
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
public class SupermarketController {

    private final SupermarketService supermarketService;
    private final CityService cityService;
    private final UserService userService;

    @Autowired
    public SupermarketController(SupermarketService supermarketService, CityService cityService, UserService userService) {
        this.supermarketService = supermarketService;
        this.cityService = cityService;
        this.userService = userService;
    }

    @GetMapping("supermarkets")
    @ApiOperation(value = "Supermarkets", consumes = "Noting", response = List.class, produces = "List of Supermarkets")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of supermarkets")})
    public List<Supermarket> getAllSupermarkets() {
        return supermarketService.findAll();
    }

    @GetMapping("supermarkets/")
    @ApiOperation(value = "Supermarkets", consumes = "Noting", response = List.class, produces = "List of Supermarkets")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of supermarkets filtered by city")})
    public List<Supermarket> getAllSupermarketsByCity(@RequestParam(value = "cityId") Integer cityId) {
        return supermarketService.findAllByCityId(cityId);
    }

    @GetMapping("supermarkets/{id}")
    @ApiOperation(value = "Fetch supermarket by id", consumes = "Path variable id", response = Supermarket.class, produces = "Supermarket object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supermarket"),
            @ApiResponse(code = 404, message = "Supermarket not found")})
    public ResponseEntity<Supermarket> getSupermarkets(@PathVariable Integer id) {
        Optional<Supermarket> supermarket = supermarketService.findById(id);
        return supermarket.map(value -> ResponseEntity.ok().body(value)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket not found"));
    }

    @PostMapping("admin/supermarkets/add")
    @ApiOperation(value = "Add supermarket", consumes = "Supermarket object", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supermarket has been created successfully"),
            @ApiResponse(code = 404, message = "City not found")})
    public ResponseEntity<Map<String, String>> addSupermarket(@RequestBody SupermarketDto supermarketDto) {
        Map<String, String> response = new HashMap<>();
        Optional<City> city = cityService.findById(supermarketDto.getCityId());
        if (city.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        }
        Supermarket supermarket = new Supermarket();
        supermarket.setName(supermarketDto.getName());
        supermarket.setCity(city.get());
        supermarketService.save(supermarket);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("admin/supermarkets/{id}")
    @ApiOperation(value = "Update supermarket", consumes = "Supermarket name and path variable id", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supermarket has been updated successfully"),
            @ApiResponse(code = 404, message = "Supermarket not found")})
    public ResponseEntity<Map<String, String>> updateSupermarket(@RequestBody SupermarketDto oldSupermarket) {
        Map<String, String> response = new HashMap<>();
        Optional<Supermarket> supermarket = supermarketService.findById(oldSupermarket.getId());
        if (supermarket.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket with id:" + oldSupermarket.getId() + " not found");
        }
        Supermarket newSupermarket = supermarket.get();
        newSupermarket.setName(oldSupermarket.getName());
        supermarketService.save(newSupermarket);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("admin/supermarkets/{id}")
    @ApiOperation(value = "Delete supermarket", consumes = "Path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> deleteSupermarket(@PathVariable Integer id) {

        Optional<Supermarket> supermarket = supermarketService.findById(id);

        if (supermarket.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket with id: " + id + "not found");
        }

        supermarket.get().clearManagers();
        supermarket.get().clearProducts();
        supermarketService.save(supermarket.get());
        supermarketService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("admin/supermarkets/managers/add")
    @ApiOperation(value = "Update supermarket", consumes = "Supermarket name and path variable id", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supermarket has been updated successfully"),
            @ApiResponse(code = 404, message = "Supermarket or User not found")})
    public ResponseEntity<Map<String, String>> addSupermarketManager(@RequestBody SupermarketManagerDto supermarketManagerDto) {
        Map<String, String> response = new HashMap<>();
        Optional<Supermarket> supermarket = supermarketService.findById(supermarketManagerDto.getSupermarketId());
        if (supermarket.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket not found");
        }
        Users manager = userService.findByUserName(supermarketManagerDto.getManagerUsername());
        if (manager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userService.makeManager(manager);
        manager.addSupermarket(supermarket.get());
        userService.save(manager);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @PutMapping("admin/supermarkets/managers/remove")
    @ApiOperation(value = "Update supermarket", consumes = "Supermarket name and path variable id", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supermarket has been updated successfully"),
            @ApiResponse(code = 404, message = "Supermarket or User not found")})
    public ResponseEntity<Map<String, String>> removeSupermarketManager(@RequestBody SupermarketManagerDto supermarketManagerDto) {
        Map<String, String> response = new HashMap<>();
        Optional<Supermarket> supermarket = supermarketService.findById(supermarketManagerDto.getSupermarketId());
        if (supermarket.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket not found");
        }
        Users manager = userService.findByUserName(supermarketManagerDto.getManagerUsername());
        if (manager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        manager.removeSupermarket(supermarket.get());
        userService.save(manager);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

}
