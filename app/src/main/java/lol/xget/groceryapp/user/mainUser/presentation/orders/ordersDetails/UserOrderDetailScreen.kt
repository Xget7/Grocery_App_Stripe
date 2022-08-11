package lol.xget.groceryapp.user.mainUser.presentation.orders.ordersDetails

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.maps.android.ktx.model.streetViewPanoramaCamera
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.mainUser.presentation.components.OrderDetailItemsList
import lol.xget.groceryapp.user.mainUser.presentation.components.OrderList
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserOrdersDetailScreen(
    navController: NavController,
    navControllerWithoutBNB: NavController,
    viewModel: UserOrderDetailViewModel = hiltViewModel()
) {
    val scrollLazyListState = rememberLazyListState()

    val simple: DateFormat = SimpleDateFormat("dd MMM yyyy")


    if (viewModel.state.value.loading == true) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
        }
    } else {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
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
                                    contentDescription = "back arrow",
                                    tint = MaterialTheme.colors.onSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(30.dp))
                            Text(
                                text = "Order Details",
                                fontSize = 30.sp,
                                color = MaterialTheme.colors.primaryVariant,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(60.dp))
                            IconButton(onClick = {
                                navControllerWithoutBNB.navigate(
                                    Destinations.UserReviewsScreen.passShop(viewModel.currentShopId.value)
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.AddComment,
                                    contentDescription = "Review button",
                                    tint = MaterialTheme.colors.onSecondary
                                )
                            }

                        }


                    }


                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
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
                        viewModel.currentOrder.value.orderId?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
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
                        viewModel.currentOrder.value.orderTime?.let {
                            Text(
                                text = simple.format(Date(it.toLong())),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Order Status ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )

                        when (viewModel.currentOrder.value.orderStatus) {
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
                        Modifier.fillMaxWidth(),
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
                        viewModel.currentOrder.value.orderShopName?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }

                    Row(
                        Modifier.fillMaxWidth(),
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
                        viewModel.currentOrder.value.orderItems?.let {
                            Text(
                                text = it.toString(),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
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
                        viewModel.currentOrder.value.orderCost?.let {
                            Text(
                                text = "$it (Include shipping cost)",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(MaterialTheme.colors.onSecondary)
                    ) {
                        Text(
                            text = "Items",
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.background
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                    }

                    if (viewModel.state.value.noItems == false) {
                        LazyColumn(
                            state = scrollLazyListState
                        ) {
                            items(viewModel.currentItemsList) { order ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Log.e("orderLisItems?", order.toString())
                                OrderDetailItemsList(
                                    order
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                            }
                        }
                    } else {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
                        }
                    }
                }


            }

        }

    }


}
