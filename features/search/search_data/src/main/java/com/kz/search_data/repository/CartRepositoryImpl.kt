package com.kz.search_data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.kz.search_data.database.ProductDatabase
import com.kz.search_data.database.ProductEntity
import com.kz.search_data.database.toProduct
import com.kz.search_domain.constant.ProductConstant
import com.kz.search_domain.model.Product
import com.kz.search_domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(private val database: ProductDatabase) : CartRepository {
    override fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = ProductConstant.PAGE_SIZE,
                initialLoadSize = ProductConstant.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                database.getProductDao().getProducts()
            }
        ).flow.map {
            it.map { entity ->
                entity.toProduct()
            }
        }
    }

    override suspend fun getProduct(productId: Int): Product? {
        return database.getProductDao().getProduct(productId)?.toProduct()
    }

    override suspend fun upsert(product: Product) {
        database.withTransaction {
            database.getProductDao().getProduct(product.id)?.let {
                database.getProductDao().increaseQuantity(product.id)
            } ?: run {
                database.getProductDao().insert(
                    ProductEntity(
                        product.id,
                        product.title,
                        product.price,
                        product.rating,
                        product.thumbnail
                    )
                )
            }
        }
    }

    override suspend fun clearCart() {
        database.withTransaction {
            database.getProductDao().deleteAll()
        }
    }

    override suspend fun increaseProductQuantity(productId: Int) {
        database.withTransaction {
            database.getProductDao().increaseQuantity(productId)
        }
    }

    override suspend fun decreaseProductQuantity(productId: Int) {
        database.withTransaction {
            database.getProductDao().getProduct(productId)?.let {
                if (it.quantity == 1){
                    database.getProductDao().delete(productId)
                } else {
                    database.getProductDao().decreaseQuantity(productId)
                }
            }
        }
    }

    override suspend fun removeProduct(productId: Int) {
        database.withTransaction {
            database.getProductDao().delete(productId)
        }
    }
}