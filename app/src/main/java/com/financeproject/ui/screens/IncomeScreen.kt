package com.financeproject.ui.screens

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.financeproject.R
import com.financeproject.data.db.Operation
import com.financeproject.ui.viewmodels.FinanceViewModel
import java.util.Locale

@Composable
fun IncomeScreen(financevm: FinanceViewModel, valute: String) {
    val allIncome by financevm.allProfit.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var totalIncome = allIncome.sumOf { it.value }

    Column {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(id=R.string.total_income),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "%.2f".format(totalIncome) + valute,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),

                icon = { Icon(Icons.Default.Add, contentDescription = "add") },
                text = { Text(stringResource(id=R.string.add_income)) },
                onClick = { showAddDialog = true }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(allIncome.reversed()) { entry ->
                    var showRemoveDialog by remember { mutableStateOf(false) }
                    IncomeItem(entry, onButton = { showRemoveDialog = true }, valute = valute)
                    if (showRemoveDialog) {
                        RemoveDialog(
                            onDismiss = { showRemoveDialog = false },
                            onRemoveIncome = {
                                financevm.deleteOperation(entry); showRemoveDialog = false
                            }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            IncomeDialog(
                onDismiss = { showAddDialog = false },
                onAddIncome = { amount, description ->
                    financevm.insertOperation(
                        Operation(
                            id = 0,
                            description = description,
                            value = amount,
                            isprofit = true,
                            date = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(
                                Calendar.getInstance()
                            )
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }


}

@Composable
private fun IncomeItem(entry: Operation, onButton: () -> Unit, valute: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "+%.2f".format(entry.value) + valute,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4CAF50)
                )
                IconButton(onClick = onButton) {
                    Icon(Icons.Default.Delete, contentDescription = "delete")
                }
            }
        }
    }
}

@Composable
private fun IncomeDialog(
    onDismiss: () -> Unit,
    onAddIncome: (Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id=R.string.add_income))},
        text = {
            Column {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(id = R.string.amount)) },
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
                    label = { Text(stringResource(id=R.string.description)) },
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
                Text(stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
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
        title = { Text(stringResource(id=R.string.delete)) },
        text = {
            Text(stringResource(id = R.string.delete_message))
        },
        confirmButton = {
            TextButton(
                onClick =
                onRemoveIncome

            ) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}