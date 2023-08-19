package com.kz.search_data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kz.search_data.network.ProductApiService
import com.kz.search_data.network.datasource.ProductPagingDataSource
import com.kz.search_domain.constant.ProductConstant
import com.kz.search_domain.model.Product
import com.kz.search_domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(private val productApiService: ProductApiService) : ProductRepository {
    override fun getProducts(query: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = ProductConstant.PAGE_SIZE,
                pageSize = ProductConstant.PAGE_SIZE
            ),
            pagingSourceFactory = {
                ProductPagingDataSource(query, productApiService)
            }
        ).flow
    }
}