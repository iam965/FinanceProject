package com.financeproject.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
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

class FinanceNavigationBar(){
    private val items: List<BarItem>
    private val topBarNav: BarItem
    var currentScreen: String = "Home"
    var targetScreen: String = "Home"

    init {
        items = listOf(
            BarItem(
                title = "Expense",
                image = R.drawable.loss_icon,
                route = "expense"
            ),
            BarItem(
                title = "Home",
                image = R.drawable.home_icon1,
                route = "home"
            ),
            BarItem(
                title = "Income",
                image = R.drawable.profit_icon,
                route = "income"
            )
        )
        topBarNav = BarItem(title = "Settings", image = R.drawable.settings, route = "settings")
    }

    @Composable
    fun BottomNavBar(navController: NavController) {
        NavigationBar() {
            val backStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute=backStackEntry.value?.destination?.route

            items.forEach { navItem->
                NavigationBarItem(
                    selected = currentRoute==navItem.route,
                    onClick = {
                        targetScreen = navItem.title
                        navController.navigate(navItem.route){
                            popUpTo(navController.graph.findStartDestination().id){saveState=true}
                            launchSingleTop=true
                            restoreState=true
                        }
                    },
                    icon = {
                        Icon(painter = BitmapPainter(ImageBitmap.imageResource(navItem.image)),
                            contentDescription = navItem.title,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                )
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController, txt: String) {
        CenterAlignedTopAppBar(colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ), title = { Text(text = txt) },
            navigationIcon = {
                IconButton(onClick = {
                    targetScreen = topBarNav.title
                    navController.navigate(topBarNav.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(
                        painter = BitmapPainter(ImageBitmap.imageResource(R.drawable.settings)),
                        contentDescription = "Settings",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        )
    }
}
