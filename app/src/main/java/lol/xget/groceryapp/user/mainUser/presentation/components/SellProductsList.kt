package lol.xget.groceryapp.user.mainUser.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import lol.xget.groceryapp.R
import lol.xget.groceryapp.ui.raleway
import java.util.*

@Composable
fun SellProductsList(
    product: lol.xget.groceryapp.seller.mainSeller.domain.ProductModel,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
) {

    Card(
        modifier = Modifier
            .padding(14.dp)
            .width(42.dp)
            .height(210.dp)
            .clickable(onClick = onClick),
        elevation = 12.dp,
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(20.dp),

        ) {
        product.productPhoto?.let {
            GlideImage(
                imageModel = it,
                contentScale = ContentScale.Crop,
                placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                // shows an error ImageBitmap when the request failed.
                error = ImageBitmap.imageResource(R.drawable.error),
                modifier = Modifier.background(color = Color(0x56757575)),
                circularReveal = CircularReveal(1)
            )
        }



        Column(
            verticalArrangement = Arrangement.Center
        ) {
            //appply discount
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .width(155.dp)
                    .height(170.dp)
                    .padding(start = 15.dp, top = 140.dp),
                text = product.productTitle!!.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                },
                fontSize = 20.sp,
                color = Color.White,
                style = TextStyle(
                    fontFamily = raleway,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )

            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                //discounts
                if (product.discountAvailable != null && product.discountAvailable == true) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.onSecondary,
                                )
                            ) {
                                append("$")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.primaryVariant,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(product.discountPrice!! + "/")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            ) {
                                append(product.productPrice!!)
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.primaryVariant,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp,
                                )
                            ) {
                                append(product.productQuantity!!)
                            }

                        },
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 15.dp, top = 5.dp),
                        style = TextStyle(
                            fontFamily = raleway,
                            fontWeight = FontWeight.ExtraBold
                        ),
                    )


                }else{
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.onSecondary,
                                )
                            ) {
                                append("$")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.primaryVariant,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(product.productPrice!! + "/")
                            }
                        },
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 15.dp, top = 5.dp),
                        style = TextStyle(
                            fontFamily = raleway,
                            fontWeight = FontWeight.ExtraBold
                        ),
                    )

                    Text(
                        text = "" + product.productQuantity!!,
                        fontSize = 13.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 0.dp, top = 9.dp)
                            .alignByBaseline(),
                        style = TextStyle(
                            fontFamily = raleway,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                }


            }


        }

        IconButton(
            onClick = onAddToCart,
            modifier = Modifier
                .padding(top = 170.dp, start = 110.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Cart icon",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colors.onSecondary
            )
        }
    }
}