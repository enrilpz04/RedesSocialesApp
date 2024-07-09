package com.example.redessocialesapp.domain.user_cases.fav_articles

import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.repository.FavArticleRepository
import javax.inject.Inject

class PutFavArticle @Inject constructor(
    private val repository: FavArticleRepository
){
    operator fun invoke(article: Article, userId: String) = repository.putFavArticle(article, userId)
}