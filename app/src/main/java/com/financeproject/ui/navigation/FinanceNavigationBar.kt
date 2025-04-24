package com.financeproject.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.financeproject.R

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BarItem(
            title = "Expense",
            image = ImageBitmap.imageResource(R.drawable.loss_icon),
            route = "expense"
        ),
        BarItem(
            title = "Home",
            image = ImageBitmap.imageResource(R.drawable.home_icon1),
            route = "home"
        ),
        BarItem(
            title = "Income",
            image = ImageBitmap.imageResource(R.drawable.profit_icon),
            route = "income"
        )
    )

    NavigationBar {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute=backStackEntry.value?.destination?.route

        items.forEach { navItem->
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
                    Icon(painter = BitmapPainter(navItem.image),
                        contentDescription = navItem.title,
                        modifier = Modifier.size(40.dp)
                    )
                }
            )
        }

    }
}
