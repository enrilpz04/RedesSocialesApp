package com.example.redessocialesapp.domain.user_cases.auth

import com.example.redessocialesapp.domain.repository.AuthenticationRepository
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class GoogleSignIn@Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(credential: AuthCredential) = repository.googleSignIn(credential = credential)
}