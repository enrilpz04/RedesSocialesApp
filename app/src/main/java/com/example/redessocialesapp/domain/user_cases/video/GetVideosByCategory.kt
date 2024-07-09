package com.example.redessocialesapp.domain.user_cases.video

import com.example.redessocialesapp.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideosByCategory @Inject constructor(
    private val repository: VideoRepository
) {
    operator fun invoke(category: String) = repository.getVideosByCategory(category)
}