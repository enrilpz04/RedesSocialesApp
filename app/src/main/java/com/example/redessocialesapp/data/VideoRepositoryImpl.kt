package com.example.redessocialesapp.data

import android.util.Log
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.Video
import com.example.redessocialesapp.domain.repository.VideoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore
) : VideoRepository {

    var operationSuccessful = false
    override fun getSomeRandomVideos(numVideos: Int): Flow<List<Video>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("videos")
                .limit(numVideos.toLong())
                .get()
                .await()

            val videos = snapshot.toObjects<Video>()

            for ((index, document) in snapshot.documents.withIndex()) {
                videos[index].videoId = document.id
            }

            emit(videos)
            operationSuccessful = true
        } catch (e : Exception) {
            emit(emptyList())
            operationSuccessful = false
        }
    }

    override fun getMostRecentVideos(numVideos: Int): Flow<List<Video>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("videos")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(numVideos.toLong())
                .get()
                .await()

            val videos = snapshot.toObjects<Video>()

            for ((index, document) in snapshot.documents.withIndex()) {
                videos[index].videoId = document.id
            }

            emit(videos)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(emptyList<Video>()) // Emit an empty list in case of error
            operationSuccessful = false
        }
    }

    override fun getMostPopularVideos(numArticles: Int): Flow<List<Video>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("videos")
                .orderBy("views", Query.Direction.DESCENDING)
                .limit(numArticles.toLong())
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

    override fun getVideosByTitle(title: String): Flow<List<Video>> = flow{
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("videos")
                .get()
                .await()

            val videos = snapshot.toObjects<Video>()

            for ((index, document) in snapshot.documents.withIndex()) {
                videos[index].videoId = document.id
            }

            var filteredVideos = mutableListOf(Video())

            for (video in videos) {
                if (video.title.contains(title, ignoreCase = true)) {
                    filteredVideos.add(video)
                }
            }

            emit(filteredVideos)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(emptyList<Video>()) // Emit an empty list in case of error
            operationSuccessful = false
        }
    }

    override fun getVideosByCategory(category: String): Flow<List<Video>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("videos")
                .whereEqualTo("category", category)
                .get()
                .await()

            val videos = snapshot.toObjects<Video>()

            for ((index, document) in snapshot.documents.withIndex()) {
                videos[index].videoId = document.id
            }

            emit(videos)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(emptyList<Video>()) // Emit an empty list in case of error
            operationSuccessful = false
        }
    }

    override fun getVideoById(videoId: String): Flow<Video> = flow {
        try {
            val snapshot = firestore.collection("videos")
                .document(videoId)
                .get()
                .await()

            val video = snapshot.toObject<Video>()
            video!!.videoId = snapshot.id
            emit(video!!)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(Video()) // Emit a default or empty Article in case of error
        }
    }

    override fun getAllVideos(): Flow<List<Video>> = flow {
        try {
            val snapshot = firestore.collection("videos")
                .get()
                .await()

            val videos = snapshot.toObjects<Video>()

            for ((index, document) in snapshot.documents.withIndex()) {
                videos[index].videoId = document.id
            }

            emit(videos)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(emptyList<Video>()) // Emit an empty list in case of error
            operationSuccessful = false
        }
    }

}