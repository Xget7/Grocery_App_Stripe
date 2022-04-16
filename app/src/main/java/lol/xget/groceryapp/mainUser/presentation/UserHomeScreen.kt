package lol.xget.groceryapp.mainUser.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.R
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.login.presentation.components.EventDialog
import lol.xget.groceryapp.mainUser.presentation.components.ShopListItem


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun UserHomeScreen(
    navController: NavController,
    viewModel: UserHomeScreenViewModel = hiltViewModel()
) {

    TODO("MAKE NAVBAR PRO")

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val corotineScope = rememberCoroutineScope()

    val user = FirebaseAuth.getInstance().currentUser


    val lazyListState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current

    var shopClicked = remember {
        mutableStateOf(false)
    }

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
                        imageModel = viewModel.userData.value.profilePhoto,
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
                        viewModel.userData.value.userName?.let{
                            Text(
                                text = it,
                                modifier = Modifier
                                    .padding(start = 13.dp)
                                    .width(60.dp),
                                fontWeight = FontWeight.Bold, fontSize = 16.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }


                        Spacer(modifier = Modifier.padding(start = 100.dp))
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.ProfileUserScreen.route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                tint = MaterialTheme.colors.onSecondary,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                contentDescription = "Edit icon",
                            )
                        }
                        IconButton(
                            onClick = {
                                FirebaseAuth.getInstance().signOut().let {
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                tint = MaterialTheme.colors.onSecondary,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                contentDescription = "Logout icon",
                            )
                        }
                    }

                    user?.let {
                        Text(
                            text = it.email.toString(),
                            modifier = Modifier.padding(top = 3.dp, start = 16.dp),
                            fontSize = 14.sp, color = Color.White
                        )
                    }

                    viewModel.userData.value.phone?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 5.dp, start = 16.dp),
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
                onClick = { /*TODO()*/ },
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.onSecondary,
                        shape = RoundedCornerShape(50)
                    )
                    .width(165.dp)
                    .height(40.dp)

            ) {
                Text(text = "Products", textAlign = TextAlign.Center, color =  MaterialTheme.colors.primaryVariant)
            }

            Spacer(modifier = Modifier.width(5.dp))

            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.onSecondary,
                        shape = RoundedCornerShape(50)
                    )
                    .width(165.dp)
                    .height(40.dp),
            ) {
                Text(text = "Orders", textAlign = TextAlign.Center, color =  MaterialTheme.colors.primaryVariant)
            }

        }


    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 130.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Column {

            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(vertical = 4.dp),
            ) {
                items(viewModel.shopList) {
                    ShopListItem(shop = it) {
                        viewModel.currentItem.value = it
                        shopClicked.value = true
                    }
                }
            }

            if (shopClicked.value){
                shopClicked.value = false
                navController.navigate(Screen.ShopDetailScreen.route + "/${viewModel.currentItem.value.uid!!}")
            }


            if (viewModel.state.value.errorMsg != null) {
                EventDialog(
                    errorMessage = viewModel.state.value.errorMsg,
                    onDismiss = { viewModel.hideErrorDialog() })
            }




        }

    }
}


