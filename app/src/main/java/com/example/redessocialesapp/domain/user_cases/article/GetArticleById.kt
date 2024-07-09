package com.example.redessocialesapp.domain.user_cases.article

import com.example.redessocialesapp.domain.repository.ArticleRepository
import javax.inject.Inject

class GetArticleById @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(id: String) = repository.getArticleById(id)
}