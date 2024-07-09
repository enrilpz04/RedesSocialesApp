package com.example.redessocialesapp.data

import android.util.Log
import com.example.redessocialesapp.domain.model.Comment
import com.example.redessocialesapp.domain.repository.CommentRepository
import com.example.redessocialesapp.util.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentRepository {

    var operationSuccessful = false
    override fun getCommentsByPostId(colecction : String, postId: String): Flow<List<Comment>> = flow {
        operationSuccessful = false
        try {
            // Obtener la referencia de la subcolección de comentarios dentro del documento de artículo
            val snapshot = firestore.collection(colecction)
                .document(postId)
                .collection("comments")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()

            // Convertir los documentos obtenidos en una lista de objetos Comment
            val comments = snapshot.toObjects<Comment>()

            for ((index, document) in snapshot.documents.withIndex()) {
                comments[index].commentId = document.id
            }

            // Emitir la lista de comentarios
            emit(comments)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            // Emitir una lista vacía en caso de error
            emit(emptyList<Comment>())
            operationSuccessful = false
        }
    }

    override fun putComment(
        colecction: String,
        postId: String,
        comment: Comment
    ): Flow<Response<Boolean>> = flow{
        operationSuccessful = false
        try {
            Log.d("Collection", colecction)
            Log.d("PostId", postId)
            Log.d("User", comment.userId)
            // Crear un nuevo documento en la subcolección de comentarios
            firestore.collection(colecction)
                .document(postId)
                .collection("comments")
                .add(comment)
                .await()

            // Emitir un valor de éxito
            emit(Response.Success(true))
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            // Emitir un valor de error
            emit(Response.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun deleteComment(colecction: String, postId: String, commentId: String): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            // Eliminar el documento de comentario de la subcolección de comentarios
            firestore.collection(colecction)
                .document(postId)
                .collection("comments")
                .document(commentId)
                .delete()
                .await()

            // Emitir un valor de éxito
            emit(Response.Success(true))
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            // Emitir un valor de error
            emit(Response.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

}