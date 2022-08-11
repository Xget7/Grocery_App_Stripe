package lol.xget.groceryapp.user.mainUser.presentation.ShopDetails

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.skydoves.landscapist.glide.GlideImage
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.R
import lol.xget.groceryapp.auth.login.presentation.components.EventDialog
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.ui.components.DialogBoxLoading
import lol.xget.groceryapp.ui.raleway
import lol.xget.groceryapp.user.mainUser.presentation.components.CategoryChip
import lol.xget.groceryapp.user.mainUser.presentation.components.SellProductsList
import lol.xget.groceryapp.user.mainUser.presentation.components.categories.getAllFoodCategories


@OptIn(
    ExperimentalCoroutinesApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ShopDetailScreen(
    navController: NavController,
    navController2: NavController,
    viewModel: ShopDetailViewModel = hiltViewModel(),
    activity: Activity


) {

    //TODO(Location into detail  , popup)

    // Make the Intent explicit by setting the Google Maps package
    val mapIntent = Intent(Intent.ACTION_VIEW, viewModel.gmmIntentUri.value)
    val phoneIntent = Intent(Intent.ACTION_DIAL, viewModel.gmmIntentUri.value)
    mapIntent.setPackage("com.google.android.apps.maps")


    val lazyListState = rememberLazyListState()
    val lazyGridState = androidx.compose.foundation.lazy.grid.rememberLazyGridState()


    val selectedCategory = viewModel.selectedCategory.value
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val scrollUpState = viewModel.scrollUp.observeAsState()
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Transparent),
        startY = 210f / 3,  // 1/3
        endY = 620f
    )


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
//            TODO("SLIDER TOP SHOPS")

            viewModel.shopBannerImage.value.let {
                Glide.with(LocalContext.current).asBitmap().transform(BlurTransformation(25, 3))
                    .load(it).into(
                    object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            bitmapState.value = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
                bitmapState.value?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Crop
                    )

                }


            }



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
                                .align(Alignment.CenterVertically)
                                .blur(30.dp),
                            shape = CircleShape,
                            elevation = 8.dp
                        ) {
                            GlideImage(
                                imageModel = viewModel.profilePhoto.value,
                                // Crop, Fit, Inside, FillHeight, FillWidth, None
                                contentScale = ContentScale.FillWidth,
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
                                color = MaterialTheme.colors.background,
                                fontSize = 29.sp,
                                style = TextStyle(
                                    fontFamily = raleway,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1
                            )

                            TextButton(onClick = { /*TODO("Open a sheet scafold with info")*/ }) {
                                Text(
                                    text = "See more information",
                                    Modifier
                                        .width(240.dp)
                                        .padding(start = 14.dp),
                                    color = MaterialTheme.colors.background,
                                    fontSize = 16.sp,
                                    style = TextStyle(
                                        fontFamily = raleway,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    maxLines = 1
                                )
                            }
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
                                    color = MaterialTheme.colors.primaryVariant,
                                    fontSize = 19.sp,
                                    style = TextStyle(
                                        fontFamily = raleway,
                                        fontWeight = FontWeight.Medium
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
                                    tint = Color.Yellow,
                                )
                                Text(
                                    text = "Average rating: ${if (viewModel.averageShopRating.value.isNaN()) "No Rating yet" else viewModel.averageShopRating.value}",
                                    Modifier
                                        .padding(start = 14.dp),
                                    color = MaterialTheme.colors.primaryVariant,
                                    fontSize = 19.sp,
                                    style = TextStyle(
                                        fontFamily = raleway,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 1
                                )
                            }

                        }


                    }
                    Spacer(modifier = Modifier.height(20.dp))


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
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                }
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    state = lazyGridState,
                    contentPadding = PaddingValues(vertical = 4.dp),
                    columns = GridCells.Fixed(2),
                ) {
                    items(viewModel.productList) { product ->
                        SellProductsList(product = product, onClick = {
                            viewModel.currentProduct.value = product
                        }, onAddToCart = {
                            navController2.navigate(
                                Destinations.ProductDetailDestinations.passProduct(
                                    viewModel.shopIdSaved.value,
                                    product.productId!!
                                )
                            )
                        })
                    }
                }

            }

            if (viewModel.state.value.loading == true) {
                CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
            }

        }
    }



    if (!viewModel.state.value.errorMsg.isNullOrEmpty()) {
        EventDialog(
            errorMessage = viewModel.state.value.errorMsg,
            onDismiss = { viewModel.hideErrorDialog() })
    }
}





