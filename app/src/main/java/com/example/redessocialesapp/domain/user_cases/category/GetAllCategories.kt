package com.example.redessocialesapp.domain.user_cases.category

import com.example.redessocialesapp.domain.repository.CategoryRepository
import javax.inject.Inject

class GetAllCategories @Inject constructor(
private val repository: CategoryRepository
) {
    operator fun invoke() = repository.getAllCategories()
}