package com.financeproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financeproject.ui.viewmodels.FinanceViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(mainvm: FinanceViewModel) {
    // Временные данные (замените потом на ViewModel)
    var operations by remember { mutableStateOf(listOf(
        FinanceOperation("Зарплата", 50000.0, true, Date()),
        FinanceOperation("Аренда", 25000.0, false, Date()),
        FinanceOperation("Продукты", 5000.0, false, Date())
    )) }

    // Вычисляемые значения
    val (balance, totalIncome, totalExpense) = remember(operations) {
        val income = operations.filter { it.isIncome }.sumOf { it.amount }
        val expense = operations.filter { !it.isIncome }.sumOf { it.amount }
        Triple(income - expense, income, expense)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Главная", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            // 1. Карточка баланса
            BalanceCard(balance)

            // 2. Статистика доходов/расходов
            IncomeExpenseStats(totalIncome, totalExpense)

            // 3. Список последних операций
            RecentOperations(operations)
        }
    }
}

// Временная модель данных (замените на свою)
data class FinanceOperation(
    val description: String,
    val amount: Double,
    val isIncome: Boolean,
    val date: Date = Date()
)

// Компонент 1: Карточка баланса
@Composable
private fun BalanceCard(balance: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                "Общий баланс",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "%.2f ₽".format(balance),
                color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Компонент 2: Статистика
@Composable
private fun IncomeExpenseStats(income: Double, expense: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatCard("Доходы", income, Color(0xFF4CAF50))
        StatCard("Расходы", expense, Color(0xFFF44336))
    }
}

@Composable
private fun StatCard(title: String, value: Double, color: Color) {
    Card(
        modifier = Modifier.width(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            Text(
                "%.2f ₽".format(value),
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Компонент 3: Список операций
@Composable
private fun RecentOperations(operations: List<FinanceOperation>) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "Последние операции",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(operations.take(5)) { operation ->
                OperationItem(operation)
            }
        }
    }
}

@Composable
private fun OperationItem(operation: FinanceOperation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    operation.description,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    SimpleDateFormat("dd.MM.yyyy").format(operation.date),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(
                "${if (operation.isIncome) "+" else "-"}${"%.2f ₽".format(operation.amount)}",
                color = if (operation.isIncome) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontWeight = FontWeight.Medium
            )
        }
    }
}