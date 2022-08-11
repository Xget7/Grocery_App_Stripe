package lol.xget.groceryapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.auth.login.presentation.LoginScreen
import lol.xget.groceryapp.auth.recoverPassword.presentation.RecoverPassword
import lol.xget.groceryapp.auth.register.presentation.register_seller.RegistrationSellerScreen
import lol.xget.groceryapp.auth.register.presentation.register_user.RegistrationScreen
import lol.xget.groceryapp.common.Constants.convert
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.presentation.main.SplashScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.AddProducts.AddProductScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.EditProducts.EditProductScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerHomeScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersDetail.SellerOrdersDetailScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.shopReviews.ShopReviewsScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.tab.SellerTabScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.BannerOption.BannerScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.EditProfile.EditSellerProfileScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.SellerConfigScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.config.SellerSettingsScreen
import lol.xget.groceryapp.ui.GroceryAppTheme
import lol.xget.groceryapp.user.UserMainScreen
import lol.xget.groceryapp.user.mainUser.presentation.ProductDetail.ProductDetailScreen
import lol.xget.groceryapp.user.mainUser.presentation.userReviews.UserReviewsSreen
import lol.xget.groceryapp.user.shoppingCar.presentation.ShoppingCarScreen
import java.util.*

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    val currentScreen = mutableStateOf("")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = mAuth.currentUser
        //messaging

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),
                "AIzaSyA_bCcuVoA-EvRfUFVmngWIuQzLWVtXpZU",
                Locale.US);
        }

        setContent {
            GroceryAppTheme {
                GroceryApplication(user)
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun GroceryApplication(
        user: FirebaseUser?,
    ) {

        val navController = rememberAnimatedNavController()
        val navBarNavController = rememberAnimatedNavController()

        //make constants of ids
        val fromNotificationOrderId : String? = intent.getStringExtra("orderId")
        val fromNotificationOrderType : String?= intent.getStringExtra("notificationType")
        val fromNotificationSellerUid : String? = intent.getStringExtra("sellerUid")


        AnimatedNavHost(navController = navController,
            startDestination = if (fromNotificationOrderId != null && fromNotificationOrderType == "NewOrder") {
                Destinations.SellerOrdersDetailDestinations.passOrder(fromNotificationOrderId)
            } else if (fromNotificationOrderType == "OrderStatusChanged" && fromNotificationSellerUid != null) {
                Destinations.UserOrderDetailScreen.passOrder(shopId = fromNotificationSellerUid, orderId = fromNotificationOrderId!!)
            }else {
                Destinations.SplashDestinations.route
            }) {

            //--------------------------------AUTH-----------------------------------------------//

            composable(
                route = Destinations.SplashDestinations.route,
            ) {
                SplashScreen(navController, user)
            }

            composable(
                route = Destinations.LoginDestinations.route,
            ) {
                LoginScreen(navController)
            }


            composable(
                route = Destinations.RegisterUserDestinations.route,
            ) {
                RegistrationScreen(navController, activity = this@MainActivity)
            }

            composable(
                route = Destinations.RegisterSellerDestinations.route,
            ) {
                RegistrationSellerScreen(navController, activity = this@MainActivity)
            }
            composable(
                route = Destinations.RecoverPasswordDestinations.route,
            ) {
                RecoverPassword(navController)
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
                route = Destinations.SellerOrdersDestinations.route
            ) {
                SellerOrdersScreen(navController)
            }
            composable(
                route = Destinations.SellerOrdersDetailDestinations.route
            ) {
                SellerOrdersDetailScreen(navController, navBarNavController)
            }

            //TAB
            composable(
                route = Destinations.SellerMainDestinations.route
            ) {
                SellerTabScreen(navController)
            }

            composable(
                route = Destinations.SellerBannerDestination.route
            ) {
                BannerScreen(navController)
            }
            composable(
                route = Destinations.SellerAccountDestinations.route
            ) {
                SellerConfigScreen(navController)
            }
            composable(
                route = Destinations.SellerEditProfileDestinations.route
            ) {
                EditSellerProfileScreen(navController, activity = this@MainActivity)
            }
            composable(
                route = Destinations.SellerEditProductDestinations.route + "/{productId}"
            ) {
                EditProductScreen(navController)
            }

            composable(
                route = Destinations.SellerConfigurationDestinations.route
            ) {
                SellerSettingsScreen(navController)
            }


            //--------------------------------MAIN USER-----------------------------------------------//


            composable(
                route = Destinations.UserMainDestination.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Destinations.SplashDestinations.route ->
                            slideInVertically(
                                initialOffsetY = { 500 },
                                animationSpec = tween(
                                    durationMillis = 400,
                                    easing = FastOutSlowInEasing
                                )
                            ) + fadeIn(animationSpec = tween(300))
                        else -> fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                },
            ) {
                UserMainScreen(
                    navBarNavController,
                    navHostWithOutBNB = navController,
                    this@MainActivity
                )
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
            composable(
                route = Destinations.UserReviewsScreen.route
            ) {
                UserReviewsSreen(navController, navBarNavController)
            }
            composable(
                route = Destinations.ShopReviewsScreen.route
            ) {
                ShopReviewsScreen(navController)
            }


        }
    }


    @SuppressLint("StringFormatInvalid")
    override fun onStart() {
        super.onStart()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FirebaseMessagingService",
                    "Fetching FCM registration token failed",
                    task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("FirebaseMessagingService", msg)
        })
    }

}
