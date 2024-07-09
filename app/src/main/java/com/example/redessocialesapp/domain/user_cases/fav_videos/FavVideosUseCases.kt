package com.example.redessocialesapp.domain.user_cases.fav_videos

data class FavVideosUseCases(
    val getFavVideosByUserId: GetFavVideosByUserId,
    val putFavVideo: PutFavVideo,
    val deleteFavVideo: DeleteFavVideo,
    val getFavVideoByUserIdAndVideoId: GetFavVideoByUserIdAndVideoId
)