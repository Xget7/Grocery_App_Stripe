package lol.xget.groceryapp.seller.mainSeller.presentation.SellerOrdersScreen.SellerOrdersDetail

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.user.mainUser.presentation.components.OrderDetailItemsList
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SellerOrdersDetailScreen(
    navController: NavController,
    navControllerWithoutBNB: NavController,
    viewModel: SellerOrdersDetailViewModel = hiltViewModel(),
) {
    val scrollLazyListState = rememberLazyListState()
    val openDialog = remember { mutableStateOf(false) }

    val simple: DateFormat = SimpleDateFormat("dd MMM yyyy")
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current


    if (state.loading == true) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
        }
    } else  {
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
                                //open dialog to change status
                                openDialog.value = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "edit arrow",
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
                            text = "Buyer Email ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )
                        viewModel.currentBuyerEmail.value.let {
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
                            text = "Buyer Phone ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(200.dp)
                        )
                        viewModel.currentBuyerPhone.value.let {
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
                            text = "Items  ",
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

                    if (state.noItems == false) {
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

        if (openDialog.value) {
            AlertDialog(
                backgroundColor = MaterialTheme.colors.background,
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Edit Order Status", color = MaterialTheme.colors.onSecondary)
                },
                text = {
                    Column(Modifier.height(150.dp).fillMaxWidth()) {
                            for (item in listOf("In Progress", "Cancelled", "Completed")) {
                                TextButton(onClick = { viewModel.changeOrderStatus(item) }) {
                                    Text(
                                        text = item,
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colors.primaryVariant
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { openDialog.value = false }
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            )
        }

        if (state.successStatusChanged == true){
            openDialog.value = false
            Toast.makeText(context, "Status changed successfully", Toast.LENGTH_SHORT).show()
        }

    }

}


