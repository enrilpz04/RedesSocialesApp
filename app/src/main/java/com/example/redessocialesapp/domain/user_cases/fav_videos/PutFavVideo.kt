package com.example.redessocialesapp.domain.user_cases.fav_videos

import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.domain.repository.FavVideoRepository
import javax.inject.Inject

class PutFavVideo @Inject constructor(
    private val repository: FavVideoRepository
){
    operator fun invoke(video: Video, userId : String) = repository.putFavVideo(video, userId)
}