package com.example.redessocialesapp.data

import android.util.Log
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.repository.ArticleRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ArticleRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ArticleRepository {
    var operationSuccessful: Boolean = false

    override fun getMostRecentArticles(numArticles: Int): Flow<List<Article>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("articles")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(numArticles.toLong())
                .get()
                .await()

            val articles = snapshot.toObjects<Article>()

            for ((index, document) in snapshot.documents.withIndex()) {
                articles[index].articleId = document.id
            }

            emit(articles)
            operationSuccessful = true
        } catch (e: Exception) {
            emit(emptyList<Article>())
            operationSuccessful = false
        }
    }

    override fun getMostPopularArticles(numArticles: Int): Flow<List<Article>> = flow {

        operationSuccessful = false
        try {
            val snapshot = firestore.collection("articles")
                .orderBy("views", Query.Direction.DESCENDING)
                .limit(numArticles.toLong())
                .get()
                .await()

            val articles = snapshot.toObjects<Article>()

            for ((index, document) in snapshot.documents.withIndex()) {
                articles[index].articleId = document.id
            }

            emit(articles)
            operationSuccessful = true
        } catch (e: Exception) {
            emit(emptyList<Article>())
            operationSuccessful = false
        }
    }

    override fun getArticlesByTitle(title: String): Flow<List<Article>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("articles")
                .get()
                .await()

            val articles = snapshot.toObjects<Article>()

            for ((index, document) in snapshot.documents.withIndex()) {
                articles[index].articleId = document.id
            }

            var filteredArticles = mutableListOf(Article())

            for (article in articles) {
                if (article.title.contains(title, ignoreCase = true)) {
                    filteredArticles.add(article)
                }
            }

            emit(filteredArticles)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(emptyList<Article>()) // Emit an empty list in case of error
            operationSuccessful = false
        }
    }

    override fun getArticlesByCategory(category: String): Flow<List<Article>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("articles")
                .whereEqualTo("category", category)
                .get()
                .await()

            val articles = snapshot.toObjects<Article>()

            for ((index, document) in snapshot.documents.withIndex()) {
                articles[index].articleId = document.id
            }

            emit(articles)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(emptyList<Article>()) // Emit an empty list in case of error
            operationSuccessful = false
        }
    }

    override fun getArticleById(articleId: String): Flow<Article> = flow {
        try {
            val snapshot = firestore.collection("articles")
                .document(articleId)
                .get()
                .await()

            val article = snapshot.toObject<Article>()
            article!!.articleId = snapshot.id
            emit(article!!)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(Article()) // Emit a default or empty Article in case of error
        }
    }

    override fun getAllArticles(): Flow<List<Article>> = flow {
        try {
            val snapshot = firestore.collection("articles")
                .get()
                .await()

            val articles = snapshot.toObjects<Article>()

            for ((index, document) in snapshot.documents.withIndex()) {
                articles[index].articleId = document.id
            }

            emit(articles)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(emptyList<Article>()) // Emit an empty list in case of error
            operationSuccessful = false
        }
    }
}
