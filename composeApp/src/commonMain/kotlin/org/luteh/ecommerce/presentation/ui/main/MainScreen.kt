package org.luteh.ecommerce.presentation.ui.main

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.luteh.ecommerce.presentation.ui.home.HomeContent
import org.luteh.ecommerce.presentation.ui.home.HomeTopBar
import org.luteh.ecommerce.presentation.ui.orders.OrdersScreenContent
import org.luteh.ecommerce.presentation.ui.profile.ProfileScreenContent
import org.luteh.ecommerce.presentation.ui.wishlist.WishlistScreenContent

enum class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    ORDERS("Orders", Icons.Filled.Receipt, Icons.Outlined.Receipt),
    WISHLIST("Wishlist", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person),
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToProductList: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: MainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        topBar = {
            when (selectedTab) {
                BottomNavItem.HOME -> {
                    HomeTopBar(
                        isLoggedIn = state.isLoggedIn,
                        onLoginClick = onNavigateToLogin,
                        onProfileClick = { selectedTab = BottomNavItem.PROFILE },
                        onCartClick = {
                            if (state.isLoggedIn) {
                                onNavigateToCart()
                            } else {
                                onNavigateToLogin()
                            }
                        },
                    )
                }
                else -> {}
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
            ) {
                BottomNavItem.entries.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = { selectedTab = item },
                        icon = {
                            Icon(
                                imageVector =
                                    if (selectedTab == item) item.selectedIcon
                                    else item.unselectedIcon,
                                contentDescription = item.label,
                            )
                        },
                        label = { Text(item.label) },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selectedTab) {
                BottomNavItem.HOME -> {
                    HomeContent(
                        onNavigateToProductList = onNavigateToProductList,
                        onNavigateToProductDetail = onNavigateToProductDetail,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                }
                BottomNavItem.ORDERS -> {
                    OrdersScreenContent()
                }
                BottomNavItem.WISHLIST -> {
                    WishlistScreenContent(onNavigateToProductDetail = onNavigateToProductDetail)
                }
                BottomNavItem.PROFILE -> {
                    ProfileScreenContent(
                        isLoggedIn = state.isLoggedIn,
                        onNavigateToLogin = onNavigateToLogin,
                    )
                }
            }
        }
    }
}
