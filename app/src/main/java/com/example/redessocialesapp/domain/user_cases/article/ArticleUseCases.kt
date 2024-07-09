package com.example.redessocialesapp.domain.user_cases.article

data class ArticleUseCases(
    val getArticlesByCategory: GetArticlesByCategory,
    val getMostRecentArticles: GetMostRecentArticles,
    val getMostPopularArticles: GetMostPopularArticles,
    val getAllArticles: GetAllArticles,
    val getArticlesByTitle: GetArticlesByTitle,
    val getArticleById: GetArticleById
)