package lol.xget.groceryapp.presentation.main.Seller.Home.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import lol.xget.groceryapp.R
import lol.xget.groceryapp.mainSeller.domain.ProductModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductBottomSheetDetail(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    product: ProductModel?,
    onClick: () -> Unit,
    onDetailClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    if (product != null) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp)
                        .background(color = MaterialTheme.colors.onSurface)
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {

                            IconButton(onClick = {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back arrow",
                                    tint = MaterialTheme.colors.primaryVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(67.dp))
                            Text(
                                text = "Product Details",
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier.padding(top = 14.dp),
                                fontSize = 20.sp,
                                fontWeight = Bold
                            )
                            Spacer(modifier = Modifier.width(40.dp))
                            IconButton(onClick = onDetailClick) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Icon",
                                    tint = MaterialTheme.colors.primaryVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(onClick = onClick) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete Icon",
                                    tint = MaterialTheme.colors.primaryVariant
                                )
                            }
                        }


                        product?.productPhoto?.let {
                            Card(

                                modifier = Modifier
                                    .size(100.dp)
                                    .testTag(tag = "circle")
                                    .padding(5.dp)
                                    .align(Alignment.CenterHorizontally),
                                shape = CircleShape,
                                elevation = 12.dp
                            ) {
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
                        product?.discountNote?.let {
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
                                        .align(Alignment.Start)
                                )
                            }

                        }
                        product?.productTitle?.let {
                            Text(
                                text = it,
                                fontWeight = Bold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 4.dp)
                            )

                        }
                        product.productDescription?.let {
                            Text(
                                text = it,
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 4.dp)
                            )
                        }
                        product.productCategory?.let {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 4.dp)
                            )
                        }

                        product.productQuantity?.let {
                            Text(
                                text = it,
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 4.dp)
                            )
                        }

                        if (!product.discountPrice.isNullOrBlank()) {
                            Text(
                                text = product.discountPrice + "$",
                                fontSize = 15.sp,
                                color = Color.Green,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            product?.productPrice?.let {
                                Text(
                                    text = "$it $", style = TextStyle(
                                        textDecoration = TextDecoration.LineThrough
                                    ), color = Color.LightGray, fontSize = 13.sp,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(start = 4.dp, bottom = 10.dp)
                                )
                            }

                        } else {
                            product?.productPrice?.let {
                                Text(
                                    text = "$it $",
                                    color = Color.LightGray,
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(start = 4.dp, bottom = 10.dp)
                                )
                            }

                        }

                    }
                }

            }){}
    }

}


