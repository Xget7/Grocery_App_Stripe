package lol.xget.groceryapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.common.Constants.LATITUDE
import lol.xget.groceryapp.common.Constants.LONGITUDE
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.presentation.auth.forgot_password.RecoverPassword
import lol.xget.groceryapp.presentation.auth.login.LoginScreen
import lol.xget.groceryapp.presentation.auth.register_seller.RegistrationSellerScreen
import lol.xget.groceryapp.presentation.auth.register_user.RegistrationScreen
import lol.xget.groceryapp.presentation.main.Seller.AddProducts.AddProductScreen
import lol.xget.groceryapp.presentation.main.Seller.EditProducts.EditProductScreen
import lol.xget.groceryapp.presentation.main.Seller.Home.SellerHomeScreen
import lol.xget.groceryapp.presentation.main.Seller.Profile.SellerProfileScreen
import lol.xget.groceryapp.presentation.main.SplashScreen
import lol.xget.groceryapp.presentation.main.User.Account.ProfileScreen
import lol.xget.groceryapp.presentation.main.User.Home.ProductDetail.ProductDetailScreen
import lol.xget.groceryapp.presentation.main.User.Home.ShopDetails.ShopDetailScreen
import lol.xget.groceryapp.presentation.main.User.Home.UserHomeScreen
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
