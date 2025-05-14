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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.financeproject.ui.navigation.*
import com.financeproject.ui.theme.FinanceProjectTheme
import com.financeproject.ui.screens.*
import com.financeproject.ui.state.UIState
import com.financeproject.ui.viewmodels.FinanceViewModel

class MainActivity : ComponentActivity() {
    private lateinit var financevm: FinanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val uiState = UIState(application.getSharedPreferences("appSettings", Context.MODE_PRIVATE))
        financevm = ViewModelProvider(this, FinanceViewModel.FinanceViewModelFactory(application, uiState))[FinanceViewModel::class.java]
        setContent {
            FinanceProjectTheme(financevm) {
                MainScreen(financevm)
            }
        }
    }
}

//надо сделать main окно, которая встречает юзера
//в main возможно диаграмма,э
//окно истории
//мб окно добавления доход/расход

@Composable
fun MainScreen(financevm: FinanceViewModel){
    val navController = rememberNavController()
    var animationSideRight =true
    val navigationBar = FinanceNavigationBar()

    Scaffold(
        bottomBar = { navigationBar.BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        floatingActionButton = { FloatingActionButton(onClick = {navController.navigate("settings")}){} }
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
                    animationSideRight = true
                    IncomeScreen(financevm)

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
                    HomeScreen(financevm)
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
                    animationSideRight = false
                    ExpenseScreen(financevm);
                    navigationBar.currentScreen = "Expense"
                }
                composable(NavRoutes.Settings.route)
                {
                    Settings(financevm)
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
