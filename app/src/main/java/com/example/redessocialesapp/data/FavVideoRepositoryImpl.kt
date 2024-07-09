package com.example.redessocialesapp.data

import android.util.Log
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.domain.repository.FavVideoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavVideoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FavVideoRepository {

    var operationSuccessful: Boolean = false
    override fun getFavVideoByUserIdAndVideoId(userId: String, videoId: String): Flow<Video> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("favVideos")
                .document(videoId)
                .get()
                .await()

            val video = snapshot.toObject<Video>()

            video?.videoId = snapshot.id

            emit(video ?: Video())
            operationSuccessful = true
        } catch (e: Exception) {
            emit(Video())
            operationSuccessful = false
        }
    }

    override fun getFavVideosByUserId(userId: String): Flow<List<Video>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("favVideos")
                .get()
                .await()

            val videos = snapshot.toObjects<Video>()

            for ((index, document) in snapshot.documents.withIndex()) {
                videos[index].videoId = document.id
            }

            emit(videos)
            operationSuccessful = true
        } catch (e: Exception) {
            emit(emptyList<Video>())
            operationSuccessful = false
        }
    }

    override fun putFavVideo(video: Video, userId: String): Flow<Boolean> {
        operationSuccessful = false
        return flow {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("favVideos")
                    .document(video.videoId)
                    .set(video)
                    .await()
                emit(true)
                operationSuccessful = true
            } catch (e: Exception) {
                emit(false)
                operationSuccessful = false
            }
        }
    }

    override fun deleteFavVideo(videoId: String, userId: String): Flow<Boolean> {
        operationSuccessful = false
        return flow {
            try {
                Log.d("FavVideoRepositoryImpl", "Deleting video with id: $videoId on user with id: $userId")
                firestore.collection("users")
                    .document(userId)
                    .collection("favVideos")
                    .document(videoId)
                    .delete()
                    .await()
                emit(true)
                operationSuccessful = true
            } catch (e: Exception) {
                Log.e("FavVideoRepositoryImpl", "Error deleting video: ", e)
                emit(false)
                operationSuccessful = false
            }
        }
    }
}