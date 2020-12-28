package com.diploma.app.controller.purchase;

import com.diploma.app.dto.PurchaseProductDto;
import com.diploma.app.model.Product;
import com.diploma.app.model.Purchase;
import com.diploma.app.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/purchases/")
public class PurchaseController {

    private final UserService userService;
    private final ProductService productService;
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(
            UserService userService,
            ProductService productService,
            PurchaseService purchaseService) {
        this.userService = userService;
        this.productService = productService;
        this.purchaseService = purchaseService;
    }

    @PostMapping("confirm")
    @ApiOperation(value = "Confirm purchase", consumes = "List of PurchaseProductDTO", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> buy(@RequestBody List<PurchaseProductDto> purchaseProducts) {
        Map<String, String> response = new HashMap<>();
        Purchase purchase = new Purchase();
        Product product;
        double total = 0.0;
        for (PurchaseProductDto purchaseProduct: purchaseProducts) {
            product = productService.findById(purchaseProduct.getProductId());
            purchase.addProduct(product, purchaseProduct.getQuantity());
            total += product.getPrice() * purchaseProduct.getQuantity();
        }
        purchase.setCustomer(userService.findByUserName(getUserDetails().getUsername()));
        purchase.setDate(new Date());
        purchase.setTotal(total);
        purchaseService.save(purchase);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}
