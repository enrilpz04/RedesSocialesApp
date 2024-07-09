package com.example.redessocialesapp.presentation

import android.view.animation.OvershootInterpolator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.redessocialesapp.R
import com.example.redessocialesapp.presentation.authentication.AuthenticationViewModel
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.util.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Variable para la animación de la imagen
    val scale = remember{
        Animatable(0f)
    }

    // Animación de la imagen
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(durationMillis = 1500, easing = {
                OvershootInterpolator(2f).getInterpolation(it)
            })
        )
        delay(3000)
        navController.navigate(Screens.HomeScreen.route){
            popUpTo(Screens.SplashScreen.route){
                inclusive = true
            }
        }
    }

    // Contenido de la splash screen
    Box(modifier = Modifier.fillMaxSize().background(AppColor), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.rs_logo),
            contentDescription = "Splash Screen Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}