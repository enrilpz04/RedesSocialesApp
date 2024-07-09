package com.example.redessocialesapp.domain.user_cases.fav_articles

data class FavArticlesUseCases(
    val getFavArticlesByUserId: GetFavArticlesByUserId,
    val putFavArticle: PutFavArticle,
    val deleteFavArticle: DeleteFavArticle,
    val getFavArticleByUserIdAndArticleId: GetFavArticleByUserIdAndArticleId
)
