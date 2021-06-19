package com.diploma.app.controller.product;

import com.diploma.app.dto.ProductDto;
import com.diploma.app.model.Category;
import com.diploma.app.model.Product;
import com.diploma.app.model.Supermarket;
import com.diploma.app.model.SupermarketProduct;
import com.diploma.app.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(value = "/api/")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupermarketService supermarketService;
    private final SupermarketProductService supermarketProductService;
    private final UserService userService;

    @Autowired
    public ProductController(
            ProductService productService,
            CategoryService categoryService,
            SupermarketService supermarketService,
            SupermarketProductService supermarketProductService,
            UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supermarketService = supermarketService;
        this.supermarketProductService = supermarketProductService;
        this.userService = userService;
    }

    @GetMapping("products")
    @ApiOperation(value = "Fetch all products", consumes = "Nothing", response = List.class, produces = "List of Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of products")})
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("products/")
    @ApiOperation(value = "Fetch product by barcode and supermarket", consumes = "Barcode(String) and supermarket id", response = Product.class, produces = "Fetch a Product by its barcode and supermarket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supermarket"),
            @ApiResponse(code = 404, message = "Product not found")})
    public ResponseEntity<SupermarketProduct> getProduct(@RequestParam("barcode") String barcode, @RequestParam("supermarketId") Integer supermarketId) {
        Optional<SupermarketProduct> product = productService.findByBarcodeAndSupermarket(barcode, supermarketId);
        if (product.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        return ResponseEntity.ok().body(product.get());
    }

    @GetMapping("products/cheaper")
    @ApiOperation(value = "Fetch product by barcode and supermarket", consumes = "Barcode(String) and supermarket id", response = Product.class, produces = "Fetch a Product by its barcode and supermarket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supermarket"),
            @ApiResponse(code = 404, message = "Supermarket not found")})
    public ResponseEntity<List<SupermarketProduct>> getCheaperProducts(
            @RequestParam("barcode") String barcode,
            @RequestParam("cityId") Integer cityId,
            @RequestParam("price") Double price) {

        List<SupermarketProduct> cheaperProducts = productService.findCheaperOptions(cityId, price, barcode);
        return ResponseEntity.ok().body(cheaperProducts);
    }

    @PostMapping("manager/products/add")
    @ApiOperation(value = "Add product", consumes = "Product object", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> addProduct(@RequestBody ProductDto productDto) {
        Map<String, String> response = new HashMap<>();
        Optional<Category> category = categoryService.findById(productDto.getCategoryId());
        if (category.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found");
        }

        Optional<Supermarket> supermarket = supermarketService.findById(productDto.getSupermarketId());
        if (supermarket.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supermarket not found");
        }
        checkSupermarketAndManager(supermarket);

        Product product = productService.findByBarcode(productDto.getBarcode()).orElseGet(Product::new);
        product.setName(productDto.getName());
        product.setBarcode(productDto.getBarcode());
        product.setCategory(category.get());
        if (product.getId() == null) {
            product = productService.save(product);
        }

        SupermarketProduct supermarketProduct = supermarketProductService.findById(productDto.getId()).orElseGet(SupermarketProduct::new);
        supermarketProduct.setPrice(productDto.getPrice());
        supermarketProduct.setDiscount(productDto.getDiscount());
        supermarketProduct.setProduct(product);
        supermarketProduct.setSupermarket(supermarket.get());
        supermarketProductService.save(supermarketProduct);
        supermarket.get().addOrUpdateProduct(supermarketProduct);
        product.addOrUpdateProduct(supermarketProduct);
        supermarketService.save(supermarket.get());
        productService.save(product);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @PutMapping("manager/products/update")
    @ApiOperation(value = "Update product", consumes = "ProductDTO object", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> updateProduct(@RequestBody ProductDto oldProduct) {
        Map<String, String> response = new HashMap<>();
        Optional<Product> product = productService.findByBarcode(oldProduct.getBarcode());
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        Optional<Category> category = categoryService.findById(oldProduct.getCategoryId());
        if (category.isEmpty())  {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Optional<Supermarket> supermarket = supermarketService.findById(oldProduct.getSupermarketId());
        checkSupermarketAndManager(supermarket);
        SupermarketProduct supermarketProduct = supermarketProductService.findById(
                oldProduct.getId()).orElseGet(SupermarketProduct::new);
        supermarketProduct.setPrice(oldProduct.getPrice());
        supermarketProduct.setDiscount(oldProduct.getDiscount());
        supermarketProduct.setProduct(product.get());
        supermarketProduct.setSupermarket(supermarket.get());
        supermarketProductService.save(supermarketProduct);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("manager/products")
    @ApiOperation(value = "Delete product", consumes = "Path variable id", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> deleteProduct(@RequestParam("id") Integer id, @RequestParam("supermarketId") Integer supermarketId) {
        Map<String, String> response = new HashMap<>();
        Optional<Supermarket> supermarket = supermarketService.findById(supermarketId);
        if (supermarket.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supermarket not found");
        }
        checkSupermarketAndManager(supermarket);
        Optional<Product> product = productService.findBySupermarketProductId(id);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        product.get().removeProduct(id);
        supermarket.get().removeProduct(id);
        productService.save(product.get());
        supermarketService.save(supermarket.get());
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);
    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkSupermarketAndManager(Optional<Supermarket> supermarket) {
        if (supermarket.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket not found");
        }
        else if (!supermarket.get().getManagers().contains(userService.findByUserName(getUserDetails().getUsername()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not the manager of this supermarket");
        }
    }

}
