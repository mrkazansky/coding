package com.kz.search_data.network.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kz.search_data.network.ProductApiService
import com.kz.search_domain.model.Product

class ProductPagingDataSource(
    private val query: String,
    private val productApiService: ProductApiService
) :
    PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 0
            val response = productApiService.getProducts(query, page, params.loadSize)
            val isEmpty = response.products.isEmpty()
            val prevPage = if (page == 0) null else page.minus(params.loadSize)
            val nextPage = if (isEmpty) null else page.plus(params.loadSize)
            LoadResult.Page(response.products, prevPage, nextPage)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}