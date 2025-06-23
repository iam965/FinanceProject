package com.financeproject.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DatePanel(date: String, onClick:() -> Unit){
    Card(Modifier.fillMaxWidth().padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 0.dp).clickable{onClick()}) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(5.dp)) {
            Text("Выбранный период")
            Text(date)
        }
    }
    HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom= 0.dp, start = 5.dp, end = 5.dp))
}