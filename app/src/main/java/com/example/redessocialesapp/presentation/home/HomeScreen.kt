package com.example.redessocialesapp.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.presentation.CardSliderComponent
import com.example.redessocialesapp.presentation.ValidateLoginDialog
import com.example.redessocialesapp.presentation.VideoCard
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily
import com.example.redessocialesapp.util.Screens

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val isLoading by viewModel.isLoading.collectAsState()

    // Obtener la lista de artículos
    val articles by viewModel.articles.collectAsState()

    // Obtener la lista de videos favoritos
    val favArticles by viewModel.favArticles.collectAsState()

    // Variable para el estado de favoritos de las noticias
    val articleFavStatus = remember { mutableStateOf<Map<Article, Boolean>>(emptyMap()) }

    // Obtener la lista de videos
    val videos by viewModel.videos.collectAsState()

    // Obtener la lista de artículos favoritos
    val favVideos by viewModel.favVideos.collectAsState()

    // Variable para el estado de favoritos de los videos
    val videoFavStatus = remember { mutableStateOf<Map<Video, Boolean>>(emptyMap()) }

    // Actualizar los estados de articulos y videos favoritos
    LaunchedEffect(videos, favVideos, articles, favArticles) {
        videoFavStatus.value = videos.associateWith { video ->
            favVideos.contains(video)
        }
        articleFavStatus.value = articles.associateWith { article ->
            favArticles.contains(article)
        }
    }




    // Variable para mostrar el diálogo de inicio de sesión
    val showDialog = remember { mutableStateOf(false) }

    // Mostrar el diálogo de inicio de sesión
    if (showDialog.value){
        ValidateLoginDialog(
            showDialog = showDialog.value,
            onDismissRequest = { showDialog.value = false },
            onRegister = { navController.navigate(Screens.SignUpScreen.route){
                popUpTo(Screens.HomeScreen.route){
                    inclusive = false
                }
            } },
            onLogin = { navController.navigate(Screens.LoginScreen.route){
                popUpTo(Screens.HomeScreen.route){
                    inclusive = false
                }
            } },
            text = "para añadir una noticia a favoritos."
        )
    }

    // Obtener el usuario
    val user by viewModel.user.collectAsState()

    // Variable para el estado del scroll
    val scrollState = rememberScrollState()

    // Mostrar un indicador de carga o un mensaje de error si la lista de artículos o videos está vacía
    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppColor)
            }
        }
    } else {

        // Mostrar los artículos cuando la lista no esté vacía
        Column(
            modifier = Modifier
                .background(Color(0xFFF0F0F1))
                .verticalScroll(scrollState)
        ) {

            // Mostrar las noticias en el componente que muestra las tarjetas de artículos
            CardSliderComponent(viewModel.articles, viewModel.favArticles, !user.userId.isEmpty(), { article, isFavorite ->
                if (!user.userId.isEmpty()) {
                    if (isFavorite) {
                        viewModel.putFavArticle(article)
                    } else {
                        viewModel.deleteFavArticle(article.articleId)
                    }
                } else {
                    showDialog.value = true
                }

            }, {
                navController.navigate("detail_article_screen/${it.articleId}") {
                    popUpTo(Screens.HomeScreen.route) {
                        inclusive = false
                    }
                }
            })

            // Mostrar el título de la sección de videos populares
            Text(
                text = "Videos populares",
                fontFamily = poppinsFontFamily,
                fontSize = 24.sp,
                color = AppColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            // Recorrer la lista de vídeos e ir mostrando las tarjetas de vídeo por cada uno
            videos.forEach { video ->
                val isFav = videoFavStatus.value[video] ?: false

                // LLamar al componente de tarjeta de vídeo
                VideoCard(video, isFav, !user.userId.isEmpty(), true, { clickedVideo ->
                    if (!user.userId.isEmpty()) {
                        if (isFav) {
                            viewModel.deleteFavVideo(clickedVideo.videoId)
                        } else {
                            viewModel.putFavVideo(clickedVideo)
                        }
                    } else {
                        showDialog.value = true
                    }
                }, {
                    navController.navigate("detail_video_screen/${it.videoId}"){
                        popUpTo(Screens.HomeScreen.route){
                            inclusive = false
                        }
                    }
                })
            }

        }
    }
}