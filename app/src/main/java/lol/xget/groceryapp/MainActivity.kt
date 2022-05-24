package lol.xget.groceryapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.auth.login.presentation.LoginScreen
import lol.xget.groceryapp.auth.recoverPassword.presentation.RecoverPassword
import lol.xget.groceryapp.auth.register.presentation.register_seller.RegistrationSellerScreen
import lol.xget.groceryapp.auth.register.presentation.register_user.RegistrationScreen
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.presentation.main.SplashScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.AddProducts.AddProductScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.EditProducts.EditProductScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerHomeScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.BannerOption.BannerScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.EditProfile.EditSellerProfileScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.SellerProfileScreen
import lol.xget.groceryapp.ui.GroceryAppTheme
import lol.xget.groceryapp.user.UserMainScreen
import lol.xget.groceryapp.user.mainUser.presentation.ProductDetail.ProductDetailScreen
import lol.xget.groceryapp.user.shoppingCar.presentation.ShoppingCarScreen

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user: FirebaseUser? = mAuth.currentUser

        setContent {
            GroceryAppTheme {
                GroceryApplication(user = user)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun GroceryApplication(
        user : FirebaseUser?
    ) {
        val navController = rememberNavController()
        val navBarNavController = rememberNavController()

        NavHost(navController = navController, startDestination = Destinations.SplashDestinations.route) {

            //--------------------------------AUTH-----------------------------------------------//

            composable(
                route = Destinations.LoginDestinations.route
            ) {
                LoginScreen(navController)
            }

            composable(
                route = Destinations.RegisterUserDestinations.route
            ) {
                RegistrationScreen(navController, activity = this@MainActivity)
            }

            composable(
                route = Destinations.RegisterSellerDestinations.route,
            ) {
                RegistrationSellerScreen(navController, activity = this@MainActivity)
            }
            composable(
                route = Destinations.RecoverPasswordDestinations.route
            ) {
                RecoverPassword(navController)
            }

            composable(
                route = Destinations.SplashDestinations.route
            ) {
                SplashScreen(navController, user)
            }

            //--------------------------------SEllER MAIN-----------------------------------------------//

            composable(
                route = Destinations.SellerAddProductDestinations.route
            ) {
                AddProductScreen(navController)
            }
            composable(
                route = Destinations.SellerHomeDestinations.route
            ) {
                SellerHomeScreen(navController)
            }
            composable(
                route = Destinations.SellerBannerDestination.route
            ) {
                BannerScreen(navController)
            }
            composable(
                route = Destinations.SellerAccountDestinations.route
            ) {
                SellerProfileScreen(navController)
            }
            composable(
                route = Destinations.SellerEditProfileDestinations.route
            ) {
                EditSellerProfileScreen(navController)
            }
            composable(
                route = Destinations.SellerEditProductDestinations.route + "/{productId}"
            ) {
                EditProductScreen(navController)
            }



            //--------------------------------MAIN USER-----------------------------------------------//


            composable(
                route = Destinations.UserMainDestination.route
            ) {
                UserMainScreen(navBarNavController, navHostWithOutBNB =  navController,this@MainActivity )
            }

            composable(
                route = Destinations.ProductDetailDestinations.route
            ) {
                ProductDetailScreen(navController)
            }
            composable(
                route = Destinations.ShoppingCar.route
            ) {
                ShoppingCarScreen(navController)
            }


        }
    }


}
