package com.kz.search_data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kz.search_data.database.ProductEntity

@Dao
interface ProductDao {
    @Upsert
    suspend fun insert(product: ProductEntity)

    @Query("Select * From product Where id=:id Limit 1")
    suspend fun getProduct(id: Int): ProductEntity?

    @Query("Update product Set quantity = quantity + 1 Where id = :id")
    suspend fun increaseQuantity(id: Int)

    @Query("Update product Set quantity = quantity - 1 Where id = :id")
    suspend fun decreaseQuantity(id: Int)

    @Query("Select * From product Order by id DESC")
    fun getProducts(): PagingSource<Int, ProductEntity>

    @Query("Delete From product")
    suspend fun deleteAll()

    @Query("Delete From product Where id =  :id")
    suspend fun delete(id: Int)
}