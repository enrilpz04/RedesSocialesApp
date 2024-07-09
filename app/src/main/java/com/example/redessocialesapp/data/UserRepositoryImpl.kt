package com.example.redessocialesapp.data

import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserRepository {

    var operationSuccessful: Boolean = false
    override fun getUserById(id: String): Flow<User> = flow {
        try {
            val snapshot = firestore.collection("users")
                .document(id)
                .get()
                .await()

            val user = snapshot.toObject<User>()
            user!!.userId = snapshot.id
            emit(user!!)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(User()) // Emit a default or empty Article in case of error
        }
    }

    override fun updateUser(user: User): Flow<Unit> = flow {
        try {
            firestore.collection("users")
                .document(user.userId)
                .set(user)
                .await()
            emit(Unit)
            operationSuccessful = true
        } catch (e: Exception) {
            // Log the exception for debugging
            emit(Unit) // Emit a default or empty Article in case of error
        }
    }
}