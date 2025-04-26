package com.financeproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.financeproject.data.Operation
import com.financeproject.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(maimvm: MainViewModel){
    val allLossValue = valueCount(maimvm.allLoss)
    val allProfitValue = valueCount(maimvm.allProfit)
    val balance=allProfitValue-allLossValue
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff5f5f5)),
        contentAlignment = Alignment.Center
    ){
        Column (
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = " Общий баланс",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Text(
                text = "%.2f".format(balance),
                style = MaterialTheme.typography.displaySmall,
                color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336), // Зеленый/красный
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FinanceCard(
                    title = "Доходы",
                    value = allProfitValue,
                    color = Color(0xFF4CAF50)
                )

                FinanceCard(
                    title = "Расходы",
                    value = allLossValue,
                    color = Color(0xFFF44336)
                )
            }
        }

        Text(text = "$allLossValue",
            modifier = Modifier.align(Alignment.BottomStart))
        Text(text = "$allProfitValue",
            modifier = Modifier.align(Alignment.BottomEnd))
    }
}


@Composable
private fun FinanceCard(title: String, value: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Text(
            text = "%.2f".format(value),
            style = MaterialTheme.typography.titleLarge,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

fun valueCount(allOperations: StateFlow<List<Operation>>): Double {
    var value: Double = 0.0
    for (operation in allOperations.value){
        value += operation.value
    }
    return value
}