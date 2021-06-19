package com.diploma.app.repository;

import com.diploma.app.dto.CategoryExpenditure;
import com.diploma.app.model.Purchase;
import com.diploma.app.model.Users;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @NotNull
    @EntityGraph(value = "purchase_with_products")
    Optional<Purchase> findById(@NotNull Integer id);
    @EntityGraph(value = "purchase_only")
    List<Purchase> findAllByCustomer(Users user);
    Optional<Purchase> findByIdAndCustomer(Integer id, Users user);

    @Query("SELECT " +
            "new com.diploma.app.dto.CategoryExpenditure(p.product.category.name, p.price * p.quantity)" +
            "FROM PurchaseProduct p WHERE p.purchase.id=:purchaseId")
    List<CategoryExpenditure> findExpenditure(Integer purchaseId);

    List<Purchase> findAllByDateBetweenAndCustomer(Date from, Date to, Users user);

}
