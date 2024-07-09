package com.example.redessocialesapp.domain.user_cases.auth

import com.example.redessocialesapp.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseAuthState @Inject constructor(
    private val repository: AuthenticationRepository
)  {
    operator fun invoke() = repository.getFirebaseAuthState()
}