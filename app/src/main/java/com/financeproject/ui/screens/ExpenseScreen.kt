package com.financeproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.financeproject.data.db.Operation
import com.financeproject.logic.dateTime.DateComparator
import com.financeproject.logic.dateTime.DateFormat
import com.financeproject.ui.viewmodels.FinanceViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import com.financeproject.logic.functions.checkPeriod
import com.financeproject.ui.navigation.DatePanel

@Composable
fun ExpenseScreen(
    financevm: FinanceViewModel,
    allLoss: List<Operation>,
    valute: String,
    beg: Long,
    end: Long,
    date: String,
    onDateClick: () -> Unit
) {
    //val allLoss by financevm.allLoss.collectAsState()
    var begPeriod = DateFormat.getDateFromMillis(beg)
    var endPeriod = DateFormat.getDateFromMillis(end)
    var periodLoss = checkPeriod(beg = begPeriod, end = endPeriod, allOperations = allLoss)
    var showAddDialog by remember { mutableStateOf(false) }
    var totalLoss = periodLoss.sumOf { it.value }

    Column {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            DatePanel(date, onClick = onDateClick)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Общий доход"
                    )
                    Text(
                        text = "%.2f".format(totalLoss) + valute,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFF44336)
                    )
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 0.dp),

                icon = { Icon(Icons.Default.Add, contentDescription = "add") },
                text = { Text("Добавить расход") },
                onClick = { showAddDialog = true }
            )
            HorizontalDivider(Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 15.dp), thickness = 3.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            ) {
                items(periodLoss.reversed()) { entry ->
                    var showRemoveDialog by remember { mutableStateOf(false) }
                    LossItem(entry, onButton = { showRemoveDialog = true }, valute = valute)
                    if (showRemoveDialog) {
                        RemoveDialog(
                            onDismiss = { showRemoveDialog = false },
                            onRemoveIncome = {
                                financevm.deleteOperation(entry); showRemoveDialog = false
                            })
                    }
                }
            }
        }

        if (showAddDialog) {
            LossDialog(
                onDismiss = { showAddDialog = false },
                onAddIncome = { amount, description ->
                    financevm.insertOperation(
                        Operation(
                            id = 0,
                            description = description,
                            value = amount,
                            isprofit = false,
                            date = DateFormat.getDateString(LocalDate.now())
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }


}

@Composable
private fun LossItem(entry: Operation, onButton: () -> Unit, valute: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "+%.2f".format(entry.value) + valute,
                    color = Color(0xFFF44336)
                )
                IconButton(onClick = onButton) {
                    Icon(Icons.Default.Delete, contentDescription = "delete")
                }
            }
        }
    }
}

@Composable
private fun LossDialog(
    onDismiss: () -> Unit,
    onAddIncome: (Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить расход") },
        text = {
            Column {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Сумма") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardOptions.Default.keyboardType
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    if (amountValue > 0 && description.isNotBlank()) {
                        onAddIncome(amountValue, description)
                    }
                }
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
private fun RemoveDialog(
    onDismiss: () -> Unit,
    onRemoveIncome: () -> Unit,

    ) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удалить") },
        text = {
            Text("Вы действительно хотите удалить операцию?")
        },
        confirmButton = {
            TextButton(
                onClick =
                onRemoveIncome

            ) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет")
            }
        }
    )
}
