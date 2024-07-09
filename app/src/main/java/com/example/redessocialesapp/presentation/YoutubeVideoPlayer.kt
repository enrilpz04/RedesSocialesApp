package com.example.redessocialesapp.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayer(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner
){
    val context = LocalContext.current
    val youtubePlayerView = remember { YouTubePlayerView(context) }
    var youtubePlayer: YouTubePlayer? = null

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
        factory = { youtubePlayerView },
        update = { view ->
            lifecycleOwner.lifecycle.addObserver(view)
        }
    )

    LaunchedEffect(youtubeVideoId) {
        youtubePlayer?.cueVideo(youtubeVideoId, 0f)
    }

    DisposableEffect(youtubePlayerView) {
        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youtubePlayer = player
                player.cueVideo(youtubeVideoId, 0f)
            }
        }

        youtubePlayerView.addYouTubePlayerListener(listener)

        onDispose {
            youtubePlayerView.removeYouTubePlayerListener(listener)
        }
    }
}
