package com.example.redessocialesapp.presentation.detail_video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.domain.user_cases.article.ArticleUseCases
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.fav_articles.FavArticlesUseCases
import com.example.redessocialesapp.domain.user_cases.fav_videos.FavVideosUseCases
import com.example.redessocialesapp.domain.user_cases.video.VideoUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailVideoViewModel @Inject constructor(
    private val videoUserCases: VideoUserCases,
    private val favVideoUseCases: FavVideosUseCases,
    private val authUseCases: AuthenticationUseCases
) : ViewModel() {

    private val _video = MutableStateFlow(Video())
    val video: StateFlow<Video> = _video

    private val _isFav = MutableStateFlow(false)
    val isFav: StateFlow<Boolean> = _isFav

    private val _user = MutableStateFlow(String())
    val user: StateFlow<String> = _user

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadData(videoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            coroutineScope {
                launch { getUser() }
                launch { getVideo(videoId) }
                launch { isFav() }
            }
            _isLoading.value = false
        }
    }

    fun getUser() {
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {
                _user.value = it
            }
        }
    }

    fun getVideo(videoId: String) {
        viewModelScope.launch {
            videoUserCases.getVideoById(videoId).collect {
                _video.value = it
            }
        }
    }

    fun isFav(){
        viewModelScope.launch {
            favVideoUseCases.getFavVideoByUserIdAndVideoId(user.value, video.value.videoId).collect {
                if (video.value.videoId == it.videoId){
                    _isFav.value = true
                } else {
                    _isFav.value = false
                }
            }
        }
    }

    fun putFavVideo(video : Video){
        viewModelScope.launch {
            favVideoUseCases.putFavVideo(video, user.value).collect{}
            isFav()
        }
    }

    fun deleteFavVideo(videoId : String){
        viewModelScope.launch {
            favVideoUseCases.deleteFavVideo(videoId, user.value).collect{}
            isFav()
        }
    }

}