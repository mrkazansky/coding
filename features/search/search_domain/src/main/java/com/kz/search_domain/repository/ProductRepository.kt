package com.kz.search_domain.repository

import androidx.paging.PagingData
import com.kz.search_domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(query: String): Flow<PagingData<Product>>
}