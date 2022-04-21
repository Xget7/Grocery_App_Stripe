package lol.xget.groceryapp.user.mainUser.presentation

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.R
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.domain.util.Destinations.*
import lol.xget.groceryapp.login.presentation.components.EventDialog
import lol.xget.groceryapp.ui.BottomNavigationBar
import lol.xget.groceryapp.user.mainUser.presentation.components.ShopListItem


@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun UserHomeScreen(
    navController: NavController,
    viewModel: UserHomeScreenViewModel = hiltViewModel()
) {


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val corotineScope = rememberCoroutineScope()

    val user = FirebaseAuth.getInstance().currentUser

    val query = viewModel.query.value

    val lazyListState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current

    var shopClicked = remember {
        mutableStateOf(false)
    }

    val navigationItems = listOf(
        UserHomeDestinations,
        ProfileUserDestinations
    )

    Scaffold(
    ) {

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
                        .height(119.dp)


                ) {

                    Column {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .width(400.dp)
                                .height(35.dp)
                                .padding(top = 6.dp)
                        ) {
                            Column() {
                                viewModel.userData.value.userName?.let {
                                    Text(
                                        text = it,
                                        modifier = Modifier
                                            .padding(start = 13.dp)
                                            .width(140.dp),
                                        fontWeight = FontWeight.Bold, fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                viewModel.userData.value.address?.let {
                                    Text(
                                        text = it,
                                        modifier = Modifier
                                            .padding(start = 13.dp)
                                            .width(100.dp),
                                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }




                            Spacer(modifier = Modifier.padding(start = 130.dp))

                            IconButton(
                                onClick = {
                                    navController.navigate(ProfileUserDestinations.route)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    tint = MaterialTheme.colors.onSecondary,
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(25.dp),
                                    contentDescription = "Edit icon",
                                )
                            }
                            IconButton(
                                onClick = {
                                    FirebaseAuth.getInstance().signOut().let {
                                        navController.navigate(LoginDestinations.route)
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    tint = MaterialTheme.colors.onSecondary,
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(25.dp),
                                    contentDescription = "Logout icon",
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(top = 20.dp))

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            color = MaterialTheme.colors.background,
                            shape = CircleShape,
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    modifier = Modifier.width(400.dp),
                                    shape = CircleShape,
                                    textStyle = TextStyle(
                                        color = Color.White,
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
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "search",
                                            tint = MaterialTheme.colors.primaryVariant
                                        )
                                    },

                                    )
                            }
                        }

                    }
                }


            }
            Row(
                modifier = Modifier.padding(top = 86.dp, start = 30.dp, end = 30.dp)
            ) {

                Spacer(modifier = Modifier.width(5.dp))

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

                //TOp stores  = score < 4
                //nearby stores
                if (!viewModel.state.value.searchError!!){
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

                    if (shopClicked.value) {
                        shopClicked.value = false
                        navController.navigate(ShopDetailDestinations.route + "/${viewModel.currentItem.value.uid!!}")
                    }
                }else{
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Filled.Cancel, contentDescription = "Error", tint = Color.Red)
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


