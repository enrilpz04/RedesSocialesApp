package com.example.redessocialesapp.util

sealed class Screens(val route:String) {
    object SplashScreen : Screens("splash_screen")
    object LoginScreen : Screens("login_screen")
    object SignUpScreen : Screens("signup_screen")
    object HomeScreen : Screens("home_screen")
    object NoticeScreen : Screens("notice_screen")
    object VideoScreen : Screens("video_screen")
    object ProfileScreen : Screens("profile_screen")
    object EditProfileScreen : Screens("edit_profile_screen")


}