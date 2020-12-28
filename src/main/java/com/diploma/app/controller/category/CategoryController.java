package com.diploma.app.controller.category;

import com.diploma.app.model.Category;
import com.diploma.app.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("categories")
    @ApiOperation(value = "Categories", consumes = "Noting", response = List.class, produces = "List of Categories")
    public List<Category> getAllProducts() {
        return categoryService.findAll();
    }

    @GetMapping("categories/{id}")
    @ApiOperation(value = "Fetch category by id", consumes = "Path variable id", response = Category.class, produces = "Category object")
    public ResponseEntity<Category> getProduct(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("categories/add")
    @RolesAllowed("ADMIN")
    @ApiOperation(value = "Add category", consumes = "Category object", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> addCategory(@RequestBody Category category) {
        Map<String, String> response = new HashMap<>();
        categoryService.save(category);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("categories")
    @RolesAllowed({"ADMIN"})
    @ApiOperation(value = "Update category", consumes = "Category object", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> updateCategory(@RequestBody Category oldCategory) {
        Map<String, String> response = new HashMap<>();
        Optional<Category> category = categoryService.findById(oldCategory.getId());
        if (category.isEmpty()) {
            response.put("response", "Category not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Category newCategory = category.get();
        newCategory.setId(oldCategory.getId());
        newCategory.setName(oldCategory.getName());
        categoryService.save(newCategory);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("categories/{id}")
    @RolesAllowed({"ADMIN"})
    @ApiOperation(value = "Delete category", consumes = "Path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

}
