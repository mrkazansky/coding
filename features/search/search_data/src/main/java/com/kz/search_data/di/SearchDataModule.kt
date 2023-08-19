package com.kz.search_data.di

import com.kz.core_data.DatabaseBuilder
import com.kz.core_data.NetworkBuilder
import com.kz.search_data.database.ProductDatabase
import com.kz.search_data.network.ProductApiService
import com.kz.search_data.repository.CartRepositoryImpl
import com.kz.search_data.repository.ProductRepositoryImpl
import com.kz.search_domain.repository.CartRepository
import com.kz.search_domain.repository.ProductRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    single {
        NetworkBuilder.createModule(
            "https://dummyjson.com",
            ProductApiService::class.java
        )
    } bind ProductApiService::class

    single {
        DatabaseBuilder.createModule(
            androidApplication(),
            "product",
            ProductDatabase::class.java
        )
    } bind ProductDatabase::class

    factoryOf(::ProductRepositoryImpl) bind ProductRepository::class
    factoryOf(::CartRepositoryImpl) bind CartRepository::class
}