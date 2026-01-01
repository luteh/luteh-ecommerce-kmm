package org.luteh.ecommerce.presentation.ui.product_detail

import org.luteh.ecommerce.domain.model.ProductModel

data class ProductDetailState(
    val product: ProductModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddedToCart: Boolean = false
)

sealed interface ProductDetailEvent {
    data class LoadProduct(val productId: String) : ProductDetailEvent
    data object AddToCart : ProductDetailEvent
    data object OnCartClicked : ProductDetailEvent
}

sealed interface ProductDetailEffect {
    data class ShowSnackbar(val message: String) : ProductDetailEffect
    data object NavigateToCart : ProductDetailEffect
    data object NavigateToLogin : ProductDetailEffect
}

