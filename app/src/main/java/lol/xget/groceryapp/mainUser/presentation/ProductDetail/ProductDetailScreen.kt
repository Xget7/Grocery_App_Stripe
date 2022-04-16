package lol.xget.groceryapp.mainUser.presentation.ProductDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import compose.icons.TablerIcons
import compose.icons.tablericons.Minus
import lol.xget.groceryapp.R
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.login.presentation.components.EventDialog
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.ui.raleway

@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel : ProductDetailViewModel = hiltViewModel(),

    ) {

    val addedToCart = remember {
        mutableStateOf(false)
    }

    val productAmount = viewModel.currentProductAmount.collectAsState()
    val productFinalPrice = viewModel.productFinalPrice.collectAsState()


    //total product price

    Surface(color = White, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(200.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 25.dp, end = 26.dp)
            ) {

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
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                        ) {
                            append("${productFinalPrice.value}")
                        }
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 29.sp,
                    color = White,
                    modifier = Modifier
                        .height(30.dp)
                        .width(110.dp),
                    style = TextStyle(
                        fontFamily = raleway,
                        fontWeight = FontWeight.ExtraBold
                    ),
                )


                OutlinedButton(
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(40),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onSecondary
                    ),
                    onClick = {
                        val item = CartItems(
                            0,
                            itemId = viewModel.productId.value,
                            itemName = viewModel.productTitle.value,
                            itemPriceEach = viewModel.productPrice.value,
                            itemPriceTotal = productFinalPrice.value.toString(),
                            itemAmount = productAmount.value,
                            itemPhoto = viewModel.productPhoto.value,
                            itemQuantity =  viewModel.productQuantity.value
                        )

                        viewModel.addToCart(item).also {
                            addedToCart.value = true
                        }
                    },
                ) {


                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Add to cart",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Add to cart", color = White, fontSize = 18.sp)
                }
            }

        }
    }



    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .height(660.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(60.dp).copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize)

    ) {

        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(top = 20.dp)
            ) {

                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Arrow",
                        tint = MaterialTheme.colors.onSecondary
                    )
                }

                Spacer(modifier = Modifier.width(280.dp))

                IconButton(
                    onClick = {
                              navController.navigate(Screen.ShoppingCar.route)
                    },
                    modifier = Modifier.background(MaterialTheme.colors.onSecondary),
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping Car",
                        tint = MaterialTheme.colors.primaryVariant
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                GlideImage(
                    imageModel = viewModel.productPhoto.value,
                    contentScale = ContentScale.Crop,
                    placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                    error = ImageBitmap.imageResource(R.drawable.error),
                    modifier = Modifier
                        .background(color = Color(0x56757575))
                        .width(300.dp)
                        .height(300.dp),
                    circularReveal = CircularReveal(1)
                )


            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Text(
                    text = viewModel.productTitle.value,
                    style = TextStyle(
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 24.sp,
                        fontFamily = raleway,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 20.dp)

                )

                Text(
                    text = "Price Per " + viewModel.productQuantity.value,
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(top = 9.dp),
                    style = TextStyle(
                        fontFamily = raleway,
                        fontWeight = FontWeight.Medium
                    ),
                )

                Row(
                    //TODO("add discount option")
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(55.dp)
                ) {
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
                                ),
                            ) {
                                append(viewModel.productPrice.value)
                            }
                        },
                        textAlign = TextAlign.Start,
                        fontSize = 29.sp,
                        color = White,
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .height(33.dp)
                            .width(140.dp),
                        style = TextStyle(
                            fontFamily = raleway,
                            fontWeight = FontWeight.ExtraBold
                        ),
                    )

                    Spacer(modifier = Modifier.width(100.dp))

                    IconButton(
                        onClick = { viewModel.addItem() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            tint = MaterialTheme.colors.primaryVariant,
                            contentDescription = "Add Item",
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.onSecondary,
                                    shape = CircleShape
                                )
                                .height(23.dp)
                                .alignByBaseline()

                        )
                    }

                    Text(
                        text = productAmount.value.toString(),
                        fontSize = 23.sp,
                        color = White,
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .align(Alignment.CenterVertically),
                        style = TextStyle(
                            fontFamily = raleway,
                            fontWeight = FontWeight.ExtraBold
                        ),
                    )

                    IconButton(
                        onClick = { viewModel.deleteItem() },
                        modifier = Modifier.align(Alignment.CenterVertically),
                        enabled = productAmount.value >= 1
                    ) {
                        Icon(
                            imageVector = TablerIcons.Minus,
                            tint = MaterialTheme.colors.primaryVariant,
                            contentDescription = "Remove Item",
                            modifier = Modifier.background(
                                color = MaterialTheme.colors.onSecondary,
                                shape = CircleShape
                            )
                        )
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = viewModel.productDescription.value,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .fillMaxWidth()
                        .height(150.dp),
                    fontSize = 16.sp,
                    color = White,
                    style = TextStyle(
                        fontFamily = raleway,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 10
                )
            }


        }

        if (addedToCart.value){
            SweetSuccess(message = "Added to cart")
        }


        if (viewModel.state.value.loading!!) {
            DialogBoxLoading()
        }

        if (viewModel.state.value.errorMsg != null) {
            EventDialog(
                errorMessage = viewModel.state.value.errorMsg,
                onDismiss = { viewModel.hideErrorDialog() })
        }



    }


}
