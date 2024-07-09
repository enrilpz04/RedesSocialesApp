package com.example.redessocialesapp.presentation.detail_article

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.redessocialesapp.presentation.ValidateLoginDialog
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily
import com.example.redessocialesapp.util.Screens

@Composable
fun DetailArticleScreen(navController: NavController, viewModel: DetailArticleViewModel, articleId : String?) {

    val user by viewModel.user.collectAsState()
    val article by viewModel.article.collectAsState()
    val isFav by viewModel.isFav.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUser()
        viewModel.getArticle(articleId!!)
        viewModel.isFav()
    }

    val scrollState = rememberScrollState()

    // Variable para mostrar el diálogo de inicio de sesión
    val showDialog = remember { mutableStateOf(false) }

    // Mostrar el diálogo de inicio de sesión
    if (showDialog.value){
        ValidateLoginDialog(
            showDialog = showDialog.value,
            onDismissRequest = { showDialog.value = false },
            onRegister = { navController.navigate(Screens.SignUpScreen.route){
                popUpTo("detail_article_screen/${articleId}"){
                    inclusive = true
                }
            } },
            onLogin = { navController.navigate(Screens.LoginScreen.route){
                popUpTo("detail_article_screen/${articleId}"){
                    inclusive = true
                }
            } },
            text = "para añadir una noticia a favoritos."
        )
    }


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp).verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Box(contentAlignment = Alignment.CenterStart) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "",
                        tint = AppColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                Text(
                    text = "Detalles de noticia",
                    fontSize = 20.sp,
                    color = AppColor,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier.clickable { }
                )
            }
            Box(contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.imageURL)
                .crossfade(true)
                .transformations(
                    listOf(
                        RoundedCornersTransformation(10f)
                    )
                )
                .scale(Scale.FILL)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, top = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "",
                            modifier = Modifier
                                .size(13.dp),
                            tint = AppColor
                        )
                        Text(
                            text = article.location,
                            modifier = Modifier.padding(start = 2.dp),
                            fontSize = 12.sp,
                            fontFamily = poppinsFontFamily,
                            color = AppColor
                        )

                    }


                }
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    IconButton(onClick = {
                        if (user != ""){
                            Log.d("DetailArticleScreen", "isFav: $isFav")
                            if (isFav){
                                viewModel.deleteFavArticle(article.articleId)
                            } else {
                                viewModel.putFavArticle(article)
                            }
                        } else {
                            showDialog.value = true
                        }
                    }) {


                        Icon(
                            imageVector = if (isFav && user != "") Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp),
                            tint = if (isFav && user != "") Color.Red else Color.Black
                        )
                    }

                }


            }

        }
        Text(
            text = article.title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 8.dp
            ),
            fontSize = 20.sp,
            fontFamily = poppinsFontFamily
        )
        Text(
            text = article.description,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 8.dp
            ),
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .padding(8.dp)
            .background(AppColor)) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = "",
                modifier = Modifier
                    .size(18.dp)
                    .padding(start = 4.dp),
                tint = Color.White
            )
            Text(
                text = article.category,
                color = Color.White,
                modifier = Modifier.padding(4.dp),
                fontSize = 12.sp,
                fontFamily = poppinsFontFamily
            )


        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
            TextButton(onClick = {
                navController.navigate("comment/${article.articleId}/${"articles"}")
            }) {
                Text(
                    text = "Ver todos los comentarios >",
                    fontSize = 16.sp,
                    color = AppColor,
                    fontFamily = poppinsFontFamily
                )

            }
        }

        Divider()
    }
}
