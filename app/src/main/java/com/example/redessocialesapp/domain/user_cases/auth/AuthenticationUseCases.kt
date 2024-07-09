package com.example.redessocialesapp.domain.user_cases.auth

data class AuthenticationUseCases(
    val isUserAuthenticated: IsUserAuthenticated,
    val firebaseAuthState: FirebaseAuthState,
    val firebaseSignIn: FirebaseSignIn,
    val firebaseSignOut: FirebaseSignOut,
    val firebaseSignUp: FirebaseSignUp,
    val googleSignIn: GoogleSignIn,
    val getCurrentUser: GetCurrentUserId,
    val updateEmail: UpdateEmail,
    val updatePassword: UpdatePassword
)
