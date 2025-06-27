package com.financeproject.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.financeproject.R

@Composable
fun DatePanel(date: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .shadow(elevation = 8.dp)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(id = R.string.selected_period), color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(date, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}