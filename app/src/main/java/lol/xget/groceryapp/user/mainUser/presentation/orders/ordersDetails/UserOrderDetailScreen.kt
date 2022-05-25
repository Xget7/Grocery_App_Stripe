package lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.mainUser.presentation.components.OrderList

@Composable
fun UserOrdersDetailScreen(
    navController: NavController,
    viewModel: UserOrderDetailViewModel = hiltViewModel()
) {
    val scrollLazyListState = rememberLazyListState()
    val currentOrder by remember {
        mutableStateOf(viewModel.currentOrder.value)
    }

    if (viewModel.state.value.loading == true) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DialogBoxLoading()
        }

    } else {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(MaterialTheme.colors.background)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = "back arrow"
                                )
                            }
                            Text(
                                text = "Order Details",
                                fontSize = 34.sp,
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Medium
                            )

                        }


                    }


                }
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Order Id  ",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )
                        currentOrder.orderId?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date ",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )
                        currentOrder.orderTime?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )

                        when (currentOrder.orderStatus) {
                            "In Progress" -> {
                                Text(
                                    text = "In progress",
                                    fontSize = 15.sp,
                                    color = Color(0xFF03A9F4),
                                )
                            }
                            "Cancelled" -> {
                                Text(
                                    text = "Cancelled",
                                    fontSize = 15.sp,
                                    color = Color.Red,
                                )
                            }
                            else -> {
                                Text(
                                    text = "Completed",
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colors.onSecondary,
                                )
                            }
                        }

                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Shop Name  ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )
                        TODO("Modificar db")
                        currentOrder.orderShopName?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Number of items  ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )
                        TODO("Modificar db")
                        currentOrder.orderItems?.let {
                            Text(
                                text = it.toString(),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Amount ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )
                        TODO("Modificar db")
                        currentOrder.orderCost?.let {
                            Text(
                                text = it + "Include shipping cost",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                }


            }

        }

    }


}
