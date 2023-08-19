package com.kz.search_domain.usecase

import androidx.paging.PagingData
import com.kz.search_domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartUseCase {
    fun getProducts(): Flow<PagingData<Product>>

    suspend fun clearAll()

    suspend fun increaseProductQuantity(productId: Int)

    suspend fun decreaseProductQuantity(productId: Int) : Boolean

    suspend fun removeProduct(productId: Int)
}