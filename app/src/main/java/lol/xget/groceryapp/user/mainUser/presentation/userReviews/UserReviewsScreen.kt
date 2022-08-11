package lol.xget.groceryapp.user.mainUser.presentation.userReviews

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import lol.xget.groceryapp.R
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.domain.util.Destinations
import lol.xget.groceryapp.user.mainUser.domain.Review

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserReviewsSreen(
    navController: NavController,
    nav2Controller : NavController,
    viewModel: UserReviewsViewModel = hiltViewModel()
) {

    var rating: Float by remember { mutableStateOf(0f) }


    var reviewText by rememberSaveable() {
        mutableStateOf("")
    }
    val coroutine = rememberCoroutineScope()

    when {
        viewModel.state.value.isSucess == true -> {

            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Review placed successfully",
                    fontSize = 25.sp,
                    color = Color(0xFF5CA230)
                )

                viewModel.finished(nav2Controller)


            }

        }
        viewModel.state.value.isLoading == true -> {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(color = MaterialTheme.colors.onSecondary)
            }
        }
        else -> {
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .background(MaterialTheme.colors.onSecondary),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back Arrow",
                                    tint = MaterialTheme.colors.background
                                )
                            }
                            Spacer(modifier = Modifier.width(50.dp))
                            Text(
                                text = "Write Review",
                                style = TextStyle(
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.background
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.onSecondary,
                        onClick = {
                            coroutine.launch {
                                val review = Review(
                                    rating = rating.toString(),
                                    review = reviewText,
                                    timestamp = System.currentTimeMillis(),
                                    uid = viewModel.currentUserId
                                )
                                viewModel.uploadReview(review)
                            }

                        }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check icon",
                            tint = MaterialTheme.colors.background
                        )
                    }
                }

            )
            {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {

                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Card(
                            shape = CircleShape
                        ) {
                            GlideImage(
                                imageModel = viewModel.shopImage.value ,
                                contentScale = ContentScale.Crop,
                                placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                                error = ImageBitmap.imageResource(R.drawable.error),
                                modifier = Modifier
                                    .background(color = Color(0x56757575))
                                    .width(100.dp)
                                    .height(100.dp)
                                    .border(1.dp, color = MaterialTheme.colors.onSecondary),
                                circularReveal = CircularReveal(1),
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = viewModel.shopName.value,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onSecondary
                            ),
                            modifier = Modifier.width(400.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "How was your experience with ${viewModel.shopName.value}?, Your feedback is important to improve  our quality of services. ",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.primaryVariant
                            ),
                            modifier = Modifier.width(300.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        RatingBar(
                            value = rating,
                            modifier = Modifier.width(200.dp),
                            config = RatingBarConfig()
                                .style(RatingBarStyle.HighLighted),
                            onValueChange = {
                                rating = it
                            },
                            onRatingChanged = {
                                Log.d("TAG", "onRatingChanged: $it")
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = {
                                reviewText = it
                            },
                            modifier = Modifier.height(100.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colors.primaryVariant,
                                focusedBorderColor = Color.LightGray
                            ),
                            placeholder = {
                                Text(text = "Write here!")
                            },
                        )

                        if (viewModel.state.value.errorMsg != null) {
                            Snackbar(
                                backgroundColor = MaterialTheme.colors.onSecondary,
                                modifier = Modifier.absolutePadding(top = 328.dp)
                            ) {
                                Text(
                                    text = viewModel.state.value.errorMsg!!,
                                    color = MaterialTheme.colors.background
                                )
                            }
                        }


                    }
                }


            }
        }
    }

}