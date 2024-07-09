package com.example.redessocialesapp.domain.repository

import com.example.redessocialesapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface FavArticleRepository {

    fun getFavArticleByUserIdAndArticleId(userId: String, articleId: String): Flow<Article>

    fun getFavArticlesByUserId(userId: String): Flow<List<Article>>

    fun putFavArticle(
        article: Article,
        userId: String
    ): Flow<Boolean>

    fun deleteFavArticle(articleId: String, userId: String): Flow<Boolean>
}