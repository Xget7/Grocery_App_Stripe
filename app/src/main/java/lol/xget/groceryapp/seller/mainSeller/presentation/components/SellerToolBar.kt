package lol.xget.groceryapp.seller.mainSeller.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerHomeScreen
import lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersScreen
import lol.xget.groceryapp.seller.profileSeller.presentation.SellerConfigScreen

@Composable
fun SellerToolBar(
    userName: String,
    userGmail: String,
    navController: NavController,
) {
    TopAppBar(
        title = {
            Column {
                Text(text = userName, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(text = userGmail, color = Color.White, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.width(140.dp))
            Row() {
                //firebase log out
                IconButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.popBackStack()
                        navController.navigate(Destinations.LoginDestinations.route)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colors.background)
                }
            }


        },
        backgroundColor = MaterialTheme.colors.onSecondary,
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, navController: NavController) {

    HorizontalPager(
        state = pagerState,

        ) { page ->
        when (page) {
            0 -> SellerHomeScreen(navController = navController)
            1 -> SellerOrdersScreen(navController = navController)
            2 -> SellerConfigScreen(navController = navController)
        }

    }

}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {

    val list = listOf("Products", "Orders", "Settings")
    val scope = rememberCoroutineScope()
    TabRow(

        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color.White,
        divider = {
            TabRowDefaults.Divider(
                thickness = 3.dp,
                color = Color.White
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 3.dp,
                color = Color.Red
            )

        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        text = list[index],
                        color = if (pagerState.currentPage == index) MaterialTheme.colors.onSecondary else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }

    }


}
