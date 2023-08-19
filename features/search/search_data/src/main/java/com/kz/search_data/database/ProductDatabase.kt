package com.kz.search_data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kz.search_data.database.dao.ProductDao

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao
}