package com.diploma.app.dto

import com.diploma.app.model.Product

data class ProductWithCheaperOptions(
    val product: Product,
    val cheaperOptions: List<Product>?
)
