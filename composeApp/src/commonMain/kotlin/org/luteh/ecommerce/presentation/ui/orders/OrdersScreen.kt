package org.luteh.ecommerce.presentation.ui.orders

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import kotlin.math.roundToInt

private fun Double.formatPrice(): String {
    val intPart = this.toLong()
    val decPart = ((this - intPart) * 100).roundToInt()
    return "$intPart.${decPart.toString().padStart(2, '0')}"
}

data class OrderItem(
    val id: String,
    val productName: String,
    val productImage: String,
    val quantity: Int,
    val totalPrice: Double,
    val status: OrderStatus,
    val date: String,
)

enum class OrderStatus(val label: String, val color: Color) {
    PENDING("Pending", Color(0xFFFFA726)),
    PROCESSING("Processing", Color(0xFF42A5F5)),
    SHIPPED("Shipped", Color(0xFF7E57C2)),
    DELIVERED("Delivered", Color(0xFF66BB6A)),
    CANCELLED("Cancelled", Color(0xFFEF5350)),
}

val dummyOrders =
    listOf(
        OrderItem(
            id = "ORD-001",
            productName = "Classic White T-Shirt",
            productImage = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400",
            quantity = 2,
            totalPrice = 59.98,
            status = OrderStatus.DELIVERED,
            date = "Dec 28, 2025",
        ),
        OrderItem(
            id = "ORD-002",
            productName = "Wireless Headphones",
            productImage = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400",
            quantity = 1,
            totalPrice = 149.99,
            status = OrderStatus.SHIPPED,
            date = "Jan 2, 2026",
        ),
        OrderItem(
            id = "ORD-003",
            productName = "Running Shoes",
            productImage = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400",
            quantity = 1,
            totalPrice = 129.99,
            status = OrderStatus.PROCESSING,
            date = "Jan 4, 2026",
        ),
        OrderItem(
            id = "ORD-004",
            productName = "Smart Watch",
            productImage = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400",
            quantity = 1,
            totalPrice = 299.99,
            status = OrderStatus.PENDING,
            date = "Jan 5, 2026",
        ),
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Orders",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
            )
        },
    ) { paddingValues ->
        OrdersScreenContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun OrdersScreenContent(modifier: Modifier = Modifier) {
    if (dummyOrders.isEmpty()) {
        EmptyOrdersContent(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(dummyOrders) { order -> OrderItemCard(order = order) }
        }
    }
}

@Composable
fun EmptyOrdersContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No orders yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your order history will appear here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
fun OrderItemCard(order: OrderItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = order.id,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = order.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                SubcomposeAsyncImage(
                    model = order.productImage,
                    contentDescription = order.productName,
                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier =
                                Modifier.fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    },
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.productName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Qty: ${order.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Text(
                    text = "$${order.totalPrice.formatPrice()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = order.status.color,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = order.status.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = order.status.color,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}
