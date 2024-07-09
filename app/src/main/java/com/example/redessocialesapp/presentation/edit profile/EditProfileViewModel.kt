package com.example.redessocialesapp.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val authUseCases: AuthenticationUseCases
) : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading



    fun loadData() {
        _isLoading.value = true
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {
                getUser(it)
            }
        }
        _isLoading.value = false
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            userUseCases.getUserById(id = userId).collect {
                _user.value = it
            }
        }
    }

    fun updateUser(
        newFirstName: String,
        newLastName: String,
        newEmail: String,
        newPassword: String
    ) {
        viewModelScope.launch {
            authUseCases.updateEmail(newEmail).collect{}
            authUseCases.updatePassword(newPassword).collect{}
            userUseCases.updateUser(
                User(
                    userId = user.value.userId,
                    firstName = newFirstName,
                    lastName = newLastName,
                    email = newEmail,
                    imageURL = user.value.imageURL
                )
            ).collect{}
        }
    }


}