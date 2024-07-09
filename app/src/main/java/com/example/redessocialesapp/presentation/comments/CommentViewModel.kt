package com.example.redessocialesapp.presentation.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.Comment
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.repository.CommentRepository
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.comment.CommentUseCases
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentUseCases: CommentUseCases,
    private val authenticationUseCases: AuthenticationUseCases,
    private val userUseCases: UserUseCases
) : ViewModel(){

    // Flow para el usuario
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    // Flow para los comentarios
    private val _comments = MutableStateFlow(emptyList<Comment>())
    val comments: StateFlow<List<Comment>> = _comments

    init {
        getCurrentUser()
    }


    fun getCurrentUser(){
        viewModelScope.launch {
            authenticationUseCases.getCurrentUser().collect {
                _user.value.userId = it
                if (_user.value.userId != ""){
                    getUser(_user.value.userId)
                }
            }
        }
    }

    private fun getUser(userId: String){
        viewModelScope.launch {
            userUseCases.getUserById(userId).collect {
                _user.value = it
            }
        }
    }

    fun getComments(colecction : String, articleId: String){
        viewModelScope.launch {
            commentUseCases.getComments(colecction, articleId).collect {
                _comments.value = it
            }
        }
    }

    fun putComment(colecction : String, articleId: String, comment: Comment){
        viewModelScope.launch {
            commentUseCases.putComment(colecction, articleId, comment).collect{}
            getComments(colecction, articleId)
        }
    }

    fun deleteComment(colecction : String, articleId: String, commentId: String){
        viewModelScope.launch {
            commentUseCases.deleteComment(colecction, articleId, commentId).collect{}
            getComments(colecction, articleId)
        }
    }
}