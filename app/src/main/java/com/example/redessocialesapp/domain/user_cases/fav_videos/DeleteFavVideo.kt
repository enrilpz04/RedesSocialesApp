package com.example.redessocialesapp.domain.user_cases.fav_videos

import com.example.redessocialesapp.domain.repository.FavVideoRepository
import javax.inject.Inject

class DeleteFavVideo @Inject constructor(
    private val repository: FavVideoRepository
){
    suspend operator fun invoke(videoId: String, userId: String) = repository.deleteFavVideo(videoId, userId)
}