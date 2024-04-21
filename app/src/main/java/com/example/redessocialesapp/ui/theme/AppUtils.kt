package com.example.redessocialesapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun ProvideAppUtils(
    dimensions: Dimensions,
    orientation: Orientation,
    content: @Composable () -> Unit
) {
    val dimSet = remember {
        dimensions
    }

    val oriSet = remember {
        orientation
    }
    CompositionLocalProvider(
        LocalAppDimens provides dimSet,
        LocalOrientationMode provides oriSet,
        content = content
    )
}

val LocalAppDimens = compositionLocalOf {
    smallDimensions
}

val LocalOrientationMode = compositionLocalOf {
    Orientation.Portrait
}