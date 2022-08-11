package lol.xget.groceryapp.seller.mainSeller.presentation.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import lol.xget.groceryapp.seller.mainSeller.presentation.components.SellerToolBar
import lol.xget.groceryapp.seller.mainSeller.presentation.components.Tabs
import lol.xget.groceryapp.seller.mainSeller.presentation.components.TabsContent

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SellerTabScreen(
    navController: NavController,
    viewModel: TabViewModel = hiltViewModel()
    ) {

    val pagerState = rememberPagerState(pageCount = 3)
    Column(
        modifier = Modifier.background(Color.White).fillMaxSize(),
    )
    {
        SellerToolBar(
            userName = viewModel.userName.value,
            userGmail = viewModel.user?.email!!,
            navController = navController
        )
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState, navController)

    }

}