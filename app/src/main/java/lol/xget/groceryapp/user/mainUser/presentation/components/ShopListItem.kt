package lol.xget.groceryapp.user.mainUser.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import lol.xget.groceryapp.R


@Composable
fun ShopListItem(shop: lol.xget.groceryapp.seller.mainSeller.domain.ShopModel, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 2.dp,
        backgroundColor = Color(0xFF1d1c37),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Card(
                modifier = Modifier
                    .size(80.dp)
                    .testTag(tag = "circle")
                    .padding(8.dp)
                    .align(Alignment.CenterVertically),
                shape = CircleShape,
                elevation = 12.dp
            ) {

                GlideImage(
                    imageModel = shop.profilePhoto,
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
                    .padding(10.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = shop.shopName!!,
                        style = typography.h4,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.width(200.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    shop.shopOpen?.let {
                        if (!it) {
                            Text(
                                text = "Closed", style = TextStyle(
                                    color = Color.White,
                                ),
                                modifier = Modifier
                                    .background(
                                        color = Color.Red,
                                        shape = RoundedCornerShape(40),
                                    )
                                    .border(
                                        BorderStroke(1.dp, Color.Red),
                                        shape = RoundedCornerShape(40)
                                    )
                            )
                        } else {
                            Text(
                                text = "Open", style = TextStyle(
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
                            )
                        }

                    }
                }

                Text(
                    text = shop.phone!!,
                    style = typography.h6,
                    color = MaterialTheme.colors.primaryVariant
                )

                Text(
                    text = shop.address!!,
                    style = typography.caption,
                    color = MaterialTheme.colors.primaryVariant
                )


            }


        }


    }
}
