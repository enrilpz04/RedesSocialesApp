package com.example.redessocialesapp.domain.repository

import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface FavVideoRepository {

    fun getFavVideoByUserIdAndVideoId(userId: String, videoId: String): Flow<Video>

    fun getFavVideosByUserId(userId: String): Flow<List<Video>>

    fun putFavVideo(
        video: Video,
        userId: String
    ): Flow<Boolean>

    fun deleteFavVideo(videoId : String, userId: String): Flow<Boolean>
}