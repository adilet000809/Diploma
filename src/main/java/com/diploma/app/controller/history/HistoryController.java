package com.diploma.app.controller.history;

import com.diploma.app.dto.CategoryExpenditure;
import com.diploma.app.model.Category;
import com.diploma.app.model.Purchase;
import com.diploma.app.model.PurchaseProduct;
import com.diploma.app.service.PurchaseService;
import com.diploma.app.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/history/")
public class HistoryController {

    private final PurchaseService purchaseService;
    private final UserService userService;

    @Autowired
    public HistoryController(
            PurchaseService purchaseService,
            UserService userService) {
        this.purchaseService = purchaseService;
        this.userService = userService;
    }

    @GetMapping("")
    @ApiOperation(value = "Fetch all purchases of the user", consumes = "Nothing", response = List.class, produces = "List of Purchase")
    public List<Purchase> getAllPurchases() {
        return purchaseService.findByCustomer(userService.findByUserName(getUserDetails().getUsername()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Fetch purchase", consumes = "Path variable id", response = Purchase.class, produces = "Purchase object")
    public List<CategoryExpenditure> getPurchase(@PathVariable Integer id) {

        Purchase purchase = purchaseService.findByIdAndCustomer(id, userService.findByUserName(getUserDetails().getUsername()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase with id: " + id + " not found"));
        Set<PurchaseProduct> purchaseProductSet = purchase.getPurchaseProducts();
        List<CategoryExpenditure> expenditures = new ArrayList<>();
        Set<Category> categories = new HashSet<>();
        purchaseProductSet.forEach(p -> categories.add(p.getProduct().getCategory()));
        categories.forEach(c -> {
            double total = purchaseProductSet.stream().filter(p -> p.getProduct().getCategory().equals(c)).mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
            expenditures.add(new CategoryExpenditure(c.getName(), total));
        });
        double a = expenditures.stream().mapToDouble(CategoryExpenditure::getAmount).sum();

        return expenditures;
    }

    @GetMapping("filter")
    @ApiOperation(value = "Fetch all purchases of the user", consumes = "Nothing", response = List.class, produces = "List of Purchase")
    public List<Purchase> getAllPurchasesByDate(@RequestParam("from") Long from, @RequestParam("to") Long to) throws ParseException {
        Date fromDate = new Date(from);
        Date toDate = new Date(to);
        return purchaseService.findAllByDateAndCustomer(fromDate, toDate, userService.findByUserName(getUserDetails().getUsername()));
    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
