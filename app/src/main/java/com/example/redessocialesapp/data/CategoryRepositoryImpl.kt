package com.example.redessocialesapp.data

import com.example.redessocialesapp.domain.model.Category
import com.example.redessocialesapp.domain.repository.CategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> = flow {
        try {
            val snapshot = firestore.collection("categories")
                .get()
                .await()

            val categories = snapshot.toObjects<Category>()

            for ((index, document) in snapshot.documents.withIndex()) {
                categories[index].categoryId = document.id
            }

            emit(categories)
        } catch (e: Exception) {
            emit(emptyList<Category>())
        }
    }
}