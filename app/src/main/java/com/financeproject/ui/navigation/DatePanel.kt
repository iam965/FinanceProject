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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.financeproject.R

@Composable
fun DatePanel(date: String, onClick: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable { onClick() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(stringResource(id = R.string.selected_period), color = MaterialTheme.colorScheme.onBackground)
            Text(date, color = MaterialTheme.colorScheme.onBackground)
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(
                top = 0.dp,
                bottom = 0.dp,
                start = 5.dp,
                end = 5.dp
            ),
        thickness = 1.dp
    )
}