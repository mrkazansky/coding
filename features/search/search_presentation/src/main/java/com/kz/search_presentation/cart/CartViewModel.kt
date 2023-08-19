package com.kz.search_presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kz.search_domain.model.Product
import com.kz.search_domain.usecase.CartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


sealed class CartViewModelSideEffect {
    class Toast(val string: String) : CartViewModelSideEffect()
    object ConfirmationDialog : CartViewModelSideEffect()
}

class CartViewModel(private val cartUseCase: CartUseCase) : ViewModel() {

    val sideEffect = MutableStateFlow<CartViewModelSideEffect?>(null)

    val products = cartUseCase.getProducts().cachedIn(viewModelScope)

    val showClearAllConfirmationDialog = MutableStateFlow(false)

    val showClearProductConfirmationDialog =
        MutableStateFlow<Pair<Product?, Boolean>>(Pair(null, false))

    fun clearAll() {
        viewModelScope.launch {
            cartUseCase.clearAll()
            showClearAllConfirmationDialog(false)
        }
    }

    fun increaseProductQuantity(product: Product) {
        viewModelScope.launch {
            cartUseCase.increaseProductQuantity(product.id)
        }
    }

    fun decreaseProductQuantity(product: Product, forceClear: Boolean) {
        viewModelScope.launch {
            if (forceClear){
                cartUseCase.removeProduct(product.id)
                showClearProductConfirmationDialog(show = false)
            } else  {
                if (!cartUseCase.decreaseProductQuantity(product.id)) {
                    showClearProductConfirmationDialog.value = Pair(product, true)
                }
            }
        }
    }

    fun showClearProductConfirmationDialog(product: Product? = null, show: Boolean) {
        showClearProductConfirmationDialog.value = Pair(product, show)
    }

    fun showClearAllConfirmationDialog(show: Boolean) {
        showClearAllConfirmationDialog.value = show
    }
}