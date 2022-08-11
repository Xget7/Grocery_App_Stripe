package lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.common.Constants.listOfOrdersState
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.seller.mainSeller.presentation.components.SellerOrderList

@Composable
fun SellerOrdersScreen(
    navController: NavController,
    viewModel: SellerOrdersViewModel = hiltViewModel(),
) {
    var expanded by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TextButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            onClick = {
                expanded = !expanded
            },

            ) {
            Text(
                if (viewModel.selectedOrderFilter.value == "") {
                    "Select Category"
                } else {
                    viewModel.selectedOrderFilter.value
                }, color = MaterialTheme.colors.primaryVariant
            )
        }

        Box(){
            DropdownMenu(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.background
                    )
                    .height(150.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(),
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
                    listOfOrdersState.forEach { label ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.background),
                            onClick = {
                                expanded = false
                                viewModel.filterOrdersBy(label)
                            },
                        ) {
                            Text(text = label, color = MaterialTheme.colors.onSecondary, textAlign = TextAlign.Center)
                        }
                    }
                }

            }
        }



        if (viewModel.ordersList.isEmpty()) {
            Text(text = "You don't have any order.",
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 100.dp))
        } else {


            LazyColumn(
                state = lazyListState
            ) {
                items(viewModel.ordersList) { order ->
                    val gmail = viewModel.getUserGmail(order.orderBy!!).collectAsState(initial = "")
                    SellerOrderList(
                        order, buyerGmail = gmail.value
                    ) {
                        navController.navigate(Destinations.SellerOrdersDetailDestinations.passOrder(order.orderId!!))
                    }
                }
            }
        }




        if (viewModel.state.value.errorMsg != null) {
            EventDialog(
                errorMessage = viewModel.state.value.errorMsg,
                onDismiss = { viewModel.hideErrorDialog() }
            )
        }

    }

}