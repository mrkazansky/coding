package com.kz.search_domain.usecase.impl

import androidx.paging.PagingData
import com.kz.search_domain.model.Product
import com.kz.search_domain.repository.CartRepository
import com.kz.search_domain.repository.ProductRepository
import com.kz.search_domain.usecase.ProductUseCase
import kotlinx.coroutines.flow.Flow

class ProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ProductUseCase {
    override fun getProducts(query: String): Flow<PagingData<Product>> {
        return productRepository.getProducts(query)
    }

    override suspend fun addToCart(product: Product) {
        cartRepository.upsert(product)
    }
}