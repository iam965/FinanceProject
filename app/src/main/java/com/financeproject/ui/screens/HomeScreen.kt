package com.financeproject.ui.screens

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financeproject.R
import com.financeproject.data.db.Operation
import com.financeproject.utils.dateTime.DateFormat
import com.financeproject.utils.checkPeriod
import com.financeproject.ui.navigation.DatePanel

@Composable
fun HomeScreen(
    allProfit: State<List<Operation>>,
    allLoss: State<List<Operation>>,
    allOperations: State<List<Operation>>,
    valute: String,
    beg: Long,
    end: Long,
    date: String,
    onDateClick: () -> Unit
) {
    var begPeriod = DateFormat.getDateFromMillis(beg)
    var endPeriod = DateFormat.getDateFromMillis(end)
    var periodOperations = checkPeriod(beg = begPeriod, end = endPeriod, allOperations = allOperations.value)
    var periodLoss = checkPeriod(beg = begPeriod, end = endPeriod, allOperations = allLoss.value)
    var periodProfit = checkPeriod(beg = begPeriod, end = endPeriod, allOperations = allProfit.value)
    var profit = periodProfit.sumOf { it.value }
    var loss = periodLoss.sumOf { it.value }
    var balance = profit - loss

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DatePanel(date, onClick = onDateClick)

        BalanceCard(balance, valute)

        IncomeExpenseStats(profit, loss, valute)

        HorizontalDivider(Modifier.padding(horizontal = 5.dp, vertical = 10.dp), thickness = 1.dp)

        RecentOperations(periodOperations, valute)
    }
}

@Composable
private fun BalanceCard(balance: Double, valute: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                stringResource(id = R.string.total_balance),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "%.2f".format(balance) + valute,
                color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun IncomeExpenseStats(income: Double, expense: Double, valute: String) {
    var max = findmax(income, expense)
    Row(
        modifier = Modifier.fillMaxWidth().height(if (max == 0.0) {68.dp} else {170.dp}),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        Diagram(
            value = expense,
            sum = max,
            color = Color(0xFFF44336),
            stringResource(id = R.string.expense),
            valute
        )
        Diagram(
            value = income,
            sum = max,
            color = Color(0xFF4CAF50),
            stringResource(id = R.string.income),
            valute
        )
    }
}

@Composable
private fun Diagram(value: Double, sum: Double, color: Color, text: String, valute: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier
                .height((100 * value / sum).dp)
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
        StatCard(text, value, color, valute)
    }
}

@Composable
private fun StatCard(title: String, value: Double, color: Color, valute: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(68.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title)
            Spacer(Modifier.height(2.dp))
            Text(
                text = "%.2f".format(value) + valute,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RecentOperations(operations: List<Operation>, valute: String) {
    Column(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp)
    ) {
        Text(
            stringResource(id = R.string.recent_transactions),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(operations.takeLast(5).reversed()) { operation ->
                OperationItem(operation, valute)
            }
        }
    }
}

@Composable
private fun OperationItem(operation: Operation, valute: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
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
                "${if (operation.isprofit) "+" else "-"}${"%.2f".format(operation.value) + valute}",
                color = if (operation.isprofit) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun findmax(income: Double, expense: Double): Double{
    return if (income > expense) income
    else expense
}