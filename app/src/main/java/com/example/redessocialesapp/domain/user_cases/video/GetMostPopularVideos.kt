package com.example.redessocialesapp.domain.user_cases.video

import com.example.redessocialesapp.domain.repository.ArticleRepository
import com.example.redessocialesapp.domain.repository.VideoRepository
import javax.inject.Inject

class GetMostPopularVideos @Inject constructor(
    private val repository: VideoRepository
) {
    operator fun invoke(numArticles: Int) = repository.getMostPopularVideos(numArticles)
}