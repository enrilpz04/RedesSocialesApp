package com.example.redessocialesapp.domain.user_cases.video

import com.example.redessocialesapp.domain.repository.VideoRepository
import javax.inject.Inject

class GetMostRecentVideos @Inject constructor(
    private val repository: VideoRepository
) {
    operator fun invoke(numVideos: Int) = repository.getMostRecentVideos(numVideos)
}