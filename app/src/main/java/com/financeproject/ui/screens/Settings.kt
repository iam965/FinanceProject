package com.financeproject.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.financeproject.R
import com.financeproject.ui.viewmodels.FinanceViewModel

@Composable
fun Settings(financevm: FinanceViewModel) {
    var showResetDialog by remember { mutableStateOf(false) }
    var showValutePicker by remember { mutableStateOf(false) }
    var showChangeLanguage by remember { mutableStateOf(false) }
    val isDarkTheme by remember { mutableStateOf(financevm.isDarkTheme) }
    val context = LocalContext.current
    val currentLanguage = financevm.getLanguage()
    val currentValute =financevm.getValute()
    val activity = context as? Activity

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
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.dark_theme))
                Switch(checked = isDarkTheme.value, onCheckedChange = { financevm.changeTheme() })
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(75.dp)
                .clickable { showValutePicker = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.change_currency))
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(75.dp)
                .clickable { showChangeLanguage = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.change_language))
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(75.dp)
                .clickable { showResetDialog = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.reset_data))
            }
        }
        if (showResetDialog) {
            ResetDialog(
                onReset = { financevm.resetData(); showResetDialog = false },
                onDismiss = { showResetDialog = false })
        }
        if (showValutePicker) {
            ValutePicker(
                onPick = { str -> financevm.changeValute(str); showValutePicker = false },
                onDismiss = { showValutePicker = false })
        }
        if (showChangeLanguage) {
            ChangeLanguage(
                onPick = { lang -> financevm.changeLanguage(lang); activity?.recreate(); showChangeLanguage = false },
                onDismiss = { showChangeLanguage = false },currentLanguage=currentLanguage
            )
        }
    }
}

@Composable
private fun ResetDialog(onDismiss: () -> Unit, onReset: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { stringResource(id = R.string.data_reset) },
        text = {
            Column {
                Text(stringResource(id = R.string.reset_message))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onReset
            ) {
                Text(stringResource(id = R.string.reset))
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
private fun ValutePicker(onPick: (String) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { stringResource(id = R.string.currency_selection)},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.ruble),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { onPick("₽") })
                Divider()
                Text(
                    text = stringResource(id = R.string.dollar),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { onPick("$") })
                Divider()
                Text(
                    text = stringResource(id = R.string.euro),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { onPick("€") })
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
private fun ChangeLanguage(onPick: (String) -> Unit, onDismiss: () -> Unit,currentLanguage: String) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { stringResource(id = R.string.language_selection) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.russian),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (currentLanguage=="ru"){
                            MaterialTheme.colorScheme.primary
                        }
                        else{
                            MaterialTheme.colorScheme.onSurface
                        }
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            if (currentLanguage != "ru") {
                                onPick("ru")
                            } else {
                                onDismiss()
                            }
                        }
                )
                Divider()
                Text(
                    text = stringResource(id = R.string.english),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (currentLanguage=="en"){
                            MaterialTheme.colorScheme.primary
                        }
                        else{
                            MaterialTheme.colorScheme.onSurface
                        }
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            if (currentLanguage!="en"){
                                onPick("en")
                            }
                            else {
                                onDismiss()
                            }
                        }
                )

            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}