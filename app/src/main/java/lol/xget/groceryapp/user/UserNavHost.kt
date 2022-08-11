package lol.xget.groceryapp.user

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.MainActivity
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.user.mainUser.presentation.ShopDetails.ShopDetailScreen
import lol.xget.groceryapp.user.mainUser.presentation.UserHomeScreen
import lol.xget.groceryapp.user.mainUser.presentation.orders.UserOrdersScreen
import lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails.UserOrdersDetailScreen
import lol.xget.groceryapp.user.mainUser.presentation.userReviews.UserReviewsSreen
import lol.xget.groceryapp.user.profileUser.presentation.ProfileScreen
import lol.xget.groceryapp.user.shoppingCar.presentation.ShoppingCarScreen

@OptIn(
    ExperimentalCoroutinesApi::class, ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun UserNavHost(
    navHostController: NavHostController,
    navHostWithOutBNB: NavController,
    activity: MainActivity
) {

    AnimatedNavHost(
        navController = navHostController,
        startDestination = Destinations.UserHomeDestinations.route
    ) {

        composable(
            route = Destinations.UserHomeDestinations.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(animationSpec = tween(300))
                    Destinations.ShoppingCar.route ->
                        slideIn(
                            initialOffset = { IntOffset(-500, 0) },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    else -> fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            exitTransition = {
                when (initialState.destination.route) {
                    Destinations.ShoppingCar.route ->
                        slideOutHorizontally(
                            targetOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(animationSpec = tween(300))
                    else -> fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destinations.ProfileUserDestinations.route ->
                        slideIn(
                            initialOffset = { IntOffset(-500, 0) },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    else -> fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }

            }
        ) {
            UserHomeScreen(navHostController, navHostWithOutBNB)
        }

        composable(
            route = Destinations.ProfileUserDestinations.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(animationSpec = tween(300))
                    Destinations.UserOrdersScreen.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
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
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    Destinations.ShoppingCar.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
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
            }
        ) {
            ProfileScreen(navHostController, activity = activity)
        }

        composable(
            route = Destinations.ShopDetailDestinations.route + "/{shopId}"
        ) {
            ShopDetailScreen(navHostController, navHostWithOutBNB, activity = activity)
        }

        composable(
            route = Destinations.UserReviewsScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserOrderDetailScreen.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
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
            UserReviewsSreen(navHostController,navHostWithOutBNB)
        }

        composable(
            route = Destinations.ShoppingCar.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    Destinations.ProfileUserDestinations.route ->
                        slideIn(
                            initialOffset = { IntOffset(-500, 0) },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    else -> fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            exitTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    Destinations.ProfileUserDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(animationSpec = tween(300))
                    else -> fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route -> slideInHorizontally(
                        initialOffsetX = { 300 },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                    Destinations.ProfileUserDestinations.route ->
                        slideIn(
                            initialOffset = { IntOffset(300, 300) },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    else -> fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    Destinations.ProfileUserDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(animationSpec = tween(300))
                    else -> fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
        ) {
            ShoppingCarScreen(navHostController)
        }

        composable(
            route = Destinations.UserOrdersScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    Destinations.ProfileUserDestinations.route ->
                        slideIn(
                            initialOffset = { IntOffset(-500, 0) },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    else -> fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            exitTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    Destinations.ProfileUserDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(animationSpec = tween(300))
                    else -> fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route -> slideInHorizontally(
                        initialOffsetX = { 300 },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                    Destinations.ProfileUserDestinations.route ->
                        slideIn(
                            initialOffset = { IntOffset(300, 300) },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    else -> fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Destinations.UserHomeDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    Destinations.ProfileUserDestinations.route ->
                        slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(animationSpec = tween(300))
                    else -> fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
        ) {
            UserOrdersScreen(navHostController)
        }

        composable(
            route = Destinations.UserOrderDetailScreen.route
        ) {
            UserOrdersDetailScreen(navHostController, navHostWithOutBNB)
        }


    }
}