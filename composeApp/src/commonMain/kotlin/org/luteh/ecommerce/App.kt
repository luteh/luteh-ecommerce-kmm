package org.luteh.ecommerce

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.luteh.ecommerce.di.appModule
import org.luteh.ecommerce.presentation.navigation.AppNavigation
import org.luteh.ecommerce.presentation.theme.LutehTheme
import org.luteh.ecommerce.presentation.ui.home.HomeScreen
import org.luteh.ecommerce.presentation.ui.login.LoginScreen
import org.luteh.ecommerce.presentation.ui.product_list.ProductListScreen
import org.luteh.ecommerce.presentation.ui.register.RegisterScreen
import org.luteh.ecommerce.presentation.ui.splash.SplashScreen

@Composable
@Preview
fun App() {
    KoinApplication(application = { modules(appModule()) }) {
        LutehTheme {
            val navigator = rememberNavController()

            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navigator,
                    startDestination = AppNavigation.Splash.route,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    composable(route = AppNavigation.Splash.route) {
                        SplashScreen(
                            onNavigateToHome = {
                                navigator.navigate(
                                    AppNavigation.Home.route,
                                    navOptions {
                                        popUpTo(AppNavigation.Splash.route) { inclusive = true }
                                        launchSingleTop = true
                                    },
                                )
                            }
                        )
                    }
                    composable(route = AppNavigation.Login.route) {
                        LoginScreen(
                            onNavigateToMainScreen = {
                                navigator.navigate(
                                    AppNavigation.Home.route,
                                    navOptions {
                                        popUpTo(AppNavigation.Login.route) { inclusive = true }
                                        launchSingleTop = true
                                    },
                                )
                            },
                            onNavigateToRegisterScreen = {
                                navigator.navigate(AppNavigation.Register.route)
                            },
                            onNavigateBack = { navigator.popBackStack() },
                        )
                    }
                    composable(route = AppNavigation.Register.route) {
                        RegisterScreen(onNavigateBack = { navigator.popBackStack() })
                    }
                    composable(route = AppNavigation.Home.route) {
                        HomeScreen(
                            onNavigateToLogin = { navigator.navigate(AppNavigation.Login.route) },
                            onNavigateToProductList = { navigator.navigate(AppNavigation.ProductList.route) }
                        )
                    }
                    composable(route = AppNavigation.ProductList.route) {
                        ProductListScreen(onNavigateBack = { navigator.popBackStack() })
                    }
                }
            }
        }
    }
}
