package lol.xget.groceryapp.user.mainUser.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Index
import lol.xget.groceryapp.user.shoppingCar.domain.Order

@Composable
fun OrderList(
    order : Order
) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
    ){
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Order ID: ${order.orderId}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant,

                    }

                )

            }

        }
    }

}