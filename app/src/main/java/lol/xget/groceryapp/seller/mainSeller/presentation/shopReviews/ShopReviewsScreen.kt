package lol.xget.groceryapp.seller.mainSeller.presentation.shopReviews

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import lol.xget.groceryapp.R
import lol.xget.groceryapp.seller.mainSeller.presentation.components.ShopReviewItem
import lol.xget.groceryapp.user.mainUser.domain.User
import kotlin.coroutines.coroutineContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShopReviewsScreen(
    navController: NavController,
    viewModel: ShopReviewsViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top) {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = " Back" , tint =  MaterialTheme.colors.onSecondary)
                }
            }
        }
    ) {
        if (viewModel.state.value.isLoading == true) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
            }
        } else {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
            ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(1.dp, bottom = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Shop Reviews", fontSize = 25.sp, color = MaterialTheme.colors.primaryVariant)


                            Card(
                                modifier = Modifier
                                    .size(140.dp)
                                    .testTag(tag = "circle"),
                                shape = CircleShape,
                                elevation = 8.dp
                            ) {
                                GlideImage(
                                    imageModel = viewModel.shopImage.value,
                                    // Crop, Fit, Inside, FillHeight, FillWidth, None
                                    contentScale = ContentScale.Crop,
                                    // shows an image with a circular revealed animation.
                                    // shows a placeholder ImageBitmap when loading.
                                    placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                                    // shows an error ImageBitmap when the request failed.
                                    error = ImageBitmap.imageResource(R.drawable.error)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = viewModel.shopName.value,
                                style = MaterialTheme.typography.h4.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                fontSize = 24.sp,
                                color = MaterialTheme.colors.onSecondary
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            LazyColumn(
                                state = lazyListState,
                                contentPadding = PaddingValues(6.dp)
                            ) {
                                items(viewModel.shopReviewsList) { review ->
                                    val user by viewModel.getUserData(review.uid!!)
                                        .collectAsState(initial = User())
                                    ShopReviewItem(review = review, reviewer = user!!)
                                }
                            }
                        }


                    }
                }
            }
        }
    }


