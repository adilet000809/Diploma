package com.diploma.app.controller.purchase;

import com.diploma.app.dto.PurchaseProductDto;
import com.diploma.app.model.Purchase;
import com.diploma.app.model.PurchaseProduct;
import com.diploma.app.model.Users;
import com.diploma.app.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/purchases/")
public class PurchaseController {

    private final UserService userService;
    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final PurchaseProductService purchaseProductService;

    @Autowired
    public PurchaseController(
            UserService userService,
            ProductService productService,
            PurchaseService purchaseService,
            PurchaseProductService purchaseProductService) {
        this.userService = userService;
        this.productService = productService;
        this.purchaseService = purchaseService;
        this.purchaseProductService = purchaseProductService;
    }

    @PostMapping("confirm")
    @ApiOperation(value = "Confirm purchase", consumes = "List of PurchaseProductDTO", response = String.class, produces = "Operation result response")
    public ResponseEntity<Map<String, String>> confirmPurchase(@RequestBody List<PurchaseProductDto> purchaseProducts) {

        Map<String, String> response = new HashMap<>();
        Users customer = userService.findByUserName(getUserDetails().getUsername());
        Purchase purchase = purchaseService.save(new Purchase());
        purchase.setCustomer(customer);
        Optional<PurchaseProduct> product;
        double total = 0.0;
        for (PurchaseProductDto purchaseProduct: purchaseProducts) {
            product = productService.findPurchaseProductBySupermarketProductId(purchaseProduct.getSupermarketProductId());
            if (product.isPresent()) {
                product.get().setQuantity(purchaseProduct.getQuantity());
                product.get().setPurchase(purchase);
                total += product.get().getPrice() * purchaseProduct.getQuantity();
                purchase.addProduct(purchaseProductService.save(product.get()));
            }
        }
        purchase.setTotal(total);
        customer.setTotal(customer.getTotal() + total);
        purchaseService.save(purchase);
        response.put("response", "Success");
        return ResponseEntity.ok().body(response);

    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
