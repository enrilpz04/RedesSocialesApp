package com.example.redessocialesapp.domain.user_cases.comment

import com.example.redessocialesapp.domain.repository.CommentRepository
import javax.inject.Inject

class GetCommentsByArticleId @Inject constructor(
    private val commentRepository: CommentRepository
) {
    operator fun invoke(colecction: String, articleId: String) =
        commentRepository.getCommentsByPostId(colecction, articleId)
}