package com.example.redessocialesapp.domain.repository

import com.example.redessocialesapp.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories() : Flow<List<Category>>
}