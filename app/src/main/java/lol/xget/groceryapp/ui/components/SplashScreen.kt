package lol.xget.groceryapp.presentation.main

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import lol.xget.groceryapp.domain.util.LoginUtil.UserType.getUserType

@Composable
fun SplashScreen(navController: NavController, currentUser: FirebaseUser?){


    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
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
        delay(2000L)
        getUserType(currentUser, navController)

    }


    Box(modifier = Modifier
        .background(Color.Cyan)
        .fillMaxSize())


}

