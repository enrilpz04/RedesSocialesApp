package com.example.redessocialesapp.data

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.redessocialesapp.R
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.repository.AuthenticationRepository
import com.example.redessocialesapp.domain.user_cases.user.UserUseCases
import com.example.redessocialesapp.util.Constants
import com.example.redessocialesapp.util.Response
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthenticationRepository {
    var operationSuccesful: Boolean = false
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserId(): Flow<String> = flow {
        try {
            if (auth.currentUser == null) {
                emit("")
            } else {
                emit(auth.currentUser!!.uid)
            }
        } catch (e: Exception) {
            emit("")
        }

    }

    override fun updateEmail(newEmail: String): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.updateEmail(newEmail)?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

    override fun updatePassword(newPassword: String): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.updatePassword(newPassword)?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun firebaseSignIn(email: String, password: String): Flow<Response<Boolean>> = flow {
        operationSuccesful = false
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).await()
            operationSuccesful = true
            emit(Response.Success(operationSuccesful))
            Log.d("FirebaseSignIn", "Success")
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

    override fun firebaseSignOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

    override fun firebaseSignUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        imageURL: String
    ): Flow<Response<Boolean>> = flow {
        operationSuccesful = false
        try {
            emit(Response.Loading)
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                val userId = authResult.user!!.uid
                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    userId = userId,
                    email = email,
                    imageURL = "https://firebasestorage.googleapis.com/v0/b/redessocialesapp-9975b.appspot.com/o/default-profile-picture.png?alt=media&token=bc295686-c0c1-48eb-b615-54e4548a4788"
                )
                firestore.collection(Constants.COLLECTION_NAME_USERS).document(userId).set(user).await()
                operationSuccesful = true
                emit(Response.Success(operationSuccesful))
            } else {
                emit(Response.Error("User creation failed"))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

    override fun googleSignIn(credential: AuthCredential): Flow<Response<Boolean>> = flow {
        operationSuccesful = false
        try {
            val authResult = auth.signInWithCredential(credential).await()
            if (authResult.user != null) {
                val userId = authResult.user!!.uid
                val userDocument = firestore.collection(Constants.COLLECTION_NAME_USERS).document(userId).get().await()
                if (!userDocument.exists()) {
                    // El usuario no existe en Firestore, por lo que creamos un nuevo usuario
                    val user = User(
                        firstName = authResult.user!!.displayName ?: "",
                        lastName = "",
                        userId = userId,
                        email = authResult.user!!.email ?: "",
                        imageURL = authResult.user!!.photoUrl?.toString() ?: "https://firebasestorage.googleapis.com/v0/b/redessocialesapp-9975b.appspot.com/o/default-profile-picture.png?alt=media&token=bc295686-c0c1-48eb-b615-54e4548a4788"
                    )
                    firestore.collection(Constants.COLLECTION_NAME_USERS).document(userId).set(user).await()
                }
                operationSuccesful = true
                emit(Response.Success(operationSuccesful))
            } else {
                emit(Response.Error("Google Sign In Error"))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }


}