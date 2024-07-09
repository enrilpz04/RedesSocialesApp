package com.example.redessocialesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.redessocialesapp.presentation.BottomNavigationMenu
import com.example.redessocialesapp.presentation.NavigationBarItems
import com.example.redessocialesapp.presentation.authentication.AuthenticationViewModel
import com.example.redessocialesapp.presentation.authentication.LoginScreen
import com.example.redessocialesapp.presentation.authentication.SignUpScreen
import com.example.redessocialesapp.presentation.home.HomeScreen
import com.example.redessocialesapp.presentation.profile.ProfileScreen
import com.example.redessocialesapp.presentation.SplashScreen
import com.example.redessocialesapp.presentation.comments.CommentScreen
import com.example.redessocialesapp.presentation.comments.CommentViewModel
import com.example.redessocialesapp.presentation.detail_article.DetailArticleScreen
import com.example.redessocialesapp.presentation.detail_article.DetailArticleViewModel
import com.example.redessocialesapp.presentation.detail_video.DetailVideoViewModel
import com.example.redessocialesapp.presentation.detail_video.DetailledVideoScreen
import com.example.redessocialesapp.presentation.edit.EditProfileScreen
import com.example.redessocialesapp.presentation.edit.EditProfileViewModel
import com.example.redessocialesapp.presentation.home.HomeViewModel
import com.example.redessocialesapp.presentation.notice.ArticleScreen
import com.example.redessocialesapp.presentation.notice.ArticlesViewModel
import com.example.redessocialesapp.presentation.profile.ProfileViewModel
import com.example.redessocialesapp.presentation.video.VideoScreen
import com.example.redessocialesapp.presentation.video.VideosViewModel
import com.example.redessocialesapp.ui.theme.RedesSocialesAppTheme
import com.example.redessocialesapp.ui.theme.rememberWindowSizeClass
import com.example.redessocialesapp.util.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val window = rememberWindowSizeClass()

            RedesSocialesAppTheme(window) {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val authViewModel: AuthenticationViewModel = hiltViewModel()
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    val articlesViewModel: ArticlesViewModel = hiltViewModel()
                    val videosViewModel: VideosViewModel = hiltViewModel()
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    val editProfileViewModel: EditProfileViewModel = hiltViewModel()
                    val detailArticleViewModel: DetailArticleViewModel = hiltViewModel()
                    val detailVideoViewModel: DetailVideoViewModel = hiltViewModel()
                    val commentViewModel: CommentViewModel = hiltViewModel()

                    RedesSocialesApp(
                        navController,
                        authViewModel,
                        homeViewModel,
                        articlesViewModel,
                        videosViewModel,
                        profileViewModel,
                        editProfileViewModel,
                        detailArticleViewModel,
                        detailVideoViewModel,
                        commentViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun RedesSocialesApp(
    navController: NavHostController,
    authViewModel: AuthenticationViewModel,
    homeViewModel: HomeViewModel,
    articlesViewModel: ArticlesViewModel,
    videosViewModel: VideosViewModel,
    profileViewModel: ProfileViewModel,
    editProfileViewModel: EditProfileViewModel,
    detailArticleViewModel: DetailArticleViewModel,
    detailVideoViewModel: DetailVideoViewModel,
    commentViewModel: CommentViewModel
) {
    var showBottomNavigation by remember { mutableStateOf(true) }
    var selectedItem by remember { mutableStateOf(NavigationBarItems.Home) }

    Column {
        NavHost(
            navController = navController,
            startDestination = Screens.SplashScreen.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(route = Screens.LoginScreen.route) {
                showBottomNavigation = false
                LoginScreen(navController, authViewModel)
            }
            composable(route = Screens.SignUpScreen.route) {
                showBottomNavigation = false
                SignUpScreen(navController, authViewModel)
            }
            composable(route = Screens.SplashScreen.route) {
                showBottomNavigation = false
                SplashScreen(navController = navController)
            }
            composable(route = Screens.HomeScreen.route) {
                showBottomNavigation = true
                selectedItem = NavigationBarItems.Home
                HomeScreen(navController, homeViewModel)
            }
            composable(route = Screens.NoticeScreen.route) {
                showBottomNavigation = true
                selectedItem = NavigationBarItems.Notice
                ArticleScreen(navController, articlesViewModel)
            }
            composable(route = Screens.VideoScreen.route) {
                showBottomNavigation = true
                selectedItem = NavigationBarItems.Video
                VideoScreen(navController, videosViewModel)
            }
            composable(route = Screens.ProfileScreen.route) {
                showBottomNavigation = true
                selectedItem = NavigationBarItems.Profile
                ProfileScreen(navController, profileViewModel, "", false)
            }
            composable("profile/{userId}") {backStackEntry ->
                showBottomNavigation = false
                selectedItem = NavigationBarItems.Profile
                val userId = backStackEntry.arguments?.getString("userId")
                if (userId != null) {
                    ProfileScreen(navController, profileViewModel, userId, true)
                }
            }
            composable(route = Screens.EditProfileScreen.route) {
                showBottomNavigation = false
                EditProfileScreen(navController, editProfileViewModel)
            }
            composable("detail_article_screen/{articleId}") { backStackEntry ->
                showBottomNavigation = false
                val articleId = backStackEntry.arguments?.getString("articleId")
                DetailArticleScreen(navController, detailArticleViewModel ,articleId)
            }
            composable("detail_video_screen/{videoId}") {backStackEntry ->
                showBottomNavigation = false
                val videoId = backStackEntry.arguments?.getString("videoId")
                DetailledVideoScreen(navController, detailVideoViewModel, videoId)
            }
            composable("comment/{postId}/{collection}") { backStackEntry ->
                showBottomNavigation = false
                val postId = backStackEntry.arguments?.getString("postId")
                val collection = backStackEntry.arguments?.getString("collection")
                if (postId != null) {
                    if (collection != null) {
                        CommentScreen(navController = navController, viewModel = commentViewModel, postId = postId, colecction = collection)
                    }
                }
            }
        }

        if (showBottomNavigation) {
            BottomNavigationMenu(
                navController = navController,
                selectedItem = selectedItem
            )
        }
    }

}