package org.luteh.ecommerce.presentation.ui.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.luteh.ecommerce.presentation.ui.common.ProductItem
import org.luteh.ecommerce.presentation.ui.common.dummyProducts

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToProductList: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToCart: () -> Unit
) {
    Scaffold(topBar = { HomeTopBar(onLoginClick = onNavigateToLogin, onCartClick = onNavigateToCart) }) { paddingValues ->
        HomeContent(
            modifier = Modifier.padding(paddingValues),
            onNavigateToProductList = onNavigateToProductList,
            onNavigateToProductDetail = onNavigateToProductDetail
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onNavigateToProductList: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(span = { GridItemSpan(2) }) { PromotionalBanner() }

        item(span = { GridItemSpan(2) }) { SectionHeader(title = "Categories", onSeeAllClick = {}) }

        item(span = { GridItemSpan(2) }) { CategoryList() }

        item(span = { GridItemSpan(2) }) {
            SectionHeader(title = "Popular Products", onSeeAllClick = onNavigateToProductList)
        }

        items(dummyProducts.take(6)) { product ->
            ProductItem(
                product = product,
                onClick = { onNavigateToProductDetail(product.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onLoginClick: () -> Unit, onCartClick: () -> Unit) {
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
                IconButton(onClick = onLoginClick) {
                    Icon(Icons.AutoMirrored.Filled.Login, contentDescription = "Login")
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
fun PromotionalBanner() {
    Card(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart).padding(16.dp).fillMaxWidth(0.6f)
            ) {
                Text(
                    text = "Summer Sale",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Get up to 50% off on selected items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {},
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                ) {
                    Text("Shop Now")
                }
            }
            Icon(
                imageVector = Icons.Rounded.Checkroom,
                contentDescription = null,
                modifier =
                    Modifier.align(Alignment.CenterEnd)
                        .size(120.dp)
                        .padding(end = 16.dp)
                        .alpha(0.2f),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
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

data class Category(val name: String, val icon: ImageVector)
