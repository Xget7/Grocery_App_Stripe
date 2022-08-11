package lol.xget.groceryapp.seller.mainSeller.presentation.components

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.skydoves.landscapist.glide.GlideImage
import hilt_aggregated_deps._lol_xget_groceryapp_user_mainUser_presentation_userReviews_UserReviewsViewModel_HiltModules_KeyModule
import lol.xget.groceryapp.R
import lol.xget.groceryapp.user.mainUser.domain.Review
import lol.xget.groceryapp.user.mainUser.domain.User
import java.util.*

@Composable
fun ShopReviewItem(
    review: Review,
    reviewer: User,
) {
    val simple: DateFormat = SimpleDateFormat("dd MMM yyyy")


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Card(
                    modifier = Modifier.padding(4.dp),
                    shape = CircleShape
                ) {
                    GlideImage(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp),
                        imageModel = reviewer.profilePhoto,
                        // Crop, Fit, Inside, FillHeight, FillWidth, None
                        contentScale = ContentScale.Crop,
                        // shows an image with a circular revealed animation.
                        // shows a placeholder ImageBitmap when loading.
                        placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                        // shows an error ImageBitmap when the request failed.
                        error = ImageBitmap.imageResource(R.drawable.error)
                    )
                }


                Spacer(modifier = Modifier.width(4.dp))
                reviewer.userName?.let {
                    Text(text = it, color = MaterialTheme.colors.onSecondary, fontSize = 20.sp)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 36.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                review.rating?.let {
                    RatingBar(value = it.toFloat(),
                        onValueChange = {},
                        onRatingChanged = {},
                        config = RatingBarConfig().activeColor(MaterialTheme.colors.onSecondary)
                            .inactiveColor(Color.LightGray))
                }
                Spacer(modifier = Modifier.width(4.dp))
                review.timestamp?.let {
                    Text(text = simple.format(Date(it)),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center)
                }
            }
            review.review?.let {
                Text(text = it, fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Medium)
            }

        }


    }

}