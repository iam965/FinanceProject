package com.financeproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.financeproject.data.Operation
import com.financeproject.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(mainViewModel: MainViewModel){
    val allLossValue = valueCount(mainViewModel.allLoss)
    val allProfitValue = valueCount(mainViewModel.allProfit)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.Center
    ){
        Text("home")
        Text(text = "$allLossValue",
            modifier = Modifier.align(Alignment.BottomStart))
        Text(text = "$allProfitValue",
            modifier = Modifier.align(Alignment.BottomEnd))
    }
}

fun valueCount(allOperations: StateFlow<List<Operation>>): Double {
    var value: Double = 0.0
    for (operation in allOperations.value){
        value += operation.value
    }
    return value
}