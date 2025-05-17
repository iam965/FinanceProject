package com.financeproject

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.financeproject.ui.navigation.FinanceNavigationBar
import com.financeproject.ui.navigation.NavRoutes
import com.financeproject.ui.screens.ExpenseScreen
import com.financeproject.ui.screens.HomeScreen
import com.financeproject.ui.screens.IncomeScreen
import com.financeproject.ui.screens.Settings
import com.financeproject.ui.state.UIState
import com.financeproject.ui.theme.FinanceProjectTheme
import com.financeproject.ui.viewmodels.FinanceViewModel

class MainActivity : ComponentActivity() {
    private lateinit var financevm: FinanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val uiState = UIState(application.getSharedPreferences("appSettings", Context.MODE_PRIVATE))
        financevm = ViewModelProvider(this, FinanceViewModel.FinanceViewModelFactory(application, uiState))[FinanceViewModel::class.java]
        setContent {
            FinanceProjectTheme(financeViewModel = financevm){
                MainScreen(financevm)
            }
        }
    }
}

@Composable
fun MainScreen(financevm: FinanceViewModel){
    val navController = rememberNavController()
    val navigationBar = FinanceNavigationBar()
    var title by remember { mutableStateOf("Home") }
    val allLoss = financevm.allLoss.collectAsState()
    val allProfit = financevm.allProfit.collectAsState()
    val allOperations = financevm.allOperations.collectAsState()

    Scaffold(
        topBar = { navigationBar.TopBar(navController, title) },
        bottomBar = { navigationBar.BottomNavBar(navController) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = NavRoutes.Home.route) {
                composable(
                    NavRoutes.Income.route,
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        )
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = {fullWidth -> fullWidth},
                            animationSpec = tween(300)
                            )
                    }
                ) {
                    navigationBar.currentScreen = "Income"
                    IncomeScreen(financevm)
                    title = "Доходы"
                }
                composable(
                    NavRoutes.Home.route,
                    enterTransition = {
                        if (navigationBar.currentScreen == "Income") {
                            slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(300)
                                )
                        } else {
                            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(300)
                                )
                        }
                    },
                    exitTransition = {
                        if (navigationBar.targetScreen == "Expense"){
                            slideOutHorizontally(targetOffsetX = {fullWidth -> fullWidth},
                                animationSpec = tween(300)
                                )
                        } else {
                            slideOutHorizontally(targetOffsetX = {fullWidth -> -fullWidth},
                                animationSpec = tween(300)
                                )
                        }
                    }
                ) {
                    HomeScreen(allOperations = allOperations, allProfit = allProfit, allLoss = allLoss)
                    title = "Главная"
                }
                composable(
                    NavRoutes.Expense.route,
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                            )
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = {fullWidth -> -fullWidth},
                            animationSpec = tween(300)
                            )
                    }
                ) {
                    ExpenseScreen(financevm);
                    navigationBar.currentScreen = "Expense"
                    title = "Расходы"
                }
                composable(NavRoutes.Settings.route)
                {
                    navigationBar.currentScreen = "Settings"
                    Settings(financevm)
                    title = "Настройки"
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
