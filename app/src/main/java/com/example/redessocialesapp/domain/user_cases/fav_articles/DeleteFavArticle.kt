package com.example.redessocialesapp.domain.user_cases.fav_articles

import com.example.redessocialesapp.domain.repository.FavArticleRepository
import javax.inject.Inject

class DeleteFavArticle @Inject constructor(
    private val repository: FavArticleRepository
){
    suspend operator fun invoke(articleId: String, userId: String) = repository.deleteFavArticle(articleId, userId)
}