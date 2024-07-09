package com.example.redessocialesapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.domain.user_cases.article.ArticleUseCases
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.fav_articles.FavArticlesUseCases
import com.example.redessocialesapp.domain.user_cases.fav_videos.FavVideosUseCases
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import com.example.redessocialesapp.domain.user_cases.video.VideoUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleUseCases: ArticleUseCases,
    private val videoUserCases: VideoUserCases,
    private val favVideosUseCases: FavVideosUseCases,
    private val favArticlesUseCases: FavArticlesUseCases,
    private val authUseCases: AuthenticationUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    // Flow para la lista de noticias
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    // Flow para la lista de videos
    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos

    // Flow para la lista de videos favoritos
    private val _favVideos = MutableStateFlow<List<Video>>(emptyList())
    val favVideos: StateFlow<List<Video>> = _favVideos

    // Flow para la lista de noticias favoritas
    private val _favArticles = MutableStateFlow<List<Article>>(emptyList())
    val favArticles: StateFlow<List<Article>> = _favArticles

    // Flow para el usuario
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Método init para recolectar todos los datos necesarios
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            coroutineScope {
                launch { getUser() }
                launch { getMostRecentArticles(5) }
                launch { getMostPopularVideos(3) }
            }
            _isLoading.value = false
        }
    }

    // Método para obtener el usuario
    fun getUser() {
        _isLoading.value = true
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {userId ->
                if (userId != "") {
                    userUseCases.getUserById(id = userId).collect{
                        Log.d("User", it.userId)
                        _user.value = it
                    }
                    getFavArticles(_user.value.userId)
                    getFavVideos(_user.value.userId)
                } else {
                    _user.value = User()
                    _favArticles.value = emptyList()
                    _favVideos.value = emptyList()
                }
            }
        }
        _isLoading.value = false
    }

    // Método para obtener las noticias más recientes
    fun getMostRecentArticles(numArticles: Int) {
        viewModelScope.launch {
            articleUseCases.getMostRecentArticles(numArticles).collect {
                _articles.value = it
            }
        }
    }

    // Método para obtener los videos más populares
    fun getMostPopularVideos(numVideos: Int) {
        viewModelScope.launch {
            videoUserCases.getMostPopularVideos(numVideos).collect {
                Log.d("Videos", it.size.toString())
                _videos.value = it
            }
        }
    }

    // Método para obtener los videos favoritos
    fun getFavVideos(userId: String) {
        viewModelScope.launch {
            favVideosUseCases.getFavVideosByUserId(userId).collect {
                _favVideos.value = it
            }
        }
    }

    // Método para obtener las noticias favoritas
    fun getFavArticles(userId: String) {
        viewModelScope.launch {
            favArticlesUseCases.getFavArticlesByUserId(userId).collect {
                _favArticles.value = it
            }
        }
    }

    // Método para agregar una noticia a la lista de favoritos
    fun putFavArticle(article: Article) {
        viewModelScope.launch {
                favArticlesUseCases.putFavArticle(article, user.value.userId)
                    .collect{}

            getFavArticles(user.value.userId)
        }
    }

    // Método para agregar un video a la lista de favoritos
    fun putFavVideo(video: Video) {
        viewModelScope.launch {
            favVideosUseCases.putFavVideo(video, user.value.userId).collect{}

            getFavVideos(_user.value.userId)
        }
    }

    // Método para eliminar una noticia de la lista de favoritos
    fun deleteFavArticle(articleId: String) {
        viewModelScope.launch {
            favArticlesUseCases.deleteFavArticle(articleId, user.value.userId).collect{}

            getFavArticles(user.value.userId)
        }
    }

    // Método para eliminar un video de la lista de favoritos
    fun deleteFavVideo(videoId: String) {
        viewModelScope.launch {
            favVideosUseCases.deleteFavVideo(videoId, user.value.userId).collect{}
            getFavVideos(_user.value.userId)
        }
    }
}