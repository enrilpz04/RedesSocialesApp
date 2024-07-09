package com.example.redessocialesapp.presentation.detail_article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.user_cases.article.ArticleUseCases
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.fav_articles.FavArticlesUseCases
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailArticleViewModel @Inject constructor(
    private val articleUseCases: ArticleUseCases,
    private val favArticlesUseCases: FavArticlesUseCases,
    private val authUseCases: AuthenticationUseCases
) : ViewModel() {

    private val _article = MutableStateFlow(Article())
    val article: StateFlow<Article> = _article

    private val _isFav = MutableStateFlow(false)
    val isFav: StateFlow<Boolean> = _isFav

    private val _user = MutableStateFlow(String())
    val user: StateFlow<String> = _user

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {
                _user.value = it
            }
        }
    }

    fun getArticle(articleId: String) {
        viewModelScope.launch {
            articleUseCases.getArticleById(id = articleId).collect {
                _article.value = it
            }
        }
    }

    fun isFav(){
        viewModelScope.launch {
            favArticlesUseCases.getFavArticleByUserIdAndArticleId(user.value, article.value.articleId).collect {
                if (article.value.articleId == it.articleId){
                    _isFav.value = true
                } else {
                    _isFav.value = false
                }
            }
        }
    }

    // Método para agregar una noticia a la lista de favoritos
    fun putFavArticle(article: Article) {
        viewModelScope.launch {
            favArticlesUseCases.putFavArticle(article, user.value)
                .collect{}
            isFav()
        }

    }

    // Método para eliminar una noticia de la lista de favoritos
    fun deleteFavArticle(articleId: String) {
        viewModelScope.launch {
            favArticlesUseCases.deleteFavArticle(articleId, user.value).collect{}

            isFav()
        }
    }

}