package com.financeproject.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financeproject.data.Operation

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(allProfit:State<List<Operation>>, allLoss: State<List<Operation>>, allOperations: State<List<Operation>>) {

    // Вычисляемые значения
    var profit = allProfit.value.sumOf { it.value }
    var loss = allLoss.value.sumOf { it.value }
    var balance = profit - loss

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 1. Карточка баланса
        BalanceCard(balance)

        // 2. Статистика доходов/расходов
        IncomeExpenseStats(profit, loss)

        // 3. Список последних операций
        RecentOperations(allOperations.value)
    }
}

// Временная модель данных (замените на свою)

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
            modifier = Modifier.padding(24.dp).fillMaxWidth()
        ) {
            Text(
                "Общий баланс",
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "%.2f ₽".format(balance),
                color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontSize = 28.sp
            )
        }
    }
}

// Компонент 2: Статистика
@Composable
private fun IncomeExpenseStats(income: Double, expense: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        Diagram(value = expense, sum = income + expense, color = Color(0xFFF44336), text = "Расходы")
        Diagram(value = income, sum = income + expense, color = Color(0xFF4CAF50), text = "Доходы")
    }
}

@Composable
private fun Diagram(value: Double, sum: Double, color: Color, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier
                .height((150 * value / sum).dp)
                .width(30.dp)
                .background(
                    color = color,
                    shape = AbsoluteRoundedCornerShape(
                        topLeft = 7.dp,
                        topRight = 7.dp,
                        bottomLeft = 0.dp,
                        bottomRight = 0.dp
                    )
                ),
        ) {
        }
        StatCard(text, value, color)
    }
}

@Composable
private fun StatCard(title: String, value: Double, color: Color) {
    Card(
        modifier = Modifier.width(150.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title)
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
private fun RecentOperations(operations: List<Operation>) {
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
            items(operations.takeLast(5)) { operation ->
                OperationItem(operation)
            }
        }
    }
}

@Composable
private fun OperationItem(operation: Operation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    operation.description,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    operation.date,
                    fontSize = 12.sp
                )
            }
            Text(
                "${if (operation.isprofit) "+" else "-"}${"%.2f ₽".format(operation.value)}",
                color = if (operation.isprofit) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontWeight = FontWeight.Medium
            )
        }
    }
}