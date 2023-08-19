package com.kz.search_domain.usecase.impl

import androidx.paging.PagingData
import com.kz.search_domain.model.Product
import com.kz.search_domain.repository.CartRepository
import com.kz.search_domain.usecase.CartUseCase
import kotlinx.coroutines.flow.Flow

class CartUseCaseImpl(private val repository: CartRepository) : CartUseCase {
    override fun getProducts(): Flow<PagingData<Product>> {
        return repository.getProducts()
    }

    override suspend fun clearAll() {
        repository.clearCart()
    }

    override suspend fun increaseProductQuantity(productId: Int) {
        repository.increaseProductQuantity(productId)
    }

    override suspend fun decreaseProductQuantity(productId: Int): Boolean {
        return repository.getProduct(productId)?.let {
            if (it.quantity <= 1) {
                return false
            } else {
                repository.decreaseProductQuantity(productId)
                return true
            }
        } ?: true

    }

    override suspend fun removeProduct(productId: Int) {
        repository.removeProduct(productId)
    }
}