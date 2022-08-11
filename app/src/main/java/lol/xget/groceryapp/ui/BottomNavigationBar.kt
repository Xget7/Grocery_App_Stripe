package lol.xget.groceryapp.ui

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import compose.icons.TablerIcons
import compose.icons.tablericons.Box
import compose.icons.tablericons.BoxMargin
import compose.icons.tablericons.User
import lol.xget.groceryapp.domain.util.Destinations

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {

    val currenRoute = currenRoute(navController)
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 6.dp
    ) {

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = if (currenRoute == Destinations.UserHomeDestinations.route) Icons.Filled.Home  else Icons.Outlined.Home ,
                    contentDescription = "Home",
                    tint = if (currenRoute == Destinations.UserHomeDestinations.route)  MaterialTheme.colors.onSecondary  else Color.LightGray
                )
            },
            selected = currenRoute == Destinations.UserHomeDestinations.route,
            onClick = {
                navController.navigate(Destinations.UserHomeDestinations.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        //DELETE NAVBACKSTACK
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            label = {
                Text(text = "Home", color = if (currenRoute == Destinations.UserHomeDestinations.route)  MaterialTheme.colors.onSecondary  else Color.LightGray)
            }
        )


        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = if (currenRoute == Destinations.ShoppingCar.route)  Icons.Filled.ShoppingCart  else  Icons.Outlined.ShoppingCart,
                    contentDescription = "Cart",
                    tint = if (currenRoute == Destinations.ShoppingCar.route)  MaterialTheme.colors.onSecondary  else Color.LightGray
                )
            },
            selected = currenRoute == Destinations.ShoppingCar.route,
            onClick =
            {
                navController.navigate(Destinations.ShoppingCar.route) {

                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            label = {
                Text(text = "Cart", color =if (currenRoute == Destinations.ShoppingCar.route)  MaterialTheme.colors.onSecondary  else Color.LightGray)
            }

        )

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = TablerIcons.User,
                    contentDescription = "Profile",
                    tint = if (currenRoute == Destinations.ProfileUserDestinations.route)  MaterialTheme.colors.onSecondary  else Color.LightGray
                )
            },
            selected = currenRoute == Destinations.ProfileUserDestinations.route,
            onClick =
            {
                navController.navigate(Destinations.ProfileUserDestinations.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            label = {
                //TODO("Refeactor with a funtion))
                Text(text = "Account", color = if (currenRoute == Destinations.ProfileUserDestinations.route)  MaterialTheme.colors.onSecondary  else Color.LightGray)
            }
        )
    }
}

@Composable
fun currenRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}