package org.luteh.ecommerce

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.luteh.ecommerce.di.appModule
import org.luteh.ecommerce.presentation.navigation.AppNavigation
import org.luteh.ecommerce.presentation.theme.LutehTheme
import org.luteh.ecommerce.presentation.ui.cart.CartScreen
import org.luteh.ecommerce.presentation.ui.checkout.CheckoutScreen
import org.luteh.ecommerce.presentation.ui.home.HomeScreen
import org.luteh.ecommerce.presentation.ui.login.LoginScreen
import org.luteh.ecommerce.presentation.ui.product_detail.ProductDetailScreen
import org.luteh.ecommerce.presentation.ui.product_list.ProductListScreen
import org.luteh.ecommerce.presentation.ui.register.RegisterScreen
import org.luteh.ecommerce.presentation.ui.splash.SplashScreen
import org.luteh.ecommerce.presentation.ui.transaction_detail.TransactionDetailScreen

@Composable
@Preview
fun App(
    koinConfig: KoinAppDeclaration? = null
) {
    KoinApplication(application = {
        modules(appModule())
        koinConfig?.invoke(this)
    }) {
        LutehTheme {
            val navigator = rememberNavController()

            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navigator,
                    startDestination = AppNavigation.Splash,
                    modifier = Modifier.fillMaxSize(),
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
                ) {
                    composable<AppNavigation.Splash> {
                        SplashScreen(
                            onNavigateToHome = {
                                navigator.navigate(
                                    AppNavigation.Home,
                                    navOptions {
                                        popUpTo(AppNavigation.Splash) { inclusive = true }
                                        launchSingleTop = true
                                    },
                                )
                            }
                        )
                    }
                    composable<AppNavigation.Login> {
                        LoginScreen(
                            onNavigateToMainScreen = {
                                navigator.navigate(
                                    AppNavigation.Home,
                                    navOptions {
                                        popUpTo(AppNavigation.Login) { inclusive = true }
                                        launchSingleTop = true
                                    },
                                )
                            },
                            onNavigateToRegisterScreen = {
                                navigator.navigate(AppNavigation.Register)
                            },
                            onNavigateBack = { navigator.popBackStack() },
                        )
                    }
                    composable<AppNavigation.Register> {
                        RegisterScreen(onNavigateBack = { navigator.popBackStack() })
                    }
                    composable<AppNavigation.Home> {
                        HomeScreen(
                            onNavigateToLogin = { navigator.navigate(AppNavigation.Login) },
                            onNavigateToProductList = { navigator.navigate(AppNavigation.ProductList) },
                            onNavigateToProductDetail = { productId ->
                                navigator.navigate(AppNavigation.ProductDetail(productId))
                            },
                            onNavigateToCart = { navigator.navigate(AppNavigation.Cart) }
                        )
                    }
                    composable<AppNavigation.ProductList> {
                        ProductListScreen(
                            onNavigateBack = { navigator.popBackStack() },
                            onNavigateToProductDetail = { productId ->
                                navigator.navigate(AppNavigation.ProductDetail(productId))
                            }
                        )
                    }
                    composable<AppNavigation.Cart> {
                        CartScreen(
                            onNavigateBack = { navigator.popBackStack() },
                            onNavigateToCheckout = { navigator.navigate(AppNavigation.Checkout) }
                        )
                    }
                    composable<AppNavigation.ProductDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<AppNavigation.ProductDetail>()
                        ProductDetailScreen(
                            productId = args.productId,
                            onNavigateBack = { navigator.popBackStack() }
                        )
                    }
                    composable<AppNavigation.Checkout> {
                        CheckoutScreen(
                            onNavigateBack = { navigator.popBackStack() },
                            onNavigateToTransactionDetail = { isSuccess, message ->
                                navigator.navigate(
                                    AppNavigation.TransactionDetail(isSuccess, message),
                                    navOptions {
                                        popUpTo(AppNavigation.Home) { inclusive = false }
                                    }
                                )
                            }
                        )
                    }
                    composable<AppNavigation.TransactionDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<AppNavigation.TransactionDetail>()
                        TransactionDetailScreen(
                            isSuccess = args.isSuccess,
                            message = args.message,
                            onNavigateToHome = {
                                navigator.navigate(AppNavigation.Home) {
                                    popUpTo(AppNavigation.Home) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
