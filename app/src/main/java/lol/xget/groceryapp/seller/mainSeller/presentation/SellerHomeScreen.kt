package lol.xget.groceryapp.seller.mainSeller.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.presentation.main.Seller.Home.ProductListItem
import lol.xget.groceryapp.presentation.main.Seller.Home.products.ProductBottomSheetDetail
import lol.xget.groceryapp.ui.components.DialogBoxLoading

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
            .background(MaterialTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart,
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                    )
                    .fillMaxWidth()
                    .height(134.dp)


            ) {
                Card(
                    modifier = Modifier
                        .size(80.dp)
                        .testTag(tag = "circle")
                        .padding(5.dp, top = 10.dp),
                    shape = CircleShape,
                    elevation = 12.dp
                ) {
                    GlideImage(
                        imageModel = viewModel.shopPhoto.value,
                        // Crop, Fit, Inside, FillHeight, FillWidth, None
                        contentScale = ContentScale.Crop,
                        // shows an image with a circular revealed animation.
                        // shows a placeholder ImageBitmap when loading.
                        placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                        // shows an error ImageBitmap when the request failed.
                        error = ImageBitmap.imageResource(R.drawable.error)
                    )

                }

                Column {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .width(300.dp)
                            .height(25.dp)
                            .padding(top = 6.dp)
                    ) {
                        Text(
                            text = viewModel.userName.value,
                            modifier = Modifier
                                .padding(start = 0.dp)
                                .width(60.dp),
                            fontWeight = FontWeight.Bold, fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.padding(start = 100.dp))
                        IconButton(
                            onClick = {
                                navController.navigate(Destinations.SellerAccountDestinations.route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                tint = Color.White,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                contentDescription = "Configuration icon",
                            )
                        }
                        IconButton(
                            onClick = {
                                navController.navigate(Destinations.SellerAddProductDestinations.route)

                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddShoppingCart,
                                tint = Color.White,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                contentDescription = "ShoppingCar icon",
                            )
                        }
                        IconButton(
                            onClick = {
                                FirebaseAuth.getInstance().signOut().let {
                                    navController.navigate(Destinations.LoginDestinations.route)
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                tint = Color.White,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                contentDescription = "Logout icon",
                            )
                        }
                    }


                    Text(
                        text = viewModel.shopName.value,
                        modifier = Modifier.padding(top = 3.dp, start = 16.dp),
                        fontSize = 14.sp, color = Color.White
                    )
                    user?.let {
                        Text(
                            text = it.email.toString(),
                            modifier = Modifier.padding(top = 3.dp, start = 16.dp),
                            fontSize = 14.sp, color = Color.White
                        )
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))
            }


        }
        Row(
            modifier = Modifier.padding(top = 86.dp, start = 30.dp, end = 30.dp)
        ) {
            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(50)
                    )
                    .width(165.dp)
                    .height(40.dp)

            ) {
                Text(text = "Products", textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.width(5.dp))

            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(50)
                    )
                    .width(165.dp)
                    .height(40.dp),
            ) {
                Text(text = "Orders", textAlign = TextAlign.Center)
            }

        }


    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Column {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.background,
                elevation = 8.dp,
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        modifier = Modifier.width(400.dp),
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
                            Text(text = "Search", color = MaterialTheme.colors.primaryVariant)
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



        if (openDialog) {
            DialogBoxLoading()
        }

        if (viewModel.state.value.loading == true) {
            DialogBoxLoading()
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


}


