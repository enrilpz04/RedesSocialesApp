package com.example.redessocialesapp.domain.user_cases.fav_videos

import com.example.redessocialesapp.domain.repository.ArticleRepository
import com.example.redessocialesapp.domain.repository.FavArticleRepository
import com.example.redessocialesapp.domain.repository.FavVideoRepository
import javax.inject.Inject

class GetFavVideosByUserId @Inject constructor(
    private val repository: FavVideoRepository
) {
    operator fun invoke(userId: String) = repository.getFavVideosByUserId(userId)
}