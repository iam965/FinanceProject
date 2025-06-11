package com.financeproject.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    isTest: Boolean = false
) {
    // Skip animation loading in test mode
    if (isTest) {
        SideEffect {
            onSplashFinished()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("splashScreen"),
            contentAlignment = Alignment.Center
        ) {
            // Empty content for test mode
        }
        return
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("SplashAnim.json")
    )

    var isPlaying by remember { mutableStateOf(false) }
    var alpha by remember { mutableStateOf(1f) }
    var backgroundColor = MaterialTheme.colorScheme.background

    LaunchedEffect(Unit) {
        delay(200)
        isPlaying = true
        delay(3000)

        animate(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        ) { value, _ ->
            alpha = value
            backgroundColor = backgroundColor.copy(alpha = 1f - value)
        }

        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .testTag("splashScreen"),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            isPlaying = isPlaying,
            iterations = 4,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha)
                .testTag("splashAnimation")
        )
    }
}