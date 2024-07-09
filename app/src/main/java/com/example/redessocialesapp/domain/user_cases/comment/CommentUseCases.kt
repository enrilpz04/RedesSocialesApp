package com.example.redessocialesapp.domain.user_cases.comment

data class CommentUseCases(
    val getComments: GetCommentsByArticleId,
    val putComment: PutComment,
    val deleteComment: DeleteComment
)