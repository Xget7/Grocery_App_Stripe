package lol.xget.groceryapp.user.mainUser.presentation.components


import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.pager.*
import com.google.android.material.animation.AnimationUtils.lerp
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.rememberDrawablePainter
import jp.wasabeef.composable.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import lol.xget.groceryapp.R
import kotlin.math.absoluteValue

@SuppressLint("RestrictedApi", "CheckResult")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSliding(
    banners: List<String>,

    ) {
    val pagerState = rememberPagerState(
        pageCount = banners.size,
        initialOffscreenLimit = 1
    )

    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(600)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1.0f),

        ) { page ->
            Card(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            0.85f,
                            1f,
                            1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                    }
                    .fillMaxWidth()
                    .height(270.dp)
                    .padding(8.dp, 0.dp, 8.dp, 0.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                val image = banners[page]

                Box(
                    modifier = Modifier
                        .height(290.dp)
                        .width(300.dp)
                        .background(Color.LightGray)
                        .align(Alignment.Center)
                ) {
                        GlideImage(
                            imageModel = image.ifEmpty {
                                CircularProgressIndicator()
                            },
                            contentScale = ContentScale.FillBounds,
                            previewPlaceholder = R.drawable.holder
                        )
                }
            }
        }

        //Horizontal dot indicator
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}