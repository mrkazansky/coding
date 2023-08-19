package com.example.standardarchitect.di

import com.kz.search_data.di.searchDataModule
import com.kz.search_domain.di.searchDomainModule
import com.kz.search_presentation.di.searchPresentationModule

val searchModules = listOf(searchPresentationModule, searchDataModule, searchDomainModule)

val appModules = listOf(
    searchModules
).flatten()

