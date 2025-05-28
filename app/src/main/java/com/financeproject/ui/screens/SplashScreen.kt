package com.financeproject.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
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

        isPlaying = false
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            isPlaying = isPlaying,
            iterations = 4,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha)
        )
    }
}