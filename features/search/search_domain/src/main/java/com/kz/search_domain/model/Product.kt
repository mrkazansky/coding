package com.kz.search_domain.model

data class Product(
    val id: Int,
    val title: String,
    val price: String,
    val rating: Float,
    val thumbnail: String,
    val images: List<String>,
    val quantity: Int = 1
)

data class ProductResponse(val products: List<Product>)