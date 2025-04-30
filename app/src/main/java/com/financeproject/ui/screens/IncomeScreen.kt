package com.financeproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.financeproject.ui.viewmodels.ProfitViewModel

@Composable
fun IncomeScreen(profitvm: ProfitViewModel) {
    val allProfitValue = valueCount(profitvm.allProfit)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff5f5f5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FinanceCard(
                    title = "Доходы",
                    value = allProfitValue,
                    color = Color(0xFF4CAF50)
                )

            }
        }

        Text(
            text = "$allProfitValue",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
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

