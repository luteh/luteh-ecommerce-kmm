package org.luteh.ecommerce.presentation.ui.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.luteh.ecommerce.domain.model.BannerModel
import org.luteh.ecommerce.domain.model.Category
import org.luteh.ecommerce.domain.model.ProductModel
import org.luteh.ecommerce.presentation.ui.common.ProductItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToProductList: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val state by viewModel.state.collectAsState()

    HomeScreenContent(
        isLoggedIn = state.isLoggedIn,
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToProductList = onNavigateToProductList,
        onNavigateToProductDetail = onNavigateToProductDetail,
        onNavigateToCart = onNavigateToCart,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreenContent(
    isLoggedIn: Boolean,
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToProductList: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                isLoggedIn = isLoggedIn,
                onLoginClick = onNavigateToLogin,
                onProfileClick = onNavigateToProfile,
                onCartClick = {
                    if (isLoggedIn) {
                        onNavigateToCart()
                    } else {
                        onNavigateToLogin()
                    }
                },
            )
        }
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier.padding(paddingValues),
            onNavigateToProductList = onNavigateToProductList,
            onNavigateToProductDetail = onNavigateToProductDetail,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onNavigateToProductList: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        item(span = { GridItemSpan(2) }) { PromotionalBanner() }

        item(span = { GridItemSpan(2) }) { SectionHeader(title = "Categories", onSeeAllClick = {}) }

        item(span = { GridItemSpan(2) }) { CategoryList() }

        item(span = { GridItemSpan(2) }) {
            SectionHeader(title = "Popular Products", onSeeAllClick = onNavigateToProductList)
        }

        items(ProductModel.dummies.take(6)) { product ->
            with(sharedTransitionScope) {
                ProductItem(
                    product = product,
                    onClick = { onNavigateToProductDetail(product.id) },
                    modifier =
                        Modifier.sharedElement(
                            sharedContentState =
                                rememberSharedContentState(key = "image-${product.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Luteh Shop",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Find your best needs",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                }
                IconButton(onClick = onCartClick) {
                    Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart")
                }
                if (isLoggedIn) {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile")
                    }
                } else {
                    IconButton(onClick = onLoginClick) {
                        Icon(Icons.AutoMirrored.Filled.Login, contentDescription = "Login")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search products...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor =
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    focusedContainerColor =
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
            singleLine = true,
        )
    }
}

@Composable
fun PromotionalBanner(banners: List<BannerModel> = BannerModel.dummies) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    LaunchedEffect(pagerState.settledPage) {
        delay(4000)
        val nextPage = (pagerState.currentPage + 1) % banners.size
        pagerState.animateScrollToPage(nextPage)
    }

    Column {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth().height(160.dp)) {
            page ->
            val banner = banners[page]
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = banner.backgroundColor),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    banner.imageUrl?.let { imageUrl ->
                        SubcomposeAsyncImage(
                            model = imageUrl,
                            contentDescription = banner.title,
                            modifier = Modifier.fillMaxSize().alpha(0.3f),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    Column(
                        modifier =
                            Modifier.align(Alignment.CenterStart).padding(16.dp).fillMaxWidth(0.7f)
                    ) {
                        Text(
                            text = banner.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = banner.textColor,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = banner.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = banner.textColor.copy(alpha = 0.9f),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = banner.textColor,
                                    contentColor = banner.backgroundColor,
                                ),
                        ) {
                            Text(banner.buttonText)
                        }
                    }
                }
            }
        }

        if (banners.size > 1) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(banners.size) { index ->
                    Box(
                        modifier =
                            Modifier.padding(horizontal = 4.dp)
                                .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        TextButton(onClick = onSeeAllClick) { Text("See All") }
    }
}

@Composable
fun CategoryList() {
    val categories =
        listOf(
            Category("All", Icons.Rounded.Home),
            Category("Clothes", Icons.Rounded.Checkroom),
            Category("Tech", Icons.Rounded.Devices),
            Category("Sports", Icons.Rounded.SportsSoccer),
        )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(categories) { category -> CategoryItem(category) }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier =
                Modifier.size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {},
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}
