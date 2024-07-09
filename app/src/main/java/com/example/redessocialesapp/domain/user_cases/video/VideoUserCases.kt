package com.example.redessocialesapp.domain.user_cases.video

data class VideoUserCases(
    val getVideoById: GetVideoById,
    val getVideosByCategory : GetVideosByCategory,
    val getMostRecentVideos: GetMostRecentVideos,
    val getSomeVideos: GetSomeVideos,
    val getMostPopularVideos: GetMostPopularVideos,
    val getAllVideos: GetAllVideos,
    val getVideosByTitle: GetVideosByTitle
)