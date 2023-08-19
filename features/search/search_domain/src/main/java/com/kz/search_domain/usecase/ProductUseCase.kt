package com.kz.search_domain.usecase

import androidx.paging.PagingData
import com.kz.search_domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductUseCase {
    fun getProducts(query: String): Flow<PagingData<Product>>

    suspend fun addToCart(product: Product)
}