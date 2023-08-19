package com.kz.search_data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kz.search_domain.model.Product

@Entity("product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val price: String,
    val rating: Float,
    val thumbnail: String,
    val quantity: Int = 1
)

fun ProductEntity.toProduct(): Product {
    return Product(id, title, price, rating, thumbnail, listOf(), quantity)
}