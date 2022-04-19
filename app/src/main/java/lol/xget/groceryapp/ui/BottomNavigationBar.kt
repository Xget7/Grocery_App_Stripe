package lol.xget.groceryapp.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import compose.icons.TablerIcons
import compose.icons.tablericons.User
import lol.xget.groceryapp.domain.util.Destinations

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<Destinations>
) {

    val currenRoute = currenRoute( navController)
    BottomNavigation(

    ) {
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
            selected = currenRoute == Destinations.UserHomeDestinations.route,
            onClick =
            {
                navController.navigate(Destinations.UserHomeDestinations.route){
                    popUpTo(navController.graph.findStartDestination().id){
                        //DELETE NAVBACKSTACK
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            label = {Text("Home")}
            )

        BottomNavigationItem(
            icon = { Icon(imageVector = TablerIcons.User, contentDescription = "Profile") },
            selected = currenRoute == Destinations.ProfileUserDestinations.route,
            onClick =
            {
                navController.navigate(Destinations.ProfileUserDestinations.route){
                    popUpTo(navController.graph.findStartDestination().id){
                        //DELETE NAVBACKSTACK
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            label = {Text("Profile")}
        )



    }
}

@Composable
fun currenRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}