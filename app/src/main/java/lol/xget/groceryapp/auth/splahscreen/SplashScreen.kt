package lol.xget.groceryapp.presentation.main

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import lol.xget.groceryapp.domain.util.LoginUtil.UserType.getUserType

@Composable
fun SplashScreen(navController: NavController, currentUser: FirebaseUser?){

//    val composition by rememberLottieComposition(
//        LottieCompositionSpec.RawRes(R.raw.code)
//    )
//
//    // control of the animation
//    val progress by animateLottieCompositionAsState(
//        // pass the composition created above
//        composition,
//        iterations = LottieConstants.IterateForever,
//        isPlaying = true,
//        speed = 1f,
//        restartOnPlay = false
//    )

    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        getUserType(currentUser, navController)


    }


    Box(modifier = Modifier
        .background(Color(0xFFF02020))
        .fillMaxSize())
}

