package com.example.redessocialesapp.presentation.authentication

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redessocialesapp.R
import com.example.redessocialesapp.domain.user_cases.auth.AuthenticationUseCases
import com.example.redessocialesapp.util.Response
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCases: AuthenticationUseCases,
) : ViewModel() {

    val isUserAuthenticated get() = authUseCases.isUserAuthenticated()

    private val _signInState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signInState: State<Response<Boolean>> = _signInState

    private val _signUpState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signUpState: State<Response<Boolean>> = _signUpState

    private val _signOutState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signOutState: State<Response<Boolean>> = _signOutState

    private val _firebaseAuthState = MutableStateFlow<Boolean>(false)
    val firebaseAuthState : StateFlow<Boolean> = _firebaseAuthState

    private val _googleAuthState = mutableStateOf<Boolean>(false)
    val googleAuthState : State<Boolean> = _googleAuthState

    fun checkIfUserIsSignedIn() {
        _firebaseAuthState.value = false
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect {
                if (it.isNotEmpty()){
                    _firebaseAuthState.value = true
                } else {
                    _firebaseAuthState.value = false
                }

            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.firebaseSignIn(email = email, password = password).collect {
                _signInState.value = it
                _firebaseAuthState.value = it is Response.Success
            }
        }
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            println("signUp: Starting sign up process")
            authUseCases.firebaseSignUp(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                imageURL = "https://firebasestorage.googleapis.com/v0/b/redessocialesapp-9975b.appspot.com/o/default-profile-picture.png?alt=media&token=bc295686-c0c1-48eb-b615-54e4548a4788"

            ).collect{
                println("signUp: Received response: $it")
                _signUpState.value = it
            }
        }
    }



    fun signOut(){
        viewModelScope.launch {
            authUseCases.firebaseSignOut().collect{
                _signOutState.value = it
                if (it==Response.Success(true)){
                    _signInState.value = Response.Success(false)
                }
            }
        }
    }

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            authUseCases.googleSignIn(credential).collect { response ->
                if (response is Response.Success) {
                    _googleAuthState.value = true
                } else {
                    _googleAuthState.value = false
                }
            }
        }
    }
}
