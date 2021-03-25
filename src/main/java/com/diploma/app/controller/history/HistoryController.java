package com.diploma.app.controller.history;

import com.diploma.app.model.Purchase;
import com.diploma.app.service.PurchaseService;
import com.diploma.app.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public Purchase getPurchase(@PathVariable Integer id) {
        return purchaseService.findByIdAndCustomer(id, userService.findByUserName(getUserDetails().getUsername()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase with id: " + id + " not found"));
    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
