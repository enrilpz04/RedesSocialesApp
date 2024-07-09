package com.example.redessocialesapp.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.fav_articles.FavArticlesUseCases
import com.example.redessocialesapp.domain.user_cases.fav_videos.FavVideosUseCases
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val favArticlesUseCases: FavArticlesUseCases,
    private val favVideosUseCases: FavVideosUseCases,
    private val authUseCases: AuthenticationUseCases
) : ViewModel() {

    // Flow para el usuario
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    // Flujo para saber si el usuario actual es el mismo que el mostrado en el perfil
    private val _isCurrentUser = MutableStateFlow(false)
    val isCurrentUser: StateFlow<Boolean> = _isCurrentUser

    // Flujo de artículos favoritos
    private val _favArticles = MutableStateFlow<List<Article>>(emptyList())
    val favArticles: StateFlow<List<Article>> = _favArticles

    // Flujo de videos favoritos
    private val _favVideos = MutableStateFlow<List<Video>>(emptyList())
    val favVideos: StateFlow<List<Video>> = _favVideos

    private val _signInValue = MutableStateFlow(false)
    val signInValue: StateFlow<Boolean> = _signInValue

    // Método para obtener el usuario mostrado en el perfil
    fun getUser(userId: String) {
        viewModelScope.launch {
            Log.d("Getting user by id", "Getting user by id $userId")
            userUseCases.getUserById(id = userId).collect {
                _user.value = it
                if (it.userId.isNotEmpty()) {
                    Log.d("User", it.toString())
                    isCurrentUser()
                    getFavArticles(user.value.userId)
                    getFavVideos(user.value.userId)
                }
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {
                _isCurrentUser.value = it.isNotEmpty()
                Log.d("Is Current User", "${isCurrentUser.value}")
                if (isCurrentUser.value){
                    getUser(it)
                } else {
                    _user.value.userId = ""
                }
            }
        }
    }

    // Método para saber si el usuario actual es el mismo que el mostrado en el perfil
    fun isCurrentUser() {
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {
                Log.d("Current User", it)
                _isCurrentUser.value = it == user.value.userId
                _signInValue.value = it == user.value.userId
            }
        }
    }

    // Método para cerrar sesión
    fun signOut() {
        viewModelScope.launch {
            authUseCases.firebaseSignOut().collect {
                _signInValue.value = false
                _isCurrentUser.value = false
            }
        }
    }

    fun getFavArticles(userId: String) {
        viewModelScope.launch {
            favArticlesUseCases.getFavArticlesByUserId(userId).collect {
                _favArticles.value = it
            }
        }
    }

    fun getFavVideos(userId: String) {
        viewModelScope.launch {
            favVideosUseCases.getFavVideosByUserId(userId).collect {
                _favVideos.value = it
            }
        }
    }

    fun removeFavArticle(articleId: String) {
        viewModelScope.launch {
            favArticlesUseCases.deleteFavArticle(articleId, user.value.userId).collect {}
            getFavArticles(user.value.userId)
        }
    }

    fun removeFavVideo(videoId: String) {
        viewModelScope.launch {
            favVideosUseCases.deleteFavVideo(videoId, user.value.userId).collect {}
            getFavVideos(user.value.userId)
        }
    }
}