package com.example.redessocialesapp.domain.user_cases.fav_articles

import com.example.redessocialesapp.domain.repository.FavArticleRepository
import javax.inject.Inject

class GetFavArticleByUserIdAndArticleId @Inject constructor(
    private val repository: FavArticleRepository
){
    operator fun invoke(userId: String, articleId: String) = repository.getFavArticleByUserIdAndArticleId(userId, articleId)
}