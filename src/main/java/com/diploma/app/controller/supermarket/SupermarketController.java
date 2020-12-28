package com.diploma.app.controller.supermarket;

import com.diploma.app.model.Category;
import com.diploma.app.model.Supermarket;
import com.diploma.app.service.SupermarketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/")
public class SupermarketController {

    private final SupermarketService supermarketService;

    @Autowired
    public SupermarketController(SupermarketService supermarketService) {
        this.supermarketService = supermarketService;
    }

    @GetMapping("supermarkets")
    @ApiOperation(value = "Supermarkets", consumes = "Noting", response = List.class, produces = "List of Supermarkets")
    public List<Supermarket> getAllSupermarkets() {
        return supermarketService.findAll();
    }

    @GetMapping("supermarkets/{id}")
    @ApiOperation(value = "Fetch supermarket by id", consumes = "Path variable id", response = Supermarket.class, produces = "Supermarket object")
    public ResponseEntity<Supermarket> getSupermarkets(@PathVariable Integer id) {
        Optional<Supermarket> supermarket = supermarketService.findById(id);
        return supermarket.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RolesAllowed({"ADMIN"})
    @PostMapping("supermarkets/add")
    @ApiOperation(value = "Add supermarket", consumes = "Supermarket object", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> addSupermarket(@RequestBody Supermarket supermarket) {
        supermarketService.save(supermarket);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    @RolesAllowed({"ADMIN"})
    @PutMapping("supermarkets/{id}")
    @ApiOperation(value = "Update supermarket", consumes = "Supermarket name and path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> updateSupermarket(@RequestBody Supermarket oldSupermarket) {
        Map<String, String> response = new HashMap<>();
        Optional<Supermarket> supermarket = supermarketService.findById(oldSupermarket.getId());
        if (supermarket.isEmpty()) {
            response.put("response", "Supermarket not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Supermarket newSupermarket = supermarket.get();
        newSupermarket.setId(oldSupermarket.getId());
        newSupermarket.setName(oldSupermarket.getName());
        supermarketService.save(newSupermarket);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("supermarkets/{id}")
    @ApiOperation(value = "Delete supermarket", consumes = "Path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> deleteSupermarket(@PathVariable Integer id) {
        supermarketService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

}
