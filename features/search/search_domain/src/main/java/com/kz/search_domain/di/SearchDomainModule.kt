package com.kz.search_domain.di

import com.kz.search_domain.usecase.CartUseCase
import com.kz.search_domain.usecase.ProductUseCase
import com.kz.search_domain.usecase.impl.CartUseCaseImpl
import com.kz.search_domain.usecase.impl.ProductUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDomainModule = module {
    factoryOf(::CartUseCaseImpl) bind CartUseCase::class
    factoryOf(::ProductUseCaseImpl) bind ProductUseCase::class
}