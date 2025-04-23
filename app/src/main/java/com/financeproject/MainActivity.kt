package com.financeproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.financeproject.data.BarItem
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

/*
@Composable
fun FinanceBottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}*/

//надо сделать main окно, которая встречает юзера
//в main возможно диаграмма,э
//окно истории
//мб окно добавления доход/расход

@Composable
fun MainScreen(){
    val navController= rememberNavController()
    Scaffold (
        bottomBar = {BottomNavBar(navController)}
    ){innerPadding->
        Column (modifier = Modifier.padding(innerPadding)){
            NavHost(navController, startDestination = NavRoutes.Income.route){
                composable (NavRoutes.Income.route){ Income()}
                composable (NavRoutes.Home.route){Home()}
                composable (NavRoutes.Expense.route){Expense()}
            }
        }
    }

}


@Composable
fun BottomNavBar(navController: NavController){
    NavigationBar {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute=backStackEntry.value?.destination?.route

        NavBarItems.BarItems.forEach { navItem->
            NavigationBarItem(
                selected = currentRoute==navItem.route,
                onClick = {
                    navController.navigate(navItem.route){
                        popUpTo(navController.graph.findStartDestination().id){saveState=true}
                        launchSingleTop=true
                        restoreState=true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }

    }
}

object NavBarItems{
    val BarItems=listOf(
        BarItem(
            title="Income",
            image = Icons.Filled.ArrowForward,
            route = "income"
        ),
        BarItem(
            title = "Home",
            image = Icons.Filled.Home,
            route = "home"
        ),
        BarItem(
            title = "Expense",
            image = Icons.Filled.ArrowBack,
            route = "expense"
        )

    )
}


//в дате должно быть сткорее всего
sealed class NavRoutes(val route: String) {
    object Income : NavRoutes("income")
    object Home : NavRoutes("home")
    object Expense : NavRoutes("expense")
}
//

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
@Composable
fun MainScreen() {
    Scaffold(bottomBar = { FinanceBottomBar() }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Text(text = "Прив")
        }
    }
}
*/

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinanceProjectTheme {
    }
}