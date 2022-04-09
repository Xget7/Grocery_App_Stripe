package lol.xget.groceryapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.recoverPassword.presentation.RecoverPassword
import lol.xget.groceryapp.login.presentation.LoginScreen
import lol.xget.groceryapp.register.presentation.register_seller.RegistrationSellerScreen
import lol.xget.groceryapp.register.presentation.register_user.RegistrationScreen
import lol.xget.groceryapp.homeSeller.presentation.AddProducts.AddProductScreen
import lol.xget.groceryapp.homeSeller.presentation.EditProducts.EditProductScreen
import lol.xget.groceryapp.homeSeller.presentation.SellerHomeScreen
import lol.xget.groceryapp.profileSeller.presentation.SellerProfileScreen
import lol.xget.groceryapp.presentation.main.SplashScreen
import lol.xget.groceryapp.profileUser.presentation.ProfileScreen
import lol.xget.groceryapp.homeUser.presentation.ProductDetail.ProductDetailScreen
import lol.xget.groceryapp.homeUser.presentation.ShopDetails.ShopDetailScreen
import lol.xget.groceryapp.homeUser.presentation.UserHomeScreen
import lol.xget.groceryapp.ui.GroceryAppTheme

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var mAuth = FirebaseAuth.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user: FirebaseUser? = mAuth.currentUser

        setContent {
            GroceryAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SplashScreen.route
                    ) {
                        composable(
                            route = Screen.LoginScreen.route
                        ) {
                            LoginScreen(navController)
                        }

                        composable(
                            route = Screen.RegisterUserScreen.route
                        ) {
                            RegistrationScreen(navController, activity = this@MainActivity)
                        }

                        composable(
                            route = Screen.RegisterSellerScreen.route,
                        ) {
                            RegistrationSellerScreen(navController, activity = this@MainActivity)

                        }
                        composable(
                            route = Screen.RecoverPasswordScreen.route
                        ) {
                            RecoverPassword(navController)
                        }
                        composable(
                            route = Screen.ProfileUserScreen.route
                        ) {
                            ProfileScreen(navController)
                        }
                        composable(
                            route = Screen.SellerAddProductScreen.route
                        ) {
                            AddProductScreen(navController)
                        }
                        composable(
                            route = Screen.SellerHomeScreen.route
                        ) {
                            SellerHomeScreen(navController)
                        }
                        composable(
                            route = Screen.SellerProfileScreen.route
                        ) {
                            SellerProfileScreen(navController)
                        }
                        composable(
                            route = Screen.SellerEditProductScreen.route + "/{productId}"
                        ) {
                            EditProductScreen(navController)
                        }
                        composable(
                            route = Screen.UserHomeScreen.route
                        ) {
                            UserHomeScreen(navController)
                        }
                        composable(
                            route = Screen.SplashScreen.route
                        ) {
                            SplashScreen(navController, user)
                        }
                        composable(
                            route = Screen.ShopDetailScreen.route + "/{shopId}"
                        ) {
                            ShopDetailScreen(navController, activity = this@MainActivity)
                        }
                        composable(
                            route = Screen.ProductDetailScreen.route
                        ) {
                            ProductDetailScreen(navController)
                        }
                    }

                }

            }
        }
    }


}
