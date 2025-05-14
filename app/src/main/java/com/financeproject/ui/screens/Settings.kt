package com.financeproject.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financeproject.ui.viewmodels.FinanceViewModel

@Composable
fun Settings(financevm: FinanceViewModel){
    var showResetDialog by remember { mutableStateOf(false) }
    val isDarkTheme by remember { mutableStateOf(financevm.isDarkTheme) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(75.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Темная тема")
                Switch(checked = isDarkTheme.value, onCheckedChange = {financevm.changeTheme()})
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(75.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { showResetDialog = true },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Сбросить данные")
            }
        }
        if (showResetDialog){
            ResetDialog(onReset = {financevm.resetData()}, onDismiss = {showResetDialog = false})
        }
    }
}

@Composable
private fun ResetDialog(onDismiss: () -> Unit, onReset: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Сброс данных") },
        text = {
            Column {
                Text("Вы действительно хотите сбросить данные?")
            }
        },
        confirmButton = {
            TextButton(
                onClick = onReset
            ) {
                Text("Сбросить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        })
}