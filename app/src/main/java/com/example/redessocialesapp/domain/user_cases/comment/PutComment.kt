package com.example.redessocialesapp.domain.user_cases.comment

import com.example.redessocialesapp.domain.model.Comment
import com.example.redessocialesapp.domain.repository.CommentRepository
import javax.inject.Inject

class PutComment @Inject constructor(
    private val commentRepository: CommentRepository
) {
    operator fun invoke(colecction: String, articleId: String, comment: Comment) =
        commentRepository.putComment(colecction, articleId, comment)
}