package com.diploma.app.controller.product;

import com.diploma.app.dto.ProductDto;
import com.diploma.app.model.Category;
import com.diploma.app.model.Product;
import com.diploma.app.model.Supermarket;
import com.diploma.app.service.CategoryService;
import com.diploma.app.service.ProductService;
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
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupermarketService supermarketService;

    @Autowired
    public ProductController(
            ProductService productService,
            CategoryService categoryService,
            SupermarketService supermarketService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supermarketService = supermarketService;
    }

    @GetMapping("products")
    @ApiOperation(value = "Fetch all products", consumes = "Nothing", response = List.class, produces = "List of Product")
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("products/")
    @ApiOperation(value = "Fetch product by barcode and supermarket", consumes = "Barcode(String) and supermarket id", response = Product.class, produces = "Fetch a Product by its barcode and supermarket")
    public ResponseEntity<Product> getProduct(@RequestParam("barcode") String barcode, @RequestParam("supermarket") Integer id) {
        Optional<Product> product = productService.findByBarcodeAndSupermarket(barcode, id);
        if (product.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(product.get());
    }

    @RolesAllowed({"ADMIN"})
    @PostMapping("products/add")
    @ApiOperation(value = "Add product", consumes = "Product object", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> addProduct(@RequestBody ProductDto productDto) {
        Map<String, String> response = new HashMap<>();
        Optional<Category> category = categoryService.findById(productDto.getCategoryId());
        if (category.isEmpty()){
            response.put("response", "Category not found");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Supermarket> supermarket = supermarketService.findById(productDto.getSupermarketId());
        if (supermarket.isEmpty()) {
            response.put("response", "Supermarket not found");
            return ResponseEntity.badRequest().body(response);
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBarcode(productDto.getBarcode());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setCategory(category.get());
        product.setSupermarket(supermarket.get());
        productService.save(product);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @RolesAllowed({"ADMIN"})
    @PutMapping("products")
    @ApiOperation(value = "Update product", consumes = "ProductDTO object", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> updateProduct(@RequestBody ProductDto oldProduct) {
        Map<String, String> response = new HashMap<>();
        Product product = productService.findById(oldProduct.getId());
        if (product == null) {
            response.put("response", "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Optional<Category> category = categoryService.findById(oldProduct.getCategoryId());
        if (category.isEmpty())  {
            response.put("response", "Category not found");
            return ResponseEntity.badRequest().body(response);
        }
        Optional<Supermarket> supermarket = supermarketService.findById(oldProduct.getCategoryId());
        if (supermarket.isEmpty()) {
            response.put("response", "Product not found.");
            return ResponseEntity.badRequest().body(response);
        }
        product.setName(product.getName());
        product.setPrice(product.getPrice());
        product.setDiscount(product.getDiscount());
        product.setCategory(category.get());
        productService.save(product);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("products/{id}")
    @ApiOperation(value = "Delete product", consumes = "Path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

}
