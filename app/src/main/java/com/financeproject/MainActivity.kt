package com.financeproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.financeproject.ui.navigation.BottomNavBar
import com.financeproject.ui.navigation.NavRoutes
import com.financeproject.ui.theme.FinanceProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceProjectTheme {
                MainScreen()
            }
        }
    }
}

//надо сделать main окно, которая встречает юзера
//в main возможно диаграмма,э
//окно истории
//мб окно добавления доход/расход

@Composable
fun MainScreen(){
    val navController= rememberNavController()
    Scaffold (
        bottomBar = { BottomNavBar(navController) }
    ){innerPadding->
        Column (modifier = Modifier.padding(innerPadding)){
            NavHost(navController, startDestination = NavRoutes.Home.route){
                composable (NavRoutes.Income.route){ Income()}
                composable (NavRoutes.Home.route){Home()}
                composable (NavRoutes.Expense.route){Expense()}
            }
        }
    }

}

@Composable
fun Income(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green),
        contentAlignment = Alignment.Center
    ) {
        Text("income")
    }

}
@Composable
fun Home(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.Center
    ){
        Text("home")
    }

}
@Composable
fun Expense(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ){
        Text("expense")
    }


}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinanceProjectTheme {
    }
}*/
