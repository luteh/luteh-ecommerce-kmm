package org.luteh.ecommerce.presentation.ui.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.usecase.auth.CheckSessionUseCase
import org.luteh.ecommerce.presentation.ui.common.dummyProducts

class ProductDetailViewModel(
    private val cartRepository: CartRepository,
    private val checkSessionUseCase: CheckSessionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    private val _effect = Channel<ProductDetailEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.LoadProduct -> loadProduct(event.productId)
            ProductDetailEvent.AddToCart -> checkAuthAndAddToCart()
            ProductDetailEvent.OnCartClicked -> checkAuthAndNavigateToCart()
        }
    }

    private fun checkAuthAndNavigateToCart() {
        viewModelScope.launch {
            val isLoggedIn = checkSessionUseCase()
            if (isLoggedIn) {
                _effect.send(ProductDetailEffect.NavigateToCart)
            } else {
                _effect.send(ProductDetailEffect.NavigateToLogin)
            }
        }
    }

    private fun checkAuthAndAddToCart() {
        viewModelScope.launch {
            val isLoggedIn = checkSessionUseCase()
            if (isLoggedIn) {
                addToCart()
            } else {
                _effect.send(ProductDetailEffect.NavigateToLogin)
            }
        }
    }

    private fun loadProduct(productId: String) {
        // In a real app, this would come from a repository
        val product = dummyProducts.find { it.id == productId }
        if (product != null) {
            _state.update {
                it.copy(
                    product =
                        org.luteh.ecommerce.domain.model.ProductModel(
                            id = product.id,
                            name = product.name,
                            price = product.price.replace("$", "").toDoubleOrNull() ?: 0.0,
                            thumbnailImageUrl = "", // Dummy doesn't have URL
                            shopName = "Luteh Shop",
                            rating = product.rating,
                            ratingCount = 120,
                        )
                )
            }
        }
    }

    private fun addToCart() {
        val product = _state.value.product ?: return
        viewModelScope.launch {
            cartRepository.addToCart(product)
            _effect.send(ProductDetailEffect.ShowSnackbar("Added to cart"))
            _state.update { it.copy(isAddedToCart = true) }
        }
    }
}
