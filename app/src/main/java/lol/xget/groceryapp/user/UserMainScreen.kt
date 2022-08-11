package lol.xget.groceryapp.user

import android.annotation.SuppressLint
import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.composable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.MainActivity
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.BottomNavigationBar
import lol.xget.groceryapp.user.mainUser.presentation.ProductDetail.ProductDetailScreen
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun UserMainScreen(
    navController: NavHostController,
    navHostWithOutBNB: NavController,
    activity: MainActivity
) {


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        UserNavHost(navHostController = navController, navHostWithOutBNB =  navHostWithOutBNB,activity )
    }
}


