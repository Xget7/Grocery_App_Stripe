package lol.xget.groceryapp.user.shoppingCar.presentation.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import compose.icons.TablerIcons
import compose.icons.tablericons.Minus
import lol.xget.groceryapp.R
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.ui.LightGreen
import lol.xget.groceryapp.ui.raleway

@Composable
fun ShoppingCartItems(
    product: CartItems,
    onClickAdd: () -> Unit,
    onDeleteAdd: () -> Unit
) {

    Box(
        Modifier.fillMaxWidth().padding(top = 10.dp)
    ) {
        Card(
            modifier = Modifier
                .padding(vertical = 1.dp)
                .width(270.dp)
                .clickable(onClick = {}),
            elevation = 12.dp,
            backgroundColor = Color(0xFF232149),
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(300.dp)
            ) {

                Card(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(5.dp)
                        .align(Alignment.CenterVertically),
                    shape = RoundedCornerShape(10),
                    elevation = 2.dp
                ) {
                    product.itemPhoto?.let {
                        GlideImage(
                            imageModel = it,
                            // Crop, Fit, Inside, FillHeight, FillWidth, None
                            contentScale = ContentScale.Crop,
                            // shows an image with a circular revealed animation.
                            // shows a placeholder ImageBitmap when loading.
                            placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                            // shows an error ImageBitmap when the request failed.
                            error = ImageBitmap.imageResource(R.drawable.error)
                        )
                    }

                }

                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {

                    Text(
                        modifier = Modifier.padding(top = 15.dp),
                        text = product.itemName,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = product.itemPriceEach + "/" + product.itemQuantity,
                        style = MaterialTheme.typography.caption,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )

                    //Add remove btn
                    Row(
                    ) {

                        IconButton(
                            onClick = onDeleteAdd,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            //enabled = productAmount.value >= 1
                        ) {
                            Icon(
                                imageVector = TablerIcons.Minus,
                                tint = Color(0xFF00FF00),
                                contentDescription = "Remove Item",
                                modifier = Modifier
                                    .background(
                                        color = LightGreen,
                                        shape = CircleShape
                                    )
                                    .height(26.dp)
                                    .width(40.dp)
                                    .alignByBaseline()
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            text = product.itemAmount.toString(),
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 0.dp)
                                .align(Alignment.CenterVertically),
                            style = TextStyle(
                                fontFamily = raleway,
                                fontWeight = FontWeight.ExtraBold
                            ),
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        IconButton(
                            onClick = onClickAdd,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                tint = Color(0xFF00FF00),
                                contentDescription = "Add Item",
                                modifier = Modifier
                                    .background(
                                        color = LightGreen,
                                        shape = CircleShape
                                    )
                                    .height(26.dp)
                                    .width(40.dp)
                                    .alignByBaseline()

                            )
                        }


                    }


                }
            }


        }

        Text(
            text = product.itemPriceTotal,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 2.dp),
            color = MaterialTheme.colors.primaryVariant,

            )
    }


}

