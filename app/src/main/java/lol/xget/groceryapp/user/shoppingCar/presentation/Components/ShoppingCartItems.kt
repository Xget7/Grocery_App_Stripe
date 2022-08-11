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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import compose.icons.TablerIcons
import compose.icons.tablericons.Minus
import compose.icons.tablericons.Trash
import lol.xget.groceryapp.R
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.ui.LightGreen
import lol.xget.groceryapp.ui.raleway

@Composable
fun ShoppingCartItems(
    product: CartItems,
    onClickAdd: () -> Unit,
    onDeleteAdd: () -> Unit,
    onDeleteAll: () -> Unit,
) {

    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable(onClick = {}),
            elevation = 12.dp,
            backgroundColor = MaterialTheme.colors.background,
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(300.dp)
            ) {

                Card(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(5.dp).padding(start = 10.dp)
                        .align(Alignment.CenterVertically),
                    shape = RoundedCornerShape(30),
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
                        text = product.itemName!!.replaceFirst(
                            product.itemName?.first()!!,
                            product.itemName?.first()!!.uppercaseChar()
                        ),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(3.dp))


                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 19.sp,
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            ) {
                                append("$${product.itemPriceEach}")
                            }
                            Spacer(modifier = Modifier.width(2.dp))
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 16.sp
                                )
                            ) {
                                append(" /${product.itemQuantity}")
                            }
                        },
                        style = MaterialTheme.typography.caption,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )

                    //Add remove btn
                    Row(
                        Modifier.fillMaxWidth().padding(end = 20.dp),
                        horizontalArrangement = Arrangement.End
                    ) {

                        IconButton(
                            onClick = onDeleteAll,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        ) {
                            Icon(
                                imageVector = TablerIcons.Trash,
                                tint = Color.LightGray,
                                contentDescription = "Remove All items",
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colors.background,
                                        shape = CircleShape,
                                    )
                                    .height(26.dp)
                                    .width(26.dp)
                                    .alignByBaseline()
                            )
                        }

                        IconButton(
                            onClick = onDeleteAdd,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            //enabled = productAmount.value >= 1
                        ) {
                            Icon(
                                imageVector = TablerIcons.Minus,
                                tint = MaterialTheme.colors.background,
                                contentDescription = "Remove Item",
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colors.onSecondary,
                                        shape = CircleShape
                                    )
                                    .height(20.dp)
                                    .width(20.dp)
                                    .alignByBaseline()
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = product.itemAmount.toString(),
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.primaryVariant,
                            modifier = Modifier
                                .padding(bottom = 3.dp)
                                .align(Alignment.CenterVertically),
                            style = TextStyle(
                                fontFamily = raleway,
                                fontWeight = FontWeight.ExtraBold
                            ),
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = onClickAdd,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                tint = MaterialTheme.colors.background,
                                contentDescription = "Add Item",
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colors.onSecondary,
                                        shape = CircleShape
                                    )
                                    .height(20.dp)
                                    .width(20.dp)
                                    .alignByBaseline()

                            )
                        }


                    }


                }
            }


        }


    }


}

