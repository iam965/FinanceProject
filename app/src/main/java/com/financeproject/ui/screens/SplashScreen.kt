package com.financeproject.ui.screens

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.financeproject.ui.viewmodels.FinanceViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    financeViewModel: FinanceViewModel,
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
                .background(MaterialTheme.colorScheme.onBackground)
                .testTag("splashScreen"),
            contentAlignment = Alignment.Center
        ) {
            // Empty content for test mode
        }
        return
    }

    val context = LocalContext.current
    val isDarkTheme by financeViewModel.isDarkTheme

    val animationAsset = if (isDarkTheme) "SplashAnimDark.json" else "SplashAnim.json"
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(animationAsset)
    )
    val systemColor = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.S){
        val systemColorRes=if (isDarkTheme){
            android.R.color.system_accent1_600
        }
        else{
            android.R.color.system_accent1_200
        }
        Color(context.resources.getColor(systemColorRes,null))
    }
    else{
        Color.Red
    }

    val dynamicProperties= rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = systemColor.toArgb(),
            keyPath = arrayOf("**","Ellipse 1","Fill 1")
        )
    )

    var isPlaying by remember { mutableStateOf(false) }
    var alpha by remember { mutableFloatStateOf(1f) }
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
                .testTag("splashAnimation"),
            dynamicProperties = dynamicProperties
        )
    }
}