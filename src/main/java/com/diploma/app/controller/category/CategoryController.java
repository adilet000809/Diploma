package com.diploma.app.controller.category;

import com.diploma.app.dto.CategoryDto;
import com.diploma.app.model.Category;
import com.diploma.app.model.Status;
import com.diploma.app.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of all categories")})
    public List<Category> getAllProducts() {
        return categoryService.findAll();
    }

    @GetMapping("categories/{id}")
    @ApiOperation(value = "Fetch category by id", consumes = "Path variable id", response = Category.class, produces = "Category object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Category object"),
            @ApiResponse(code = 404, message = "Category not found")})
    public ResponseEntity<Category> getProduct(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(value -> ResponseEntity.ok().body(value)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id:" + id + "not found"));
    }

    @PostMapping("admin/categories/add")
    @ApiOperation(value = "Add category", consumes = "Category object", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Category created successfully")})
    public ResponseEntity<Map<String, String>> addCategory(@RequestBody CategoryDto category) {
        Map<String, String> response = new HashMap<>();
        categoryService.save(new Category(category.getName()));
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("admin/categories/update")
    @ApiOperation(value = "Update category", consumes = "Category object", response = String.class, produces = "Operation result response")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Category has been updated successfully"),
            @ApiResponse(code = 404, message = "Category not found")})
    public ResponseEntity<Map<String, String>> updateCategory(@RequestBody CategoryDto oldCategory) {
        Map<String, String> response = new HashMap<>();
        Optional<Category> category = categoryService.findById(oldCategory.getId());
        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Category newCategory = category.get();
        newCategory.setId(oldCategory.getId());
        newCategory.setName(oldCategory.getName());
        categoryService.save(newCategory);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("admin/categories/{id}")
    @RolesAllowed({"ADMIN"})
    @ApiOperation(value = "Delete category", consumes = "Path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id:" + id + "not found");
        category.get().setStatus(Status.INACTIVE);
        categoryService.save(category.get());
        Map<String, String> response = new HashMap<>();
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

}
