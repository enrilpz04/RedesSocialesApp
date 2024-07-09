package com.example.redessocialesapp.di

import com.example.redessocialesapp.data.ArticleRepositoryImpl
import com.example.redessocialesapp.data.AuthenticationRepositoryImpl
import com.example.redessocialesapp.data.CategoryRepositoryImpl
import com.example.redessocialesapp.data.CommentRepositoryImpl
import com.example.redessocialesapp.data.FavArticleRepositoryImpl
import com.example.redessocialesapp.data.FavVideoRepositoryImpl
import com.example.redessocialesapp.data.UserRepositoryImpl
import com.example.redessocialesapp.data.VideoRepositoryImpl
import com.example.redessocialesapp.domain.repository.ArticleRepository
import com.example.redessocialesapp.domain.repository.AuthenticationRepository
import com.example.redessocialesapp.domain.repository.CategoryRepository
import com.example.redessocialesapp.domain.repository.CommentRepository
import com.example.redessocialesapp.domain.repository.FavArticleRepository
import com.example.redessocialesapp.domain.repository.FavVideoRepository
import com.example.redessocialesapp.domain.repository.UserRepository
import com.example.redessocialesapp.domain.repository.VideoRepository
import com.example.redessocialesapp.domain.user_cases.article.ArticleUseCases
import com.example.redessocialesapp.domain.user_cases.article.GetAllArticles
import com.example.redessocialesapp.domain.user_cases.article.GetArticleById
import com.example.redessocialesapp.domain.user_cases.article.GetArticlesByCategory
import com.example.redessocialesapp.domain.user_cases.article.GetArticlesByTitle
import com.example.redessocialesapp.domain.user_cases.article.GetMostPopularArticles
import com.example.redessocialesapp.domain.user_cases.article.GetMostRecentArticles
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.auth.FirebaseAuthState
import com.example.redessocialesapp.domain.user_cases.auth.FirebaseSignIn
import com.example.redessocialesapp.domain.user_cases.auth.FirebaseSignOut
import com.example.redessocialesapp.domain.user_cases.auth.FirebaseSignUp
import com.example.redessocialesapp.domain.user_cases.auth.GetCurrentUserId
import com.example.redessocialesapp.domain.user_cases.auth.GoogleSignIn
import com.example.redessocialesapp.domain.user_cases.auth.IsUserAuthenticated
import com.example.redessocialesapp.domain.user_cases.auth.UpdateEmail
import com.example.redessocialesapp.domain.user_cases.auth.UpdatePassword
import com.example.redessocialesapp.domain.user_cases.category.CategoriesUseCases
import com.example.redessocialesapp.domain.user_cases.category.GetAllCategories
import com.example.redessocialesapp.domain.user_cases.comment.CommentUseCases
import com.example.redessocialesapp.domain.user_cases.comment.DeleteComment
import com.example.redessocialesapp.domain.user_cases.comment.GetCommentsByArticleId
import com.example.redessocialesapp.domain.user_cases.comment.PutComment
import com.example.redessocialesapp.domain.user_cases.fav_articles.DeleteFavArticle
import com.example.redessocialesapp.domain.user_cases.fav_articles.FavArticlesUseCases
import com.example.redessocialesapp.domain.user_cases.fav_articles.GetFavArticleByUserIdAndArticleId
import com.example.redessocialesapp.domain.user_cases.fav_articles.GetFavArticlesByUserId
import com.example.redessocialesapp.domain.user_cases.fav_articles.PutFavArticle
import com.example.redessocialesapp.domain.user_cases.fav_videos.DeleteFavVideo
import com.example.redessocialesapp.domain.user_cases.fav_videos.FavVideosUseCases
import com.example.redessocialesapp.domain.user_cases.fav_videos.GetFavVideoByUserIdAndVideoId
import com.example.redessocialesapp.domain.user_cases.fav_videos.GetFavVideosByUserId
import com.example.redessocialesapp.domain.user_cases.fav_videos.PutFavVideo
import com.example.redessocialesapp.domain.user_cases.user.GetUserById
import com.example.redessocialesapp.domain.user_cases.user.UpdateUser
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import com.example.redessocialesapp.domain.user_cases.video.GetAllVideos
import com.example.redessocialesapp.domain.user_cases.video.GetMostPopularVideos
import com.example.redessocialesapp.domain.user_cases.video.GetMostRecentVideos
import com.example.redessocialesapp.domain.user_cases.video.GetSomeVideos
import com.example.redessocialesapp.domain.user_cases.video.GetVideoById
import com.example.redessocialesapp.domain.user_cases.video.GetVideosByCategory
import com.example.redessocialesapp.domain.user_cases.video.GetVideosByTitle
import com.example.redessocialesapp.domain.user_cases.video.VideoUserCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RedesSocialesAppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthenticationRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(auth = auth, firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideAuthUseCases(repository: AuthenticationRepository) = AuthenticationUseCases(
        isUserAuthenticated = IsUserAuthenticated(repository = repository),
        firebaseAuthState = FirebaseAuthState(repository = repository),
        firebaseSignOut = FirebaseSignOut(repository = repository),
        firebaseSignIn = FirebaseSignIn(repository = repository),
        firebaseSignUp = FirebaseSignUp(repository = repository),
        googleSignIn = GoogleSignIn(repository = repository),
        getCurrentUser = GetCurrentUserId(repository = repository),
        updateEmail = UpdateEmail(repository = repository),
        updatePassword = UpdatePassword(repository = repository)
    )

    @Singleton
    @Provides
    fun provideArticleRepository(firestore: FirebaseFirestore): ArticleRepository {
        return ArticleRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideArticleUseCases(repository: ArticleRepository) = ArticleUseCases(
        getArticlesByCategory = GetArticlesByCategory(repository = repository),
        getMostRecentArticles = GetMostRecentArticles(repository = repository),
        getMostPopularArticles = GetMostPopularArticles(repository = repository),
        getAllArticles = GetAllArticles(repository = repository),
        getArticlesByTitle = GetArticlesByTitle(repository = repository),
        getArticleById = GetArticleById(repository = repository)
    )

    @Singleton
    @Provides
    fun provideVideoRepository(firestore: FirebaseFirestore): VideoRepository {
        return VideoRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideVideoUserCases(repository: VideoRepository) = VideoUserCases(
        getVideosByCategory = GetVideosByCategory(repository = repository),
        getVideoById = GetVideoById(repository = repository),
        getMostRecentVideos = GetMostRecentVideos(repository = repository),
        getSomeVideos = GetSomeVideos(repository = repository),
        getMostPopularVideos = GetMostPopularVideos(repository = repository),
        getAllVideos = GetAllVideos(repository = repository),
        getVideosByTitle = GetVideosByTitle(repository = repository)
    )

    @Singleton
    @Provides
    fun provideCategoryRepository(firestore: FirebaseFirestore): CategoryRepository {
        return CategoryRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideCategoryUseCases(repository: CategoryRepository) = CategoriesUseCases(
        getAllCategories = GetAllCategories(repository = repository)
    )

    @Singleton
    @Provides
    fun provideUserRepository(
        firestore: FirebaseFirestore,
    ): UserRepository {
        return UserRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideUserUseCases(repository: UserRepository) = UserUseCases(
        getUserById = GetUserById(repository = repository),
        updateUser = UpdateUser(repository = repository)
    )

    @Singleton
    @Provides
    fun provideFavArticleRepository(
        firestore: FirebaseFirestore,
    ): FavArticleRepository {
        return FavArticleRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideFavArticleUseCases(repository: FavArticleRepository) = FavArticlesUseCases(
        getFavArticlesByUserId = GetFavArticlesByUserId(repository = repository),
        putFavArticle = PutFavArticle(repository = repository),
        deleteFavArticle = DeleteFavArticle(repository = repository),
        getFavArticleByUserIdAndArticleId = GetFavArticleByUserIdAndArticleId(repository = repository)
    )

    @Singleton
    @Provides
    fun provideFavVideoRepository(
        firestore: FirebaseFirestore,
    ): FavVideoRepository {
        return FavVideoRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideFavVideoUseCases(repository: FavVideoRepository) = FavVideosUseCases(
        getFavVideosByUserId = GetFavVideosByUserId(repository = repository),
        putFavVideo = PutFavVideo(repository = repository),
        deleteFavVideo = DeleteFavVideo(repository = repository),
        getFavVideoByUserIdAndVideoId = GetFavVideoByUserIdAndVideoId(repository = repository)
    )

    @Singleton
    @Provides
    fun provideCommentRepository(
        firestore: FirebaseFirestore,
    ): CommentRepository {
        return CommentRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideCommentUseCases(repository: CommentRepository) = CommentUseCases(
        getComments = GetCommentsByArticleId(repository),
        putComment = PutComment(repository),
        deleteComment = DeleteComment(repository)
    )
}