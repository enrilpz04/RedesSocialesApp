package com.example.redessocialesapp.domain.user_cases.article

import com.example.redessocialesapp.domain.repository.ArticleRepository
import javax.inject.Inject

class GetMostRecentArticles @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(numArticles: Int) = repository.getMostRecentArticles(numArticles)
}