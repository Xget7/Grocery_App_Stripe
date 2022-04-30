package lol.xget.groceryapp.user

import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.composable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.MainActivity
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.user.mainUser.presentation.ProductDetail.ProductDetailScreen
import lol.xget.groceryapp.user.mainUser.presentation.ShopDetails.ShopDetailScreen
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreen
import lol.xget.groceryapp.user.profileUser.presentation.ProfileScreen
import lol.xget.groceryapp.user.shoppingCar.presentation.ShoppingCarScreen

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterialApi::class)
@Composable
fun UserNavHost(
    navHostController: NavHostController,
    navHostWithOutBNB : NavController,
    activity: MainActivity
) {

    NavHost(navController = navHostController, startDestination = Destinations.UserHomeDestinations.route) {

        composable(
            route = Destinations.UserHomeDestinations.route
        ) {
            UserHomeScreen(navHostController , navHostWithOutBNB)
        }

        composable(
            route = Destinations.ProfileUserDestinations.route
        ) {
            ProfileScreen(navHostController,activity = activity)
        }

        composable(
            route = Destinations.ShopDetailDestinations.route + "/{shopId}"
        ) {
            ShopDetailScreen(navHostController, navHostWithOutBNB,activity = activity)
        }

        composable(
            route = Destinations.ShoppingCar.route
        ) {
            ShoppingCarScreen(navHostController)
        }

    }
}