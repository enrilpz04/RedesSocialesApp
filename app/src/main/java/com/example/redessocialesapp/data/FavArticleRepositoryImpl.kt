package com.example.redessocialesapp.data

import android.util.Log
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.repository.FavArticleRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavArticleRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FavArticleRepository {

    var operationSuccessful: Boolean = false
    override fun getFavArticleByUserIdAndArticleId(userId: String, articleId: String): Flow<Article> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("favArticles")
                .document(articleId)
                .get()
                .await()

            val article = snapshot.toObject<Article>()

            article?.articleId = snapshot.id

            emit(article ?: Article())
            operationSuccessful = true
        } catch (e: Exception) {
            emit(Article())
            operationSuccessful = false
        }
    }

    override fun getFavArticlesByUserId(userId: String): Flow<List<Article>> = flow {
        operationSuccessful = false
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("favArticles")
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

    override fun putFavArticle(article: Article, userId: String): Flow<Boolean> = flow {
        Log.d("addFavArticle", "userId: $userId, articleId: ${article.articleId}")
        operationSuccessful = false
        try {
            firestore.collection("users")
                .document(userId)
                .collection("favArticles")
                .document(article.articleId)
                .set(article)
                .await()
            emit(true)
            operationSuccessful = true
        } catch (e: Exception) {
            emit(false)
            operationSuccessful = false
            e.printStackTrace()
        }
    }

    override fun deleteFavArticle(articleId : String, userId: String): Flow<Boolean> = flow{
        Log.d("deleteFavArticle", "userId: $userId, articleId: $articleId")
        operationSuccessful = false
        try {
            firestore.collection("users")
                .document(userId)
                .collection("favArticles")
                .document(articleId)
                .delete()
                .await()
            emit(true)
            operationSuccessful = true
        } catch (e: Exception) {
            emit(false)
            operationSuccessful = false
        }

    }

}