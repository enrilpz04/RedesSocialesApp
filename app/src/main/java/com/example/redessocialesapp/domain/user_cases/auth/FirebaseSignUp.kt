package com.example.redessocialesapp.domain.user_cases.auth

import com.example.redessocialesapp.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseSignUp @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(email: String, password: String, firstName: String, lastName: String, imageURL: String) =
        repository.firebaseSignUp(email, password, firstName, lastName, imageURL)
}