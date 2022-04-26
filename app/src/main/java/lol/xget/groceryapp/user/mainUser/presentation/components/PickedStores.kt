package lol.xget.groceryapp.user.mainUser.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import lol.xget.groceryapp.R
import lol.xget.groceryapp.seller.mainSeller.domain.ShopModel
import lol.xget.groceryapp.ui.raleway

@Composable
fun PickedStoresRow(
    shop: ShopModel,
    onClick: () -> Unit
) {


    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.background
//        color = if (isSelected) Color.LightGray else MaterialTheme.colors.onSecondary
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick),
            ) {

            Column() {
                Card(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(10),
                    elevation = 12.dp
                ) {
                    GlideImage(
                        imageModel = shop.profilePhoto,
                        // Crop, Fit, Inside, FillHeight, FillWidth, None
                        contentScale = ContentScale.Crop,
                        // shows an image with a circular revealed animation.
                        // shows a placeholder ImageBitmap when loading.
                        placeHolder = ImageBitmap.imageResource(R.drawable.holder),
                        // shows an error ImageBitmap when the request failed.
                        error = ImageBitmap.imageResource(R.drawable.error)
                    )
                }
                shop.shopName?.let {
                    Text(
                        text = it,
                        Modifier
                            .width(80.dp)
                            .padding(start = 1.dp),
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


        }

    }

}