package org.luteh.ecommerce.presentation.navigation

import androidx.navigation.NamedNavArgument

sealed class AppNavigation(
    val route: String, val arguments: List<NamedNavArgument>
) {

    data object Splash : AppNavigation(route = "Splash", arguments = emptyList())

    data object Login : AppNavigation(route = "Login", arguments = emptyList())

    data object Register : AppNavigation(route = "Register", arguments = emptyList())


}

