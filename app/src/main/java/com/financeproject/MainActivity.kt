package com.financeproject

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.financeproject.ui.navigation.BottomNavBar
import com.financeproject.ui.navigation.NavRoutes
import com.financeproject.ui.theme.FinanceProjectTheme
import com.financeproject.ui.screens.*
import com.financeproject.ui.viewmodels.LossViewModel
import com.financeproject.ui.viewmodels.MainViewModel
import com.financeproject.ui.viewmodels.ProfitViewModel

class MainActivity : ComponentActivity() {
    private lateinit var mainvm: MainViewModel
    private lateinit var lossvm: LossViewModel
    private lateinit var profitvm: ProfitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainvm = ViewModelProvider(this, MainViewModel.MainViewModelFactory(application))[MainViewModel::class.java]
        lossvm = ViewModelProvider(this, LossViewModel.LossViewModelFactory(application))[LossViewModel::class.java]
        profitvm = ViewModelProvider(this, ProfitViewModel.ProfitViewModelFactory(application))[ProfitViewModel::class.java]
        setContent {
            FinanceProjectTheme {
                MainScreen(mainvm)
            }
        }
    }
}

//надо сделать main окно, которая встречает юзера
//в main возможно диаграмма,э
//окно истории
//мб окно добавления доход/расход

@Composable
fun MainScreen(mainvm: MainViewModel){
    val navController = rememberNavController()
    var animationSideRight =true

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
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
                    IncomeScreen()

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
                    HomeScreen(mainvm)
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
                    ExpenseScreen();

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
