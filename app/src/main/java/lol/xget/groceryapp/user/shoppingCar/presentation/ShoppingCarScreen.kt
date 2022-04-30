package lol.xget.groceryapp.user.shoppingCar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.talhafaki.composablesweettoast.main.SweetToast
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetError
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import kotlinx.coroutines.launch
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.shoppingCar.presentation.Components.ShoppingCartItems

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingCarScreen(
    navController: NavController,
    viewModel: ShoppingCarViewModel = hiltViewModel()
) {

    val bottomBarHeight = 85.dp
    val bottomBarHeightPx = with(LocalDensity.current) {
        bottomBarHeight.roundToPx().toFloat()
    }
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnectionPx = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.value + delta
                bottomBarOffsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)

    val drawerState = rememberDrawerState(DrawerValue.Open)
    //snackbar
    val snackbarState = SnackbarHostState()

    val shoppingCarItems = viewModel._shopCartItems

    val subTotal = viewModel.totalItemsPrice.value
    val deliveryFee  = viewModel.currentShop.value.deliveryFee
    val totalPrice  = viewModel.currentShop.value.deliveryFee?.let {
        it.toFloat() + subTotal.toFloat()
    }.toString()

    val totalItemsInCart = viewModel.totalItems

    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .nestedScroll(nestedScrollConnectionPx),
        backgroundColor = MaterialTheme.colors.background,
        scaffoldState = scaffoldState,
        ) {
        val coroutineScope = rememberCoroutineScope()

        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 70.dp,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(color = Color.Black)
                ) {
                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Info , contentDescription = "More info" , tint = MaterialTheme.colors.primaryVariant)
                            }
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 24.sp
                                    )
                                    ) {
                                        append("Total: ")
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Green)) {
                                        append("$")
                                    }
                                    withStyle(style = SpanStyle(
                                        fontWeight = FontWeight.Light,
                                        fontSize = 20.sp
                                    )
                                    ) {
                                        append("$totalPrice")
                                    }
                                },
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colors.primaryVariant,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(176.dp)
                            )

                            Spacer(modifier = Modifier.width(30.dp))

                            Button(
                                onClick = {
                                          viewModel.placeOrder()
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.onSecondary
                                ),
                                modifier = Modifier
                                    .width(108.dp)
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = "Checkout",
                                    color =  MaterialTheme.colors.primaryVariant,
                                    fontSize = 15.sp,
                                )
                            }
                        }
                    }else{
                        Column(
                            verticalArrangement = Arrangement.Center

                        ) {
                            Spacer(modifier = Modifier.height(13.dp))
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.padding(start = 5.dp, end = 16.dp)

                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 20.sp
                                        )
                                        ) {
                                            append("Sub Total: ")
                                        }
                                        Spacer(modifier = Modifier.width(30.dp))

                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 20.sp
                                        )
                                        ) {
                                            append(if (subTotal == 0) "0.00" else totalPrice)
                                        }
                                    },
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colors.primaryVariant,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .width(226.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(18.dp))

                            Row (
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.padding(start = 5.dp, end = 16.dp)

                            ){
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 20.sp
                                        )
                                        ) {
                                            append("Delivery Fee: ")
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 20.sp
                                        )
                                        ) {
                                            append(if (deliveryFee == null) "0.00" else "$deliveryFee")
                                        }
                                    },
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colors.primaryVariant,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .width(246.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }) {
                                    Icon(imageVector = Icons.Default.ArrowCircleDown , contentDescription = "More info" , tint = MaterialTheme.colors.primaryVariant)
                                }

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 24.sp
                                        )
                                        ) {
                                            append("Total: ")
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Green,
                                            fontSize = 18.sp
                                        )
                                        ) {
                                            append("$")
                                        }
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 20.sp
                                        )
                                        ) {
                                            append(totalPrice )
                                        }
                                    },
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colors.primaryVariant,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .width(186.dp)
                                )

                                Spacer(modifier = Modifier.width(30.dp))

                                Button(
                                    onClick = {   viewModel.placeOrder() },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.onSecondary
                                    ),
                                    modifier = Modifier
                                        .width(108.dp)
                                        .height(50.dp)

                                ) {
                                    Text(
                                        text = "Checkout",
                                        color = MaterialTheme.colors.primaryVariant ,
                                        fontSize = 15.sp,
                                    )
                                }

                            }

                        }

                    }

                }

            }) {
            //-----------------------------------------------Lazy Colums , Rows ---------------------------------------------------\\
            Box(
            ) {
                
                Column() {
                    Row(
                        //topBar
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Back button",
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(30.dp))


                        Text(
                            text = "My Cart (${totalItemsInCart.value})",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primaryVariant,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(110.dp))

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home button",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colors.onSecondary
                            )
                        }


                    }

                    Divider(thickness = 2.dp, color = Color.LightGray)



                    LazyColumn(
                        modifier = Modifier.padding(20.dp),
                        state = lazyListState,
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(viewModel._shopCartItems) {

                            ShoppingCartItems(product = it,
                                onClickAdd = {
                                    viewModel.addItem(it)
                                },
                                onDeleteAdd = {
                                    viewModel.deleteItem(it)
                                }
                            )
                        }
                    }
                }

                viewModel.state.value.errorMsg?.let {
                    SweetError(message = it)
                }

                if (viewModel.state.value.displayPb == true) {
                    DialogBoxLoading()
                }

                //if state is success
                if (viewModel.state.value.successPlacedOrder == true) {
                    SweetSuccess(message = "Order Placed Successfully")
                    LaunchedEffect(viewModel.state.value.successPlacedOrder){
                        if (viewModel.state.value.successPlacedOrder == true) {
                            navController.navigateUp()
                        }
                    }

                }



            }
        }



    }
}
