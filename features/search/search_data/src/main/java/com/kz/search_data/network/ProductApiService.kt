package com.kz.search_data.network

import com.kz.search_domain.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {
    @GET("/products/search")
    suspend fun getProducts(
        @Query("q") query: String,
        @Query("skip") page: Int,
        @Query("limit") pageSize: Int,
    ): ProductResponse
}