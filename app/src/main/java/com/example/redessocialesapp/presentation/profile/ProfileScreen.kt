package com.example.redessocialesapp.presentation.profile

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.redessocialesapp.presentation.ArticleCard
import com.example.redessocialesapp.presentation.VideoCard
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily
import com.example.redessocialesapp.util.Screens

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel, userId: String, show : Boolean) {

    // Obtener el usuario
    val user by viewModel.user.collectAsState()

    // Instanciar al usuario al iniciar la pantalla
    LaunchedEffect(Unit) {
        if (userId == "") {
            Log.d("Current User", "Getting current user")
            viewModel.getCurrentUser()

        } else {
            Log.d("External User", "Getting external user")
            viewModel.getUser(userId)
        }
    }



    val isCurrentUser by viewModel.isCurrentUser.collectAsState()

    val favArticles by viewModel.favArticles.collectAsState()

    val favVideos by viewModel.favVideos.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }


    Log.d("User", user.toString())
    if (user.userId.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Debes iniciar sesión para ver tu perfil",
                fontSize = 18.sp,
                fontFamily = poppinsFontFamily,
                color = Color.Black
            )

            Row(modifier = Modifier.padding(top = 16.dp)) {
                TextButton(
                    onClick = {
                        navController.navigate(Screens.SignUpScreen.route) {
                            popUpTo(Screens.ProfileScreen.route) {
                                inclusive = true
                            }
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        backgroundColor = Color.LightGray),
                    modifier = Modifier
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = "Registrarse",
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                    )
                }
                TextButton(
                    onClick = {
                        navController.navigate(Screens.LoginScreen.route) {
                            popUpTo(Screens.ProfileScreen.route) {
                                inclusive = true
                            }
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = AppColor
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                    )
                }
            }


        }
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = if (show) 16.dp else 50.dp, start = if (show) 16.dp else 0.dp, end = if (show) 16.dp else 0.dp)
        ) {
            if (show){
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
                            text = "Detalles de Perfil",
                            fontSize = 20.sp,
                            color = AppColor,
                            fontFamily = poppinsFontFamily,
                            modifier = Modifier.clickable { }
                        )
                    }
                    Box(contentAlignment = Alignment.CenterEnd) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.imageURL)
                            .crossfade(true)
                            .transformations(
                                CircleCropTransformation()
                            )
                            .scale(Scale.FIT)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(130.dp)
                    )
                    Text(
                        text = user.firstName + " " + user.lastName,
                        fontSize = 22.sp,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 10.dp),
                        color = Color.Black
                    )
                    Text(
                        text = user.email,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier.padding(top = 8.dp, bottom = 10.dp),
                        color = Color.Gray
                    )
                    if (isCurrentUser) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            TextButton(
                                onClick = {
                                    viewModel.signOut()
                                    navController.navigate(Screens.HomeScreen.route) {
                                        popUpTo(
                                            Screens.ProfileScreen.route
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }, colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    backgroundColor = Color.Red
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    Icons.Default.ExitToApp,
                                    contentDescription = "Editar Perfil",
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Cerrar sesión",
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFontFamily,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = Color.White
                                )
                            }
                            TextButton(
                                onClick = {
                                    navController.navigate(Screens.EditProfileScreen.route) {
                                        popUpTo(
                                            Screens.ProfileScreen.route
                                        ) {
                                            inclusive = false
                                        }
                                    }
                                }, colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    backgroundColor = AppColor
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar Perfil",
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Editar Perfil",
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFontFamily,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = Color.White
                                )
                            }


                        }

                    }


                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = MaterialTheme.colors.onSurface,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                                color = AppColor, // Color de la línea
                                height = 2.dp// Grosor de la línea

                            )
                        }
                    ) {
                        Tab(
                            text = { Text("Noticias Favoritas") },
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 })
                        Tab(
                            text = { Text("Videos Favoritos") },
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 })
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF1F1F1))
                    ) {
                        if (selectedTabIndex == 0) {

                            items(favArticles.size) { index ->
                                val item = favArticles[index]
                                ArticleCard(item = item, true, true, isCurrentUser, {
                                    viewModel.removeFavArticle(item.articleId)
                                }, {
                                    navController.navigate("detail_article_screen/${it.articleId}") {
                                        popUpTo(Screens.ProfileScreen.route) {
                                            inclusive = false
                                        }
                                    }
                                })

                            }
                        } else {
                            items(favVideos.size) { index ->
                                val item = favVideos[index]
                                if (selectedTabIndex == 1) {
                                    VideoCard(item = item, true, true, isCurrentUser, {
                                        viewModel.removeFavVideo(item.videoId)
                                    }, {
                                        navController.navigate("detail_video_screen/${it.videoId}"){
                                            popUpTo(Screens.ProfileScreen.route){
                                                inclusive = false
                                            }
                                        }
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
