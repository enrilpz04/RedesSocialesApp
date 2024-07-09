package com.example.redessocialesapp.domain.user_cases.fav_videos

import com.example.redessocialesapp.domain.repository.FavVideoRepository
import javax.inject.Inject

class GetFavVideoByUserIdAndVideoId @Inject constructor(
    private val repository: FavVideoRepository
){
    operator fun invoke(userId: String, videoId: String) = repository.getFavVideoByUserIdAndVideoId(userId, videoId)
}