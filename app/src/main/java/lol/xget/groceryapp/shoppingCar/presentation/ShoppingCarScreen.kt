package lol.xget.groceryapp.shoppingCar.presentation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.TablerIcons
import lol.xget.groceryapp.shoppingCar.presentation.Components.ShoppingCartItems
import lol.xget.groceryapp.ui.GroceryAppTheme

@Composable
fun ShoppingCarScreen() {

    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
    ) {
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
                        text = "My Cart (4)",
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


                //TODO(ADD spacers between items , implement bottom checkout button )
                LazyColumn(
                    modifier = Modifier.padding(20.dp),
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    item {
                        ShoppingCartItems()
                        ShoppingCartItems()
                        ShoppingCartItems()
                        ShoppingCartItems()
                        ShoppingCartItems()
                        ShoppingCartItems()
                        ShoppingCartItems()
                        ShoppingCartItems()

                    }
                }
            }


        }


    }
}

@Preview
@Composable
private fun preview() {
    GroceryAppTheme {
        ShoppingCarScreen()
    }
}