package lol.xget.groceryapp.user

import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.composable
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreen
import lol.xget.groceryapp.user.profileUser.presentation.ProfileScreen

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NavigationHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Destinations.UserHomeDestinations.route) {
        composable(
            route = Destinations.UserHomeDestinations.route
        ) {
            UserHomeScreen(navController)
        }

        composable(
            route = Destinations.ProfileUserDestinations.route
        ) {
            ProfileScreen(navController)
        }

    }
}