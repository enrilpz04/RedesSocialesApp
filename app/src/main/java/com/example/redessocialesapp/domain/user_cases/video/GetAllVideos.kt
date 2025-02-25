package com.example.redessocialesapp.domain.user_cases.video

import com.example.redessocialesapp.domain.repository.ArticleRepository
import com.example.redessocialesapp.domain.repository.VideoRepository
import javax.inject.Inject

class GetAllVideos @Inject constructor(
    private val repository: VideoRepository
) {
    operator fun invoke() = repository.getAllVideos()
}