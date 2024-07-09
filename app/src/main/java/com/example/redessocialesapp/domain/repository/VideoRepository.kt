package com.example.redessocialesapp.domain.repository

import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getSomeRandomVideos(numVideos: Int) : Flow<List<Video>>

    fun getMostRecentVideos(numVideos: Int) : Flow<List<Video>>

    fun getMostPopularVideos(numArticles: Int) : Flow<List<Video>>

    fun getVideosByTitle(title: String) : Flow<List<Video>>

    fun getVideosByCategory(category: String) : Flow<List<Video>>

    fun getVideoById(videoId : String) : Flow<Video>

    fun getAllVideos() : Flow<List<Video>>
}