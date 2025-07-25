package com.financeproject

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.financeproject.ui.screens.MainScreen
import com.financeproject.ui.screens.SplashScreen
import com.financeproject.ui.state.UIState
import com.financeproject.ui.theme.FinanceProjectTheme
import com.financeproject.ui.viewmodels.FinanceViewModel
import com.financeproject.utils.LocaleHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var financevm: FinanceViewModel

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("appSettings", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "ru") ?: "ru"
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashScreen = true

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch {
            delay(400)
            keepSplashScreen = false
        }
        enableEdgeToEdge()
        val uiState = UIState(application.getSharedPreferences("appSettings", Context.MODE_PRIVATE))
        financevm = ViewModelProvider(
            this,
            FinanceViewModel.FinanceViewModelFactory(application, uiState)
        )[FinanceViewModel::class.java]

        setContent {
            var showSplash by remember { mutableStateOf(true) }

            val allLoss = financevm.allLoss.collectAsState()
            val allProfit = financevm.allProfit.collectAsState()
            val allOperations = financevm.allOperations.collectAsState()
            val allLossCategory = financevm.allLossCategory.collectAsState()
            val allProfitCategory = financevm.allProfitCategory.collectAsState()

            FinanceProjectTheme(financevm) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = showSplash,
                        enter = fadeIn(animationSpec = tween(400)),
                        exit = fadeOut(animationSpec = tween(600))
                    ) {
                        SplashScreen(
                            onSplashFinished = {
                                showSplash = false
                            },
                            financevm
                        )
                    }
                    AnimatedVisibility(
                        visible = !showSplash,
                        enter = fadeIn(animationSpec = tween(500)),
                        exit = fadeOut(animationSpec = tween(400))
                    ) {
                        MainScreen(financevm = financevm, allProfit = allProfit, allLoss = allLoss, allOperations = allOperations, allProfitCategory = allProfitCategory, allLossCategory = allLossCategory)
                        MainScreen(
                            financevm = financevm,
                            allProfit = allProfit,
                            allLoss = allLoss,
                            allOperations = allOperations,
                            allLossCategory = allLossCategory,
                            allProfitCategory = allProfitCategory
                        )
                    }
                }
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinanceProjectTheme {
    }
}*/
