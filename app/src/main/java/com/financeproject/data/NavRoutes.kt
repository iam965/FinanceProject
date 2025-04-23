package com.financeproject.data

sealed class NavRoutes(val route: String) {
    object Income : NavRoutes("income")
    object Home : NavRoutes("home")
    object Expense : NavRoutes("expense")
}