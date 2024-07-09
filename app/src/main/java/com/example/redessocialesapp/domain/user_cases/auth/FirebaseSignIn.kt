package com.example.redessocialesapp.domain.user_cases.auth

import com.example.redessocialesapp.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseSignIn @Inject constructor(
    private val repository: AuthenticationRepository
)  {
    operator fun invoke(email:String, password:String) = repository.firebaseSignIn(email, password)
}