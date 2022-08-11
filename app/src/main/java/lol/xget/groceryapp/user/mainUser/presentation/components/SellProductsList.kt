package lol.xget.groceryapp.user.mainUser.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import lol.xget.groceryapp.R
import lol.xget.groceryapp.ui.raleway
import java.util.*
import com.bumptech.glide.request.transition.Transition as Transition1

@Composable
fun SellProductsList(
    product: lol.xget.groceryapp.seller.mainSeller.domain.ProductModel,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
) {
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = 210f/3,  // 1/3
        endY = 620f
    )

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
            Glide.with(LocalContext.current).asBitmap().load(it).into(
                object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition1<in Bitmap>?) {
                        bitmapState.value = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
            bitmapState.value?.let {
                Box(){
                    Image(bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            sizeImage = coordinates.size
                        },
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.matchParentSize().background(gradient))
                }
            }


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
                fontSize = 22.sp,
                color = MaterialTheme.colors.background,
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
                                    color = MaterialTheme.colors.background,
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
                                    color = MaterialTheme.colors.background,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp,
                                )
                            ) {
                                append(product.productQuantity!!)
                            }

                        },
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.background,
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
                                    color = Color.Green,
                                )
                            ) {
                                append("$")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.background,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(product.productPrice!! + "/")
                            }
                        },
                        fontSize = 18.sp,
                        color =MaterialTheme.colors.background,
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
                        color = MaterialTheme.colors.background,
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