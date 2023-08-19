package com.kz.search_domain.repository

import androidx.paging.PagingData
import com.kz.search_domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getProducts(): Flow<PagingData<Product>>

    suspend fun getProduct(productId: Int): Product?

    suspend fun upsert(product: Product)

    suspend fun clearCart()

    suspend fun increaseProductQuantity(productId: Int)

    suspend fun decreaseProductQuantity(productId: Int)

    suspend fun removeProduct(productId: Int)
}