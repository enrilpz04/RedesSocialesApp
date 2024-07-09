package com.example.redessocialesapp.presentation.notice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.Category
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.user_cases.article.ArticleUseCases
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.category.CategoriesUseCases
import com.example.redessocialesapp.domain.user_cases.fav_articles.FavArticlesUseCases
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
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
class ArticlesViewModel @Inject constructor(

    // Casos de uso de artículos y categorías para obtener información de la base de datos de Firebase
    private val articleUseCases: ArticleUseCases,
    private val favArticlesUseCases: FavArticlesUseCases,
    private val authUseCases: AuthenticationUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    // Flujo de artículos
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    // Flujo de artículos favoritos
    private val _favArticles = MutableStateFlow<List<Article>>(emptyList())
    val favArticles: StateFlow<List<Article>> = _favArticles

    // Flow para el usuario
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    // Inicialización del usuario y los artículos más recientes
    init {
        getUser()
        getMostRecentArticles(10)
    }


    // Método para obtener el usuario
    fun getUser() {
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {userId ->
                if (userId != "") {
                    userUseCases.getUserById(id = userId).collect{
                        _user.value = it
                    }
                    getFavArticles(user.value.userId)
                }
            }
        }
    }

    // Función para obtener los artículos por categoría
    fun getArticlesByCategory(category: String) {
        viewModelScope.launch {
            articleUseCases.getArticlesByCategory(category).collect {
                _articles.value = it
            }
        }
    }

    // Función para obtener los artículos más recientes
    fun getMostRecentArticles(numArticles: Int) {
        viewModelScope.launch {
            articleUseCases.getMostRecentArticles(numArticles).collect {
                _articles.value = it
            }
        }
    }

    // Función para obtener los artículos más populares
    fun getMostPopularArticles(numArticles: Int) {
        viewModelScope.launch {
            articleUseCases.getMostPopularArticles(numArticles).collect {
                _articles.value = it
            }
        }
    }

    fun getArticlesByTitle(title: String) {
        viewModelScope.launch {
            articleUseCases.getArticlesByTitle(title).collect { article ->
                _articles.value = article.filter { filter -> filter.title.isNotEmpty() }
            }
        }
    }

    /**
     * Función para obtener los artículos mediante el sistema de filtrado que tiene la
     * aplicación. Tiene validaciones para prevenir campos vacíos y para filtrar los artículos
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

            articleUseCases.getAllArticles().collect {

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
                _articles.value = articlesFiltered
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

    fun putFavArticle(article: Article) {
        viewModelScope.launch {
            favArticlesUseCases.putFavArticle(article, user.value.userId).collect {}
            getFavArticles(user.value.userId)
        }
    }

    fun deleteFavArticle(articleId: String) {
        viewModelScope.launch {
            favArticlesUseCases.deleteFavArticle(articleId, user.value.userId).collect {}
            getFavArticles(user.value.userId)
        }
    }

}