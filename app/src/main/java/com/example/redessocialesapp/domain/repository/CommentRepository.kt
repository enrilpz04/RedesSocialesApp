package com.example.redessocialesapp.domain.repository

import com.example.redessocialesapp.domain.model.Comment
import com.example.redessocialesapp.util.Response
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getCommentsByPostId(colecction : String, articleId: String): Flow<List<Comment>>

    fun putComment(
        colecction: String,
        postId: String,
        comment: Comment
    ): Flow<Response<Boolean>>

    fun deleteComment(colecction: String, postId: String, commentId: String): Flow<Response<Boolean>>
}