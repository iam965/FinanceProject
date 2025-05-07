package com.financeproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.financeproject.ui.navigation.*
import com.financeproject.ui.theme.FinanceProjectTheme
import com.financeproject.ui.screens.*
import com.financeproject.ui.viewmodels.FinanceViewModel

class MainActivity : ComponentActivity() {
    private lateinit var financevm: FinanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        financevm = ViewModelProvider(this, FinanceViewModel.FinanceViewModelFactory(application))[FinanceViewModel::class.java]
        setContent {
            FinanceProjectTheme {
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
        contentColor = MaterialTheme.colorScheme.primary
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
                    animationSideRight = true
                    IncomeScreen(financevm)

                }
                composable(
                    NavRoutes.Home.route,
                    enterTransition = {
                        if (animationSideRight) {
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
                        if (animationSideRight){
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
