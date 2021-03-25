package com.diploma.app.repository;

import com.diploma.app.model.SupermarketProduct;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupermarketProductRepository extends JpaRepository<SupermarketProduct, Integer> {

    @NotNull
    Optional<SupermarketProduct> findById(@NotNull Integer id);

}
