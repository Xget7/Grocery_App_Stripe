package lol.xget.groceryapp.user

import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.composable
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.BottomNavigationBar
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreen

@Composable
fun UserMainScreen() {
    val navController = rememberNavController()

    val navigationItems = listOf(
        Destinations.UserHomeDestinations,
        Destinations.ProfileUserDestinations
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, items = navigationItems) }
    ) {
        NavigationHost(navController = navController)
    }




}

