package com.example.redessocialesapp.domain.user_cases.article

import com.example.redessocialesapp.domain.repository.ArticleRepository
import javax.inject.Inject

class GetArticlesByCategory @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(category: String) = repository.getArticlesByCategory(category)
}