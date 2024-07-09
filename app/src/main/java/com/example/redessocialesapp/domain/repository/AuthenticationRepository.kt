package com.example.redessocialesapp.domain.repository

import android.content.Intent
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.util.Response
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun isUserAuthenticatedInFirebase() : Boolean

    fun getFirebaseAuthState() : Flow<Boolean>

    fun firebaseSignIn(email: String, password: String) : Flow<Response<Boolean>>

    fun firebaseSignOut() : Flow<Response<Boolean>>

    fun firebaseSignUp(email:String, password:String, firstName:String, lastName:String, imageURL: String) : Flow<Response<Boolean>>

    fun googleSignIn(credential: AuthCredential) : Flow<Response<Boolean>>

    fun getCurrentUserId(): Flow<String>

    // Método para cambiar el correo electrónico
    fun updateEmail(newEmail: String): Flow<Response<Boolean>>

    // Método para cambiar la contraseña
    fun updatePassword(newPassword: String): Flow<Response<Boolean>>
}