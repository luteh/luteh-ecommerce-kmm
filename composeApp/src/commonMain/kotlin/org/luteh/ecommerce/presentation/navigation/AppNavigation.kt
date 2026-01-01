package org.luteh.ecommerce.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AppNavigation {

    @Serializable
    data object Splash : AppNavigation

    @Serializable
    data object Login : AppNavigation

    @Serializable
    data object Register : AppNavigation

    @Serializable
    data object Home : AppNavigation

    @Serializable
    data object ProductList : AppNavigation

    @Serializable
    data object Cart : AppNavigation

    @Serializable
    data class ProductDetail(val productId: String) : AppNavigation
}
