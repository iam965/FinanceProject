package com.financeproject.ui.navigation

sealed class NavRoutes(val route: String) {
    object Income : NavRoutes("income")
    object Home : NavRoutes("home")
    object Expense : NavRoutes("expense")
    object Settings : NavRoutes("settings")
}