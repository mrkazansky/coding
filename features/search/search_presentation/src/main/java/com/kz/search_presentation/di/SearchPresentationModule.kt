package com.kz.search_presentation.di

import com.kz.search_presentation.cart.CartViewModel
import com.kz.search_presentation.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

val searchPresentationModule = module {
    single { Dispatchers.IO }
    viewModelOf(::SearchViewModel)
    viewModelOf(::CartViewModel)
}