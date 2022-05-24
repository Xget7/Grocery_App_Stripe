package lol.xget.groceryapp.user.mainUser.presentation.components

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lol.xget.groceryapp.user.shoppingCar.domain.Order
import java.time.Instant
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun OrderList(
    order : Order,
    shopName : String,
    onClick: () -> Unit
) {
    val simple: DateFormat = SimpleDateFormat("dd MMM yyyy")



    Box(
        modifier = Modifier
            .height(117.dp)
            .fillMaxWidth()
            .padding(1.dp)
    ){
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            shape = RoundedCornerShape(5),
            backgroundColor = Color(0xFF1F2D42),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    order.orderId?.let {
                        Text(
                            text = "Order ID: $it",
                            fontSize = 15.sp,
                            color = MaterialTheme.colors.primaryVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(70.dp))

                    order.orderTime?.let {
                        Text(
                            text = simple.format(Date(it.toLong())),
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primaryVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //Name Of commerce
                    Text(
                        text = shopName,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    Spacer(modifier = Modifier.width(230.dp))
                    IconButton(onClick = {
                        onClick()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "Navigate Arrow")
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Name Of commerce
                    Text(
                        text = "Amount: $${order.orderCost}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    Spacer(modifier = Modifier.width(148.dp))
                    when(order.orderStatus){
                        "In Progress" -> {
                            Text(
                                text = "In progress",
                                fontSize = 16.sp,
                                color = Color(0xFF03A9F4),
                            )
                        }
                        "Cancelled" -> {
                            Text(
                                text = "Cancelled",
                                fontSize = 16.sp,
                                color = Color.Red,
                            )
                        }
                        else -> {
                            Text(
                                text = "Completed",
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.onSecondary,
                            )
                        }
                    }
                }

            }
        }

    }

}