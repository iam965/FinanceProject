package com.financeproject.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.financeproject.R
import com.financeproject.data.db.Category
import com.financeproject.data.db.Operation
import com.financeproject.utils.dateTime.DateFormat
import com.financeproject.ui.navigation.FinanceNavigationBar
import com.financeproject.ui.navigation.NavRoutes
import com.financeproject.ui.viewmodels.FinanceViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    financevm: FinanceViewModel,
    allLoss: State<List<Operation>>,
    allProfit: State<List<Operation>>,
    allOperations: State<List<Operation>>,
    allLossCategory: State<List<Category>>,
    allProfitCategory: State<List<Category>>
) {
    val navController = rememberNavController()
    val navigationBar = FinanceNavigationBar()
    var title by remember { mutableStateOf("Home") }
    val valute = financevm.getValute()
    var showPicker by remember { mutableStateOf(false) }
    val dateState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = DateFormat.getMillisFromDate(
            LocalDate.now().withDayOfMonth(1)
        ),
        initialSelectedEndDateMillis = DateFormat.getMillisFromDate(
            LocalDate.now()
        )
    )
    var begPeriod by rememberSaveable { mutableStateOf(dateState.selectedStartDateMillis) }
    var endPeriod by rememberSaveable { mutableStateOf(dateState.selectedEndDateMillis) }
    var dateString by remember {
        mutableStateOf(
            DateFormat.getDateString(
                DateFormat.getDateFromMillis(
                    begPeriod!!
                )
            ) + " - " + DateFormat.getDateString(
                DateFormat.getDateFromMillis(endPeriod!!))
        )
    }

    fun openPicker(){
        dateState.setSelection(begPeriod,endPeriod)
        showPicker=true
    }

    Scaffold(
        topBar = { navigationBar.TopBar(navController, title, financevm) },
        bottomBar = { navigationBar.BottomNavBar(navController) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            HorizontalDivider()
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
                                Button(onClick = {
                                    showPicker = false
                                }) { Text(stringResource(id = R.string.cancel)) }
                                Button(
                                    onClick = {
                                        begPeriod = dateState.selectedStartDateMillis;
                                        endPeriod = dateState.selectedEndDateMillis;
                                        if (begPeriod != null && endPeriod !=null){
                                            dateString = DateFormat.getDateString(
                                                DateFormat.getDateFromMillis(begPeriod!!)
                                            ) + " – " + DateFormat.getDateString(
                                                DateFormat.getDateFromMillis(
                                                    endPeriod!!
                                                )
                                            )
                                        }

                                        showPicker = false;

                                    },
                                    enabled = dateState.selectedStartDateMillis != null &&
                                            dateState.selectedEndDateMillis != null
                                ) { Text(stringResource(id = R.string.ok)) }
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
                        //onDateClick = {showPicker = true},
                        onDateClick = { openPicker() },
                        incomeCategory = allProfitCategory.value

                    )
                    title = stringResource(id = R.string.income_title)
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
                        onDateClick = { openPicker() }
                    )
                    title = stringResource(id = R.string.home_title)
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
                        onDateClick = { openPicker()},
                        //onDateClick = {showPicker = true},
                        lossCategory = allLossCategory.value
                    );
                    navigationBar.currentScreen = "Expense"
                    title = stringResource(id = R.string.expense_title)
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
                    title = stringResource(id = R.string.settings_title)
                }
            }
            HorizontalDivider()
        }
    }
}
