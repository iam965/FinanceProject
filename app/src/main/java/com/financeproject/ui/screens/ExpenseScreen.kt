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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.financeproject.R
import com.financeproject.data.db.Category
import com.financeproject.data.db.Operation
import com.financeproject.utils.dateTime.DateFormat
import com.financeproject.utils.checkPeriod
import com.financeproject.utils.findCategory
import com.financeproject.ui.navigation.DatePanel
import com.financeproject.ui.viewmodels.FinanceViewModel
import com.financeproject.utils.getLocalizedCat
import java.time.LocalDate

@Composable
fun ExpenseScreen(
    financevm: FinanceViewModel,
    allLoss: List<Operation>,
    valute: String,
    beg: Long,
    end: Long,
    date: String,
    onDateClick: () -> Unit,
    lossCategory: List<Category>
) {
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
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.total_expense)
                    )
                    Text(
                        text = "%.2f".format(totalLoss) + valute,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFF44336)
                    )
                }
            }
            HorizontalDivider(Modifier.padding(5.dp), thickness = 1.dp)
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 10.dp),

                icon = { Icon(Icons.Default.Add, contentDescription = "add") },
                text = { Text(stringResource(id = R.string.add_expense)) },
                onClick = { showAddDialog = true }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 5.dp)
            ) {
                items(periodLoss.reversed()) { entry ->
                    val category = findCategory(id = entry.categoryId, category = lossCategory)
                    var showRemoveDialog by remember { mutableStateOf(false) }
                    LossItem(entry = entry, cat = category, onButton = { showRemoveDialog = true }, valute = valute)
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
                lossCategory = lossCategory,
                onDismiss = { showAddDialog = false },
                onAddIncome = { amount, description, catId ->
                    financevm.insertOperation(
                        Operation(
                            id = 0,
                            description = description,
                            value = amount,
                            isprofit = false,
                            date = DateFormat.getDateString(LocalDate.now()),
                            categoryId = catId
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }


}

@Composable
private fun LossItem(entry: Operation, cat: Category?, onButton: () -> Unit, valute: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
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
                    text = if (cat != null){
                        getLocalizedCat(context, cat )
                    } else {
                        "No cat"
                    },
                    style = MaterialTheme.typography.bodySmall
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
    lossCategory: List<Category>,
    onDismiss: () -> Unit,
    onAddIncome: (Double, String, Int) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selected = findCategory(12, lossCategory)
    var selText by remember { mutableStateOf(selected!!.category) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.add_expense)) },
        text = {
            Column {
                DDownMenu(list = lossCategory, text = selText, selected = selected, onItemClick = {
                    id -> selected = findCategory(id, lossCategory); selText = selected!!.category
                })
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(id = R.string.amount))},
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
                    label = { Text(stringResource(id = R.string.description)) },
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
                        onAddIncome(amountValue, description, selected!!.id)
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
            Text(stringResource(id=R.string.delete_message))
        },
        confirmButton = {
            TextButton(
                onClick =
                onRemoveIncome

            ) {
                Text(stringResource(id=R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id=R.string.no))
            }
        }
    )
}