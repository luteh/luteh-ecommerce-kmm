package org.luteh.ecommerce.presentation.ui.transaction_detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionDetailScreen(
    isSuccess: Boolean,
    message: String,
    onNavigateToHome: () -> Unit,
    viewModel: TransactionDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.processEvent(TransactionDetailViewModel.Event.Init(isSuccess, message))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TransactionDetailViewModel.Effect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    Scaffold(
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        viewModel.processEvent(TransactionDetailViewModel.Event.OnBackToHome)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (state.isSuccess) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error
                        ),
                ) {
                    Text(
                        text = if (state.isSuccess) "Back to Home" else "Try Again",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (state.isSuccess) {
                SuccessAnimation()
            } else {
                FailureAnimation()
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (state.isSuccess) "Order Placed Successfully!" else "Transaction Failed",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color =
                    if (state.isSuccess) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun SuccessAnimation() {
    val circleProgress = remember { Animatable(0f) }
    val checkProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        circleProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        )
        checkProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        )
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(120.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2
        val strokeWidth = 8.dp.toPx()

        // Draw Circle
        drawCircle(
            color = primaryColor,
            radius = radius,
            style = Stroke(width = strokeWidth),
            alpha = circleProgress.value,
        )

        // Draw Checkmark
        if (circleProgress.value >= 0.8f) {
            val path =
                Path().apply {
                    moveTo(center.x - radius * 0.4f, center.y)
                    lineTo(center.x - radius * 0.1f, center.y + radius * 0.3f)
                    lineTo(center.x + radius * 0.5f, center.y - radius * 0.4f)
                }

            // Animate path drawing
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(path, false)
            val length = pathMeasure.length
            val partialPath = Path()
            pathMeasure.getSegment(0f, length * checkProgress.value, partialPath, true)

            drawPath(
                path = partialPath,
                color = primaryColor,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
        }
    }
}

@Composable
fun FailureAnimation() {
    val circleProgress = remember { Animatable(0f) }
    val crossProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        circleProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        )
        crossProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        )
    }

    val errorColor = MaterialTheme.colorScheme.error

    Canvas(modifier = Modifier.size(120.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2
        val strokeWidth = 8.dp.toPx()

        // Draw Circle
        drawCircle(
            color = errorColor,
            radius = radius,
            style = Stroke(width = strokeWidth),
            alpha = circleProgress.value,
        )

        // Draw Cross
        if (circleProgress.value >= 0.8f) {
            val path1 =
                Path().apply {
                    moveTo(center.x - radius * 0.3f, center.y - radius * 0.3f)
                    lineTo(center.x + radius * 0.3f, center.y + radius * 0.3f)
                }
            val path2 =
                Path().apply {
                    moveTo(center.x + radius * 0.3f, center.y - radius * 0.3f)
                    lineTo(center.x - radius * 0.3f, center.y + radius * 0.3f)
                }

            val pathMeasure1 = PathMeasure()
            pathMeasure1.setPath(path1, false)
            val length1 = pathMeasure1.length
            val partialPath1 = Path()
            pathMeasure1.getSegment(0f, length1 * crossProgress.value, partialPath1, true)

            drawPath(
                path = partialPath1,
                color = errorColor,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )

            val pathMeasure2 = PathMeasure()
            pathMeasure2.setPath(path2, false)
            val length2 = pathMeasure2.length
            val partialPath2 = Path()
            pathMeasure2.getSegment(0f, length2 * crossProgress.value, partialPath2, true)

            drawPath(
                path = partialPath2,
                color = errorColor,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
        }
    }
}
