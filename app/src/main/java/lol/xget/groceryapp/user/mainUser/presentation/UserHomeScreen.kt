package lol.xget.groceryapp.user.mainUser.presentation

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.yield
import lol.xget.groceryapp.GroceryApp
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.domain.util.Destinations.*
import lol.xget.groceryapp.ui.BottomNavigationBar
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.user.mainUser.presentation.components.AutoSliding
import lol.xget.groceryapp.user.mainUser.presentation.components.PickedStoresRow
import lol.xget.groceryapp.user.mainUser.presentation.components.ShopListItem


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

    val corotineScope = rememberCoroutineScope()


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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 8.dp)
            .background(MaterialTheme.colors.background)
    ) {

        Column(
            modifier = Modifier
                .padding(top = if (scrollUpState.value == true) 30.dp else 108.dp)
                .verticalScroll(scrollState)
        ) {
            if (viewModel.bannersFromShopListFilteredByRating().isNullOrEmpty()) {
                CircularProgressIndicator()
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

            if (viewModel.state.value.loading!!) {
                DialogBoxLoading()
            }

        }

        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.graphicsLayer {
                translationY = position
            }

        ) {
            Column {

                Row(
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start,
                    modifier = androidx.compose.ui.Modifier
                        .width(400.dp)
                        .height(35.dp)
                        .padding(top = 6.dp)
                ) {
                    Column() {
                        viewModel.userData.value.userName?.let {
                            androidx.compose.material.Text(
                                text = it,
                                modifier = androidx.compose.ui.Modifier
                                    .padding(start = 13.dp)
                                    .width(140.dp),
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontSize = 24.sp,
                                color = androidx.compose.ui.graphics.Color.White,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        Spacer(
                            modifier = androidx.compose.ui.Modifier.height(
                                10.dp
                            )
                        )
                        viewModel.userData.value.address?.let {
                            androidx.compose.material.Text(
                                text = it,
                                modifier = androidx.compose.ui.Modifier
                                    .padding(start = 13.dp)
                                    .width(100.dp),
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontSize = 16.sp,
                                color = androidx.compose.ui.graphics.Color.White,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                    Spacer(
                        modifier = androidx.compose.ui.Modifier.padding(
                            start = 130.dp
                        )
                    )
                    androidx.compose.material.IconButton(
                        onClick = {
                            navController.navigate(lol.xget.groceryapp.domain.util.Destinations.ProfileUserDestinations.route)
                        }
                    ) {
                        androidx.compose.material.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Person,
                            tint = androidx.compose.material.MaterialTheme.colors.onSecondary,
                            modifier = androidx.compose.ui.Modifier
                                .width(25.dp)
                                .height(25.dp),
                            contentDescription = "Edit icon",
                        )
                    }
                    androidx.compose.material.IconButton(
                        onClick = {
                            com.google.firebase.auth.FirebaseAuth.getInstance().signOut().let {
                                navControllerWithoutBNB.navigate(lol.xget.groceryapp.domain.util.Destinations.LoginDestinations.route)
                            }
                        },
                    ) {
                        androidx.compose.material.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Logout,
                            tint = androidx.compose.material.MaterialTheme.colors.onSecondary,
                            modifier = androidx.compose.ui.Modifier
                                .width(25.dp)
                                .height(25.dp),
                            contentDescription = "Logout icon",
                        )
                    }
                }
                Spacer(
                    modifier = androidx.compose.ui.Modifier.padding(
                        top = 20.dp
                    )
                )

                Surface(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    color = androidx.compose.material.MaterialTheme.colors.background,
                    shape = androidx.compose.foundation.shape.CircleShape,
                ) {
                    Row(modifier = androidx.compose.ui.Modifier.fillMaxWidth()) {
                        androidx.compose.material.OutlinedTextField(
                            modifier = androidx.compose.ui.Modifier.width(400.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = androidx.compose.ui.graphics.Color.White,
                            ),
                            colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = androidx.compose.material.MaterialTheme.colors.onSecondary,
                                leadingIconColor = androidx.compose.material.MaterialTheme.colors.onSecondary
                            ),
                            singleLine = true,
                            value = query,
                            onValueChange = {
                                viewModel.onQueryChanged(it)
                                viewModel.newSearch()
                            },
                            label = {
                                androidx.compose.material.Text(
                                    text = "Search",
                                    color = androidx.compose.material.MaterialTheme.colors.primaryVariant
                                )
                            },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                                imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                            ),
                            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                                //TODO()ONBack = charge all normaly
                                onDone = {
                                    keyboardController?.hide()
                                }
                            ),
                            leadingIcon = {
                                androidx.compose.material.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Filled.Search,
                                    contentDescription = "search",
                                    tint = androidx.compose.material.MaterialTheme.colors.primaryVariant
                                )
                            },

                            )
                        androidx.activity.compose.BackHandler() {
                            keyboardController?.hide()
                        }
                    }

                }

            }
        }

    }

}




