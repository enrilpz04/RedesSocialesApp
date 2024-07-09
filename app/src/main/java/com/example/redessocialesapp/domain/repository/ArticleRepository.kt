package com.example.redessocialesapp.domain.repository

import com.example.redessocialesapp.domain.model.Article
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun getMostRecentArticles(numArticles: Int) : Flow<List<Article>>

    fun getMostPopularArticles(numArticles: Int) : Flow<List<Article>>

    fun getArticlesByTitle(title: String) : Flow<List<Article>>

    fun getArticlesByCategory(category: String) : Flow<List<Article>>

    fun getArticleById(articleId: String) : Flow<Article>

    fun getAllArticles() : Flow<List<Article>>
}