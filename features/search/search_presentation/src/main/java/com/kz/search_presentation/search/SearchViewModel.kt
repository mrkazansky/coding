package com.kz.search_presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kz.search_domain.model.Product
import com.kz.search_domain.usecase.ProductUseCase
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SearchViewState(
    val query: String = "",
    val products: PagingData<Product> = PagingData.empty()
)

class SearchViewModel(
    private val productUseCase: ProductUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _products = MutableStateFlow<PagingData<Product>>(PagingData.empty())

    val state = combine(_query, _products) { q, p ->
        SearchViewState(q, p)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), SearchViewState())

    init {
        viewModelScope.launch {
            _query
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest {
                    println("Debuging ${it}")
                    productUseCase.getProducts(it)
                }
                .flowOn(ioDispatcher)
                .cachedIn(viewModelScope)
                .collect {
                    println("Debuging ${it}")
                    _products.value = it
                }
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            productUseCase.addToCart(product = product)
        }
    }

}