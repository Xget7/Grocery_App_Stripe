package lol.xget.groceryapp.presentation.main.Seller.Home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import lol.xget.groceryapp.R


@Composable
fun ProductListItem(product: lol.xget.groceryapp.seller.mainSeller.domain.ProductModel, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(vertical = 1.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Card(
                modifier = Modifier
                    .size(70.dp)
                    .testTag(tag = "circle")
                    .padding(5.dp)
                    .align(Alignment.CenterVertically),
                shape = CircleShape,
                elevation = 12.dp
            ) {
                GlideImage(
                    imageModel = product.productPhoto!!,
                    // Crop, Fit, Inside, FillHeight, FillWidth, None
                    contentScale = ContentScale.Crop,
                    // shows an image with a circular revealed animation.
                    // shows a placeholder ImageBitmap when loading.
                    placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                    // shows an error ImageBitmap when the request failed.
                    error = ImageBitmap.imageResource(R.drawable.error)
                )
            }
            Column(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {

                product.discountNote?.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = it, style = TextStyle(
                                color = Color.White,
                            ),
                            modifier = Modifier
                                .background(
                                    color = Color(0x6B93FF87),
                                    shape = RoundedCornerShape(40),
                                )
                                .border(
                                    BorderStroke(1.dp, Color.Green),
                                    shape = RoundedCornerShape(40)
                                )
                                .padding(2.dp)
                        )
                    }

                }
                Text(
                    text = product.productTitle!!,
                    style = typography.h6,
                    color = MaterialTheme.colors.primaryVariant
                )

                Text(
                    text = product.productQuantity!!,
                    style = typography.caption,
                    color = MaterialTheme.colors.primaryVariant
                )




                Row {
                    if (!product.discountPrice.isNullOrBlank()) {
                        Text(
                            text = product.discountPrice + "$",
                            fontSize = 15.sp,
                            color = Color.Green
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.productPrice!! + " $", style = TextStyle(
                                textDecoration = TextDecoration.LineThrough
                            ), color = Color.LightGray, fontSize = 13.sp
                        )
                    } else {
                        Text(
                            text = product.productPrice!! + " $",
                            color = Color.LightGray,
                            fontSize = 15.sp
                        )
                    }


                }


            }


        }


    }
}

