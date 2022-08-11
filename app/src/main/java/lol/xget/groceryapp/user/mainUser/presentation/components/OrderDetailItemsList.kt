package lol.xget.groceryapp.user.mainUser.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lol.xget.groceryapp.data.localdb.CartItems

@Composable
fun OrderDetailItemsList(
    item : CartItems
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            ,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp,
        shape = RoundedCornerShape(10)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            item.itemName?.let{
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primaryVariant
                    ),
                    modifier = Modifier.width(200.dp)
                )
            }

            Row {
                item.itemPriceEach?.let {
                    Text(
                        text = "$$it",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.primaryVariant
                        ),
                        modifier = Modifier.width(200.dp)
                    )

                }
                item.itemAmount?.let {
                    Text(
                        text = "Amount  [$it]",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.primaryVariant
                        ),
                        modifier = Modifier.width(200.dp)
                    )
                }


            }


        }

    }

}