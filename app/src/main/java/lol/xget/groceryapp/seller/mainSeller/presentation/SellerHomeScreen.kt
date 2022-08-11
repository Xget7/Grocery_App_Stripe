package lol.xget.groceryapp.seller.mainSeller.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.presentation.main.Seller.Home.ProductListItem
import lol.xget.groceryapp.presentation.main.Seller.Home.products.ProductBottomSheetDetail

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable

fun SellerHomeScreen(
    navController: NavController,
    viewModel: SellerHomeViewModel = hiltViewModel()
) {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val openDialog by viewModel.open.observeAsState(false)

    val productClicked = viewModel.productClicked

    val corotineScope = rememberCoroutineScope()
    val user = FirebaseAuth.getInstance().currentUser
    val lazyListState = rememberLazyListState()
    val query = viewModel.query.value
    val keyboardController = LocalSoftwareKeyboardController.current





    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(MaterialTheme.colors.background),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp)
                .background(MaterialTheme.colors.background)
        ) {
            Column {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colors.background,
                    elevation = 2.dp,
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            textStyle = TextStyle(
                                color = Color.White,
                            ),
                            singleLine = true,
                            value = query,
                            onValueChange = {
                                viewModel.onQueryChanged(it)
                            },
                            label = {
                                Text(
                                    text = "Search",
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    viewModel.newSearch(query)
                                    keyboardController?.hide()
                                }
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search",
                                    tint = MaterialTheme.colors.primaryVariant
                                )
                            },

                            )
                    }
                }

                if (viewModel.myList.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No products found",
                            style = TextStyle(
                                color = MaterialTheme.colors.primaryVariant,
                                fontSize = 20.sp
                            )
                        )
                    }
                }else{
                    LazyColumn(
                        state = lazyListState,
                        contentPadding = PaddingValues(vertical = 4.dp),
                    ) {
                        items(viewModel.myList) {
                            ProductListItem(product = it) {
                                viewModel.currentItem.value = it
                                corotineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }
                        }
                    }
                }


                ProductBottomSheetDetail(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    product = viewModel.currentItem.value,
                    onClick = {
                        viewModel.deleteProduct(viewModel.currentItem.value.productId!!)
                        corotineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    },
                    onDetailClick = {
                        //navigate to edit screen with product detail
                        navController.navigate(Destinations.SellerEditProductDestinations.route + "/${viewModel.currentItem.value.productId!!}")
                    }
                )


            }
            if (viewModel.state.value.loading == true) {
                CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
            }

            if (viewModel.state.value.errorMsg != null) {
                EventDialog(
                    errorMessage = viewModel.state.value.errorMsg,
                    onDismiss = { viewModel.hideErrorDialog() })
            }

            if (viewModel.user == null) {
                navController.navigate(Destinations.LoginDestinations.route)
            }


        }
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom, modifier = Modifier
            .fillMaxSize()
            .padding(26.dp)) {
            FloatingActionButton(onClick = {
                                           navController.navigate(Destinations.SellerAddProductDestinations.route)
            }, backgroundColor = MaterialTheme.colors.onSecondary) {
                Icon(imageVector = Icons.Default.Add, contentDescription ="Add Product", tint = MaterialTheme.colors.background )
            }
        }

    }


}



