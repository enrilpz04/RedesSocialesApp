package com.example.redessocialesapp.domain.user_cases.comment

import com.example.redessocialesapp.domain.repository.CommentRepository
import javax.inject.Inject

class DeleteComment @Inject constructor(
    private val commentRepository: CommentRepository
) {
    operator fun invoke(colecction: String, postId: String, commentId: String) =
        commentRepository.deleteComment(colecction, postId, commentId)
}
