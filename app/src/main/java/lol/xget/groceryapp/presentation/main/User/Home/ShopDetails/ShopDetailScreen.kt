package lol.xget.groceryapp.presentation.main.User.Home.ShopDetails

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.palette.BitmapPalette
import com.skydoves.landscapist.palette.PaletteBuilderInterceptor
import jp.wasabeef.composable.glide.GlideImage
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import lol.xget.groceryapp.MainActivity
import lol.xget.groceryapp.R
import lol.xget.groceryapp.common.Constants.listOfCategories
import lol.xget.groceryapp.data.localdb.CartItems
import lol.xget.groceryapp.domain.util.Screen
import lol.xget.groceryapp.presentation.auth.login.components.EventDialog
import lol.xget.groceryapp.presentation.main.Seller.Home.ProductListItem
import lol.xget.groceryapp.presentation.main.User.Account.ProfileViewModel
import lol.xget.groceryapp.presentation.main.User.Home.components.CategoryChip
import lol.xget.groceryapp.presentation.main.User.Home.components.SellProductsList
import lol.xget.groceryapp.presentation.main.categories.getAllFoodCategories
import lol.xget.groceryapp.ui.GroceryAppTheme
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.ui.raleway


@OptIn(
    ExperimentalCoroutinesApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ShopDetailScreen(
    navController: NavController,
    viewModel: ShopDetailViewModel = hiltViewModel(),
    activity: Activity


) {

    //TODO(BACK BUTTOn)
    // Create a scrollable

    // Make the Intent explicit by setting the Google Maps package
    val mapIntent = Intent(Intent.ACTION_VIEW, viewModel.gmmIntentUri.value)
    val phoneIntent = Intent(Intent.ACTION_DIAL, viewModel.gmmIntentUri.value)
    mapIntent.setPackage("com.google.android.apps.maps")


    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()

    val query = viewModel.query.value

    val selectedCategory = viewModel.selectedCategory.value

    val finalCost = viewModel.finalCost.value

    val scrollUpState = viewModel.scrollUp.observeAsState()

    viewModel.updateScrollPosition(lazyListState.firstVisibleItemIndex)


    val position by animateFloatAsState(if (scrollUpState.value == true) -150f else 0f)

    Surface(
        modifier = Modifier.graphicsLayer { translationY = position },
        elevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)

        ) {

            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                imageModel = viewModel.backgGroundImage.value,
                // Crop, Fit, Inside, FillHeight, FillWidth, None
                contentScale = ContentScale.Crop,
                // shows an image with a circular revealed animation.
                // shows a placeholder ImageBitmap when loading.
                placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                // shows an error ImageBitmap when the request failed.
                error = ImageBitmap.imageResource(R.drawable.error),
            )


            Column() {

                Column(
                    modifier = Modifier.padding(1.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(top = 20.dp, start = 10.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .size(60.dp)
                                .testTag(tag = "circle")
                                .align(Alignment.CenterVertically),
                            shape = CircleShape,
                            elevation = 12.dp
                        ) {
                            //blur image
                            GlideImage(
                                imageModel = viewModel.profilePhoto.value,
                                // Crop, Fit, Inside, FillHeight, FillWidth, None
                                contentScale = ContentScale.Crop,
                                // shows an image with a circular revealed animation.
                                // shows a placeholder ImageBitmap when loading.
                                placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                                // shows an error ImageBitmap when the request failed.
                                error = ImageBitmap.imageResource(R.drawable.error)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = viewModel.shopName.value,
                                Modifier
                                    .width(240.dp)
                                    .padding(start = 14.dp),
                                color = Color.White,
                                fontSize = 29.sp,
                                style = TextStyle(
                                    fontFamily = raleway,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1
                            )

                            Text(
                                text = "See more information",
                                Modifier
                                    .width(240.dp)
                                    .padding(start = 14.dp),
                                color = Color.White,
                                fontSize = 16.sp,
                                style = TextStyle(
                                    fontFamily = raleway,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                maxLines = 1
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Card(
                        modifier = Modifier
                            .width(320.dp)
                            .height(150.dp)
                            .padding(start = 30.dp),
                        shape = RoundedCornerShape(22.dp),
                        elevation = 20.dp,
                        backgroundColor = MaterialTheme.colors.background
                    ) {

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(top = 20.dp)

                        ) {

                            if (viewModel.shopOpen.value) {
                                Text(
                                    text = "Open Now",
                                    Modifier
                                        .padding(start = 49.dp),
                                    color = Color.Green,
                                    fontSize = 19.sp,
                                    style = TextStyle(
                                        fontFamily = raleway,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1
                                )
                            } else {
                                Text(
                                    text = "Closed Now",
                                    Modifier
                                        .padding(start = 49.dp),
                                    color = Color.Red,
                                    fontSize = 19.sp,
                                    style = TextStyle(
                                        fontFamily = raleway,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1
                                )
                            }

                            Row(
                                modifier = Modifier.padding(start = 14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Motorcycle,
                                    contentDescription = "Rating Logo",
                                    tint = Color.White
                                )

                                Text(
                                    text = "Delivery fee: $" + viewModel.deliveryFee.value,
                                    Modifier
                                        .padding(start = 14.dp),
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    style = TextStyle(
                                        fontFamily = raleway,
                                        fontWeight = FontWeight.Light
                                    ),
                                    maxLines = 1
                                )
                            }

                            Row(
                                modifier = Modifier.padding(start = 14.dp)

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating Logo",
                                    tint = Color.Yellow
                                )
                                Text(
                                    text = "Average rating: 4.0",
                                    Modifier
                                        .padding(start = 14.dp),
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    style = TextStyle(
                                        fontFamily = raleway,
                                        fontWeight = FontWeight.Light
                                    ),
                                    maxLines = 1
                                )
                            }

                        }


                    }


                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(items = getAllFoodCategories(), itemContent = { category ->
                            CategoryChip(
                                category = category.value,
                                isSelected = selectedCategory == category,
                                onSelectedCategoryChange = {
                                    viewModel.onSelectedCategoryChanged(it)
                                },
                                onExecuteSearch = viewModel::newSearch, //delegated function

                            )
                        })
                    }

                    Divider(
                        color = Color.DarkGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                }
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 4.dp),
                ) {
                    items(viewModel.productList) { product ->
                        SellProductsList(product = product, onClick = {
                            viewModel.currentProduct.value = product
                        }, onAddToCart = {
                            navController.navigate(
                                Screen.ProductDetailScreen.passProduct(
                                    viewModel.shopIdSaved.value,
                                    product.productId!!
                                )
                            )
                        })
                    }
                }

            }

            if (viewModel.state.value.loading == true) {
                DialogBoxLoading()
            }

        }
    }


    if (!viewModel.state.value.errorMsg.isNullOrEmpty()) {
        EventDialog(
            errorMessage = viewModel.state.value.errorMsg,
            onDismiss = { viewModel.hideErrorDialog() })
    }
}


