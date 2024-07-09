package com.example.redessocialesapp.presentation.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.Category
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.category.CategoriesUseCases
import com.example.redessocialesapp.domain.user_cases.fav_videos.FavVideosUseCases
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import com.example.redessocialesapp.domain.user_cases.video.VideoUserCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class VideosViewModel @Inject constructor(

    // Casos de uso de videos y categorías para obtener información de la base de datos de Firebase
    private val videoUserCases: VideoUserCases,
    private val categoriesUseCases: CategoriesUseCases,
    private val favVideosUseCases: FavVideosUseCases,
    private val authUseCases: AuthenticationUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    // Flujo de videos
    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos

    // Flujo de videos favoritos
    private val _favVideos = MutableStateFlow<List<Video>>(emptyList())
    val favVideos: StateFlow<List<Video>> = _favVideos

    // Flow para el usuario
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    // Inicialización de las categorías y los videos más recientes
    // Inicialización del usuario y los artículos más recientes
    init {
        getUser()
        getMostRecentVideos(10)
    }

    // Método para obtener el usuario
    fun getUser() {
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {userId ->
                if (userId != "") {
                    userUseCases.getUserById(id = userId).collect{
                        _user.value = it
                    }
                    getFavVideos(user.value.userId)
                }
            }
        }
    }

    // Función para obtener los videos por categoría
    fun getVideosByCategory(category: String) {
        viewModelScope.launch {
            videoUserCases.getVideosByCategory(category).collect {
                _videos.value = it
            }
        }
    }

    // Función para obtener los videos más recientes
    fun getMostRecentVideos(numArticles: Int) {
        viewModelScope.launch {
            videoUserCases.getMostRecentVideos(numArticles).collect {
                _videos.value = it
            }
        }
    }

    // Función para obtener los videos más populares
    fun getMostPopularVideos(numArticles: Int) {
        viewModelScope.launch {
            videoUserCases.getMostPopularVideos(numArticles).collect {
                _videos.value = it
            }
        }
    }

    // Función para obtener los videos por título
    fun getArticlesByTitle(title: String) {
        viewModelScope.launch {
            videoUserCases.getVideosByTitle(title).collect { article ->
                _videos.value = article.filter { filter -> filter.title.isNotEmpty() }
            }
        }
    }

    /**
     * Función para obtener los videos mediante el sistema de filtrado que tiene la
     * aplicación. Tiene validaciones para prevenir campos vacíos y para filtrar los videos
     * por fecha, categoría y ubicación.
     */
    fun getArticlesByFilter(
        startDate: String,
        endDate: String,
        selectedCategories: List<String>,
        selectedLocations: List<String>
    ) {
        viewModelScope.launch {

            // Lista mutable de artículos filtrados

            // Formateo de las fechas de inicio y fin
            val inputDateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

            // Validación de campos vacíos
            var filtrarFechaInicio = startDate.isNotEmpty()
            var filtrarFechaFin = endDate.isNotEmpty()
            var filtrarCategorias = selectedCategories.isNotEmpty()
            var filtrarUbicaciones = selectedLocations.isNotEmpty()

            videoUserCases.getAllVideos().collect {

                var articlesFiltered = it.filter { article ->
                    var shouldKeep = true

                    // Validación de fecha de inicio
                    if (filtrarFechaInicio) {
                        val startDateFormatted: Date? = inputDateFormat.parse(startDate)
                        if (startDateFormatted != null && startDateFormatted > article.date.toDate()) {
                            shouldKeep = false
                        }
                    }

                    // Validación de fecha de fin
                    if (filtrarFechaFin) {
                        val endDateFormatted: Date? = inputDateFormat.parse(endDate)
                        if (endDateFormatted != null && endDateFormatted < article.date.toDate()) {
                            shouldKeep = false
                        }
                    }

                    // Validación de categorías
                    if (filtrarCategorias && !selectedCategories.contains(article.category)) {
                        shouldKeep = false
                    }

                    // Validación de ubicaciones
                    if (filtrarUbicaciones && !selectedLocations.contains(article.location)) {
                        shouldKeep = false
                    }

                    shouldKeep
                }
                _videos.value = articlesFiltered
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

    fun putFavVideo(video: Video) {
        viewModelScope.launch {
            favVideosUseCases.putFavVideo(video, user.value.userId).collect {}
            getFavVideos(user.value.userId)
        }
    }

    fun deleteFavVideo(videoId: String) {
        viewModelScope.launch {
            favVideosUseCases.deleteFavVideo(videoId, user.value.userId).collect {}
            getFavVideos(user.value.userId)
        }
    }
}