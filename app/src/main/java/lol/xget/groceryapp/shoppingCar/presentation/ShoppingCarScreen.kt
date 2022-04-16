package lol.xget.groceryapp.shoppingCar.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.shoppingCar.presentation.Components.ShoppingCartItems
import kotlin.math.roundToInt

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

    val shoppingCarItems = viewModel._shopCartItems

    val itemsPrice = viewModel.totalItemsPrice.value

    val totalItemsInCart = viewModel.totalItems.collectAsState(initial = 0)

    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .nestedScroll(nestedScrollConnectionPx),
        backgroundColor = MaterialTheme.colors.background,
        scaffoldState = scaffoldState,
        content = {
            Box(
            ) {
                Column() {
                    Row(
                        //topBar
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
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


            }

        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(75.dp)
                    .offset {
                        IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt())
                    },
                cutoutShape = RoundedCornerShape(50),
            ) {

                Row(
                    horizontalArrangement = Arrangement.End,
                ) {

                    Text(
                        text = "Total: $itemsPrice",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .width(200.dp)
                    )

                    Spacer(modifier = Modifier.width(100.dp))



                    Button(
                        onClick = { /*TODO*/ },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onSecondary
                        ),
                    ) {
                        Text(text = "Checkout")
                    }
                }


            }

        }
    )
}
