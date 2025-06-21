package com.financeproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.financeproject.data.db.Operation
import com.financeproject.logic.dateTime.DateFormat
import com.financeproject.ui.navigation.FinanceNavigationBar
import com.financeproject.ui.navigation.NavRoutes
import com.financeproject.ui.viewmodels.FinanceViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    financevm: FinanceViewModel,
    allLoss: State<List<Operation>>,
    allProfit: State<List<Operation>>,
    allOperations: State<List<Operation>>
) {
    val navController = rememberNavController()
    val navigationBar = FinanceNavigationBar()
    var title by remember { mutableStateOf("Home") }
    val valute = financevm.getValute()
    var showPicker by remember { mutableStateOf(false) }
    var dateState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = DateFormat.getMillisFromDate(
            LocalDate.now().withDayOfMonth(1)
        ),
        initialSelectedEndDateMillis = DateFormat.getMillisFromDate(
            LocalDate.now()
        )
    )
    var begPeriod by remember { mutableStateOf(dateState.selectedStartDateMillis) }
    var endPeriod by remember { mutableStateOf(dateState.selectedEndDateMillis) }
    var dateString by remember {
        mutableStateOf(
            DateFormat.getDateString(
                DateFormat.getDateFromMillis(
                    begPeriod!!
                )
            ) + " - " + DateFormat.getDateString(DateFormat.getDateFromMillis(endPeriod!!))
        )
    }
    Scaffold(
        topBar = { navigationBar.TopBar(navController, title, financevm) },
        bottomBar = { navigationBar.BottomNavBar(navController) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column {
                if (showPicker) {
                    DatePickerDialog(
                        onDismissRequest = { showPicker = false },
                        confirmButton = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            DateRangePicker(
                                state = dateState,
                                showModeToggle = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp),
                                title = null
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                Button(onClick = { showPicker = false }) { Text("Cancel") }
                                Button(onClick = {
                                    begPeriod = dateState.selectedStartDateMillis;
                                    endPeriod = dateState.selectedEndDateMillis;
                                    dateString = DateFormat.getDateString(
                                        DateFormat.getDateFromMillis(begPeriod!!)
                                    ) + " " + DateFormat.getDateString(
                                        DateFormat.getDateFromMillis(
                                            endPeriod!!
                                        )
                                    )
                                    showPicker = false;
                                }) { Text("OK") }
                            }
                        }
                    }
                }
            }
            NavHost(navController, startDestination = NavRoutes.Home.route) {
                composable(
                    NavRoutes.Income.route,
                    enterTransition = {
                        if (navigationBar.currentScreen == "Settings") {
                            slideInVertically(
                                initialOffsetY = { fullHeight -> fullHeight },
                                animationSpec = tween(300)
                            ) + fadeIn()
                        } else {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(300)
                            ) + fadeIn()
                        }
                    },
                    exitTransition = {
                        if (navigationBar.targetScreen == "Settings") {
                            slideOutVertically(
                                targetOffsetY = { fullHeight -> fullHeight },
                                animationSpec = tween(300)
                            ) + fadeOut()
                        } else {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(300)
                            ) + fadeOut()
                        }
                    }
                ) {
                    navigationBar.currentScreen = "Income"
                    IncomeScreen(
                        financevm = financevm,
                        valute,
                        beg = begPeriod!!,
                        end = endPeriod!!,
                        date = dateString,
                        onDateClick = {showPicker = true}
                    )
                    title = "Доходы"
                }
                composable(
                    NavRoutes.Home.route,
                    enterTransition = {
                        when (navigationBar.currentScreen) {
                            "Income" -> {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> -fullWidth },
                                    animationSpec = tween(300)
                                ) + fadeIn()
                            }

                            "Settings" -> {
                                slideInVertically(
                                    initialOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(300)
                                ) + fadeIn()
                            }

                            else -> {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth },
                                    animationSpec = tween(300)
                                ) + fadeIn()
                            }
                        }
                    },
                    exitTransition = {
                        when (navigationBar.targetScreen) {
                            "Expense" -> {
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> fullWidth },
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            }

                            "Settings" -> {
                                slideOutVertically(
                                    targetOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            }

                            else -> {
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -fullWidth },
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            }
                        }
                    }
                ) {
                    navigationBar.currentScreen = "Home"
                    HomeScreen(
                        allOperations = allOperations,
                        allProfit = allProfit,
                        allLoss = allLoss,
                        valute = valute,
                        beg = begPeriod!!,
                        end = endPeriod!!,
                        date = dateString,
                        onDateClick = { showPicker = true }
                    )
                    title = "Главная"
                }
                composable(
                    NavRoutes.Expense.route,
                    enterTransition = {
                        if (navigationBar.currentScreen == "Settings") {
                            slideInVertically(
                                initialOffsetY = { fullHeight -> fullHeight },
                                animationSpec = tween(300)
                            ) + fadeIn()
                        } else {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(300)
                            ) + fadeIn()
                        }
                    },
                    exitTransition = {
                        if (navigationBar.targetScreen == "Settings") {
                            slideOutVertically(
                                targetOffsetY = { fullHeight -> fullHeight },
                                animationSpec = tween(300)
                            ) + fadeOut()
                        } else {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(300)
                            ) + fadeOut()
                        }
                    }
                ) {
                    ExpenseScreen(
                        financevm,
                        valute = valute,
                        beg = begPeriod!!,
                        end = endPeriod!!,
                        allLoss = allLoss.value,
                        date = dateString,
                        onDateClick = {showPicker = true}
                    );
                    navigationBar.currentScreen = "Expense"
                    title = "Расходы"
                }
                composable(NavRoutes.Settings.route, enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> -fullHeight },
                        animationSpec = tween(300)
                    ) + fadeIn()
                }, exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> -fullHeight },
                        animationSpec = tween(300)
                    ) + fadeOut()
                })
                {
                    navigationBar.currentScreen = "Settings"
                    Settings(financevm)
                    title = "Настройки"
                }
            }
        }
    }
}
