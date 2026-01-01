package org.luteh.ecommerce.presentation.ui.product_detail

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(productId) { viewModel.onEvent(ProductDetailEvent.LoadProduct(productId)) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProductDetailEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                ProductDetailEffect.NavigateToCart -> onNavigateToCart()
                ProductDetailEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        val product = state.product
        if (product == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        var isVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) { isVisible = true }

        val contentAlpha by
            animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(durationMillis = 500, delayMillis = 300),
            )

        val slideUpOffset by
            animateFloatAsState(
                targetValue = if (isVisible) 0f else 100f,
                animationSpec =
                    tween(durationMillis = 500, delayMillis = 300, easing = FastOutSlowInEasing),
            )

        // Use a fixed color for now as ProductModel doesn't have imageColor
        val imageColor = Color(0xFFE57373)

        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                // Header Image Area
                Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                    Box(
                        modifier =
                            Modifier.fillMaxSize()
                                .background(
                                    brush =
                                        Brush.verticalGradient(
                                            colors =
                                                listOf(imageColor.copy(alpha = 0.8f), imageColor)
                                        )
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Checkroom,
                            contentDescription = null,
                            modifier = Modifier.size(150.dp).scale(if (isVisible) 1f else 0.8f),
                            tint = Color.White.copy(alpha = 0.8f),
                        )
                    }

                    // Top Bar Actions
                    Row(
                        modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier =
                                Modifier.clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.2f)),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { /* TODO: Add to favorite logic */ },
                                modifier =
                                    Modifier.clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.2f)),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = Color.White,
                                )
                            }

                            IconButton(
                                onClick = { viewModel.onEvent(ProductDetailEvent.OnCartClicked) },
                                modifier =
                                    Modifier.clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.2f)),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }

                // Product Details Content
                Column(
                    modifier =
                        Modifier.offset(y = -30.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(24.dp)
                            .alpha(contentAlpha)
                            .offset(y = slideUpOffset.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CATEGORY", // Placeholder
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Text(
                            text = "$${product.price}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.rating} (${product.ratingCount} reviews)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text =
                            "Experience premium quality with this outstanding product. Designed for comfort and durability.", // Placeholder description
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf(Color(0xFFE57373), Color(0xFF81C784), Color(0xFF64B5F6)).forEach {
                            color ->
                            Box(
                                modifier =
                                    Modifier.size(32.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .then(
                                            if (color == Color(0xFFE57373)) {
                                                Modifier.border(
                                                    2.dp,
                                                    MaterialTheme.colorScheme.primary,
                                                    CircleShape,
                                                )
                                            } else Modifier
                                        )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for FAB
                }
            }

            // Bottom Action Bar
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter),
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(ProductDetailEvent.AddToCart) },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Cart")
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                    ) {
                        Text("Buy Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
