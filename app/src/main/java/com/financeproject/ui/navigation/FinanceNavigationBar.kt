package com.financeproject.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.financeproject.R
import com.financeproject.ui.state.CurrencyState
import com.financeproject.ui.viewmodels.FinanceViewModel

class FinanceNavigationBar() {
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
        NavigationBar(modifier = Modifier.shadow(elevation = 8.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer) {
            val backStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry.value?.destination?.route

            items.forEach { navItem ->
                NavigationBarItem(
                    selected = currentRoute == navItem.route,
                    onClick = {
                        targetScreen = navItem.title
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = BitmapPainter(ImageBitmap.imageResource(navItem.image)),
                            contentDescription = navItem.title,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    colors = NavigationBarItemColors(
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController, txt: String, vm: FinanceViewModel) {
        val currency = vm.currency.value
        CenterAlignedTopAppBar(colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ), title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp),
            ) {
                IconButton(onClick = {
                    targetScreen = "Settings"
                    navController.navigate(topBarNav.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        painter = BitmapPainter(ImageBitmap.imageResource(R.drawable.settings)),
                        contentDescription = "Settings",
                        modifier = Modifier.size(25.dp)
                    )
                }
                Text(text = txt, modifier = Modifier.align(Alignment.Center))
                when (currency) {
                    is CurrencyState.Loading -> {
                        Column(modifier = Modifier.align(Alignment.CenterEnd), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(id = R.string.loading), style = MaterialTheme.typography.bodyMedium)
                            Text(text = stringResource(id = R.string.currency), style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    is CurrencyState.Success -> {
                        Column(
                            modifier = Modifier
                                .clickable(onClick = { vm.getDailyRates(true) })
                                .align(Alignment.CenterEnd),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$" + "%.2f".format(currency.usd.Value),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                            Text(
                                text = "€" + "%.2f".format(currency.eur.Value),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (vm.isCachedData()) {
                                Text(
                                    text =  stringResource(id = R.string.outdated),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    is CurrencyState.Error -> {
                        Text(
                            text = stringResource(id = R.string.error),
                            modifier = Modifier
                                .clickable(onClick = { vm.getDailyRates(true) })
                                .align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        },
            navigationIcon = {
            }
        )
    }
}