package lol.xget.groceryapp.user.mainUser.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.landscapist.glide.GlideImage
import com.talhafaki.composablesweettoast.theme.whiteBackground
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.yield
import lol.xget.groceryapp.GroceryApp
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.auth.login.presentation.components.TransparentTextField
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.domain.util.Destinations.*
import lol.xget.groceryapp.ui.BottomNavigationBar
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.mainUser.presentation.components.AutoSliding
import lol.xget.groceryapp.user.mainUser.presentation.components.PickedStoresRow
import lol.xget.groceryapp.user.mainUser.presentation.components.ShopListItem


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun UserHomeScreen(
    navController: NavController,
    navControllerWithoutBNB: NavController,
    viewModel: UserHomeScreenViewModel = hiltViewModel()
) {
    val scrollLazyListState = rememberLazyListState()

    val scrollUpState = viewModel.scrollUp.observeAsState()

    val scrollState = rememberScrollState()

    viewModel.updateScrollPosition(scrollState.value)
    val query = viewModel.query.value

    val lazyListState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current

    var shopClicked = remember {
        mutableStateOf(false)
    }




    val position by animateFloatAsState(
        if (scrollUpState.value == true)
            -320f
        else
            0f
    )

    if (viewModel.state.value.loading == true) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
        }
    }else{
        Scaffold(
            topBar = {
                Surface(
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.graphicsLayer {
                        translationY = position
                    }

                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = androidx.compose.ui.Modifier
                                .width(400.dp)
                                .height(45.dp)
                                .padding(top = 6.dp)
                        ) {
                            Column() {
                                viewModel.userData.value.userName?.let {
                                    Text(
                                        text = it,
                                        modifier = androidx.compose.ui.Modifier
                                            .padding(start = 13.dp)
                                            .width(150.dp).height(30.dp),
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        fontSize = 24.sp,
                                        color = MaterialTheme.colors.background,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis

                                    )
                                }
                                Spacer(
                                    modifier = androidx.compose.ui.Modifier.height(
                                        10.dp
                                    )
                                )
                                viewModel.userData.value.address?.let {
                                    Text(
                                        text = it,
                                        modifier = androidx.compose.ui.Modifier
                                            .padding(start = 13.dp)
                                            .width(100.dp),
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(
                                modifier = Modifier.padding(
                                    start = 130.dp
                                )
                            )
                            IconButton(
                                onClick = {
                                    navController.navigate(ProfileUserDestinations.route)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    tint = MaterialTheme.colors.background,
                                    modifier = androidx.compose.ui.Modifier
                                        .width(25.dp)
                                        .height(25.dp),
                                    contentDescription = "Edit icon",
                                )
                            }
                            IconButton(
                                onClick = {
                                    FirebaseAuth.getInstance().signOut().let {
                                        navControllerWithoutBNB.navigate(lol.xget.groceryapp.domain.util.Destinations.LoginDestinations.route)
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    tint = MaterialTheme.colors.background,
                                    modifier = androidx.compose.ui.Modifier
                                        .width(25.dp)
                                        .height(25.dp),
                                    contentDescription = "Logout icon",
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier.padding(
                                top = 10.dp
                            )
                        )

                        Row(
                            modifier = Modifier
                                .height(55.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.onSecondary),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(360.dp),
                                shape = CircleShape,
                                backgroundColor = MaterialTheme.colors.onSecondary
                            ) {
                                Row(
                                    Modifier
                                        .width(360.dp)
                                        .background(MaterialTheme.colors.background),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    TextField(
                                        modifier = Modifier
                                            .width(360.dp)
                                            .height(55.dp),
                                        shape = CircleShape,
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colors.primaryVariant,
                                        ),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = MaterialTheme.colors.onSecondary,
                                            leadingIconColor = MaterialTheme.colors.onSecondary
                                        ),
                                        singleLine = true,
                                        value = query,
                                        onValueChange = {
                                            viewModel.onQueryChanged(it)
                                            viewModel.newSearch()
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
                                            //TODO()ONBack = charge all normaly
                                            onDone = {
                                                keyboardController?.hide()
                                            }
                                        ),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = androidx.compose.material.icons.Icons.Filled.Search,
                                                contentDescription = "search",
                                                tint = MaterialTheme.colors.primaryVariant
                                            )
                                        },

                                        )
                                    androidx.activity.compose.BackHandler() {
                                        keyboardController?.hide()
                                    }
                                }


                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))



                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 13.dp, end = 8.dp)
                ) {

                    if (viewModel.bannersFromShopListFilteredByRating().isNullOrEmpty()) {
                        CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
                    } else {
                        AutoSliding(
                            banners = viewModel.bannersFromShopListFilteredByRating()
                        )
                    }

                    //TOp stores  = score < 4
                    if (!viewModel.state.value.searchError!!) {
                        Row(
                            modifier = Modifier
                                .height(30.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.recommendation),
                                contentDescription = "Recommendated stores"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(

                                text = "Recommended Stores For You",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primaryVariant
                                ),
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            state = lazyListState,
                            contentPadding = PaddingValues(
                                vertical = 4.dp
                            )
                        ) {
                            items(viewModel.shopListFilteredByLocation) {
                                PickedStoresRow(shop = it) {
                                    viewModel.currentItem.value = it
                                    shopClicked.value = true
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Divider()

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "All Stores",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        LazyColumn(
                            state = scrollLazyListState,
                            modifier = Modifier.height(128.times(viewModel.shopList.size).dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                        ) {
                            items(viewModel.shopList) {
                                Spacer(modifier = Modifier.height(8.dp))
                                ShopListItem(shop = it) {
                                    viewModel.currentItem.value = it
                                    shopClicked.value = true
                                }
                            }
                        }

                        if (shopClicked.value) {
                            shopClicked.value = false
                            navController.navigate(ShopDetailDestinations.route + "/${viewModel.currentItem.value.uid!!}")

                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "Error",
                                tint = Color.Red
                            )
                            Text(text = "Shop not found ", color = Color.Red)
                        }

                    }

                    if (viewModel.state.value.errorMsg != null) {
                        EventDialog(
                            errorMessage = viewModel.state.value.errorMsg,
                            onDismiss = { viewModel.hideErrorDialog() })
                    }





                }



            }
        }

    }


}




